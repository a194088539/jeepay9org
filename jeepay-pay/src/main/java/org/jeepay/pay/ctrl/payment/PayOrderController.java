package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.PayEnum;
import org.jeepay.core.common.util.*;
import org.jeepay.core.common.vo.OrderCostFeeVO;
import org.jeepay.core.entity.*;
import org.jeepay.pay.channel.PaymentInterface;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.PayOrderService;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @Description: 支付订单,包括:统一下单,订单查询,补单等接口
 * @author aragom qq194088539
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
@Controller
public class PayOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    private PaymentInterface paymentInterface;

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/pay/create_order")
    @ResponseBody
    public String payOrder(HttpServletRequest request, HttpServletResponse response) {
        _log.info("###### 开始接收商户统一下单请求 ######");
        String logPrefix = "【商户统一下单】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            PayOrder payOrder = null;
            // 验证参数有效性
            Object object = validateParams(po, payContext, request);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, PayEnum.ERR_0014.getCode(), object.toString()));
            }
            if (object instanceof PayOrder) payOrder = (PayOrder) object;
            if(payOrder == null) return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心下单失败", null, PayEnum.ERR_0010.getCode(), "生成支付订单失败"));

            String channelId = payOrder.getChannelId();
            String channelName = channelId.substring(0, channelId.indexOf("_"));
            try {
                paymentInterface = (PaymentInterface) SpringUtil.getBean(channelName.toLowerCase() +  "PaymentService");
            }catch (BeansException e) {
                _log.error(e, "支付渠道类型[channelId="+channelId+"]实例化异常");
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "调用支付渠道失败", null, PayEnum.ERR_0010.getCode(), "支付渠道类型[channelId="+channelId+"]实例化异常"));
            }
            // 如果该通道重新定义了订单号,那么使用新的订单号
            String orderId = paymentInterface.getOrderId(payOrder);
            if(StringUtils.isNotBlank(orderId)) payOrder.setPayOrderId(orderId);
            // 如果该通道重新设置订单金额，那么重写订单金额及分润
            Long newAmount = paymentInterface.getAmount(payOrder);
            if(newAmount != null) { // 通道实现了getAmount方法
                if(newAmount == -1) {   // 表示当前金额不可用，需更换金额重新下单
                    return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心下单失败", null, PayEnum.ERR_0010.getCode(), "请更换金额重新下单"));
                }else if(newAmount > 0) {
                    payOrder.setAmount(newAmount);
                    // 重新计算订单:渠道成本费用,代理商费用,商户入账,平台利润
                    OrderCostFeeVO orderCostFeeVO = JEEPayUtil.calOrderCostFeeAndIncome(newAmount, payOrder.getChannelRate(), payOrder.getAgentRate(), payOrder.getParentAgentRate(), payOrder.getMchRate());
                    // 重新设置渠道成本及分润
                    payOrder.setChannelCost(orderCostFeeVO.getChannelCostFee());
                    payOrder.setPlatProfit(orderCostFeeVO.getPlatProfit());
                    payOrder.setAgentProfit(orderCostFeeVO.getAgentProfit());
                    payOrder.setParentAgentProfit(orderCostFeeVO.getParentAgentProfit());
                    payOrder.setMchIncome(orderCostFeeVO.getMchIncome());
                }
            }
            int result = rpcCommonService.rpcPayOrderService.createPayOrder(payOrder);
            _log.info("{}创建支付订单,结果:{}", logPrefix, result);
            if(result != 1) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心下单失败", null, PayEnum.ERR_0010.getCode(), "DB插入支付订单失败"));
            }
            // 执行支付
            JSONObject retObj = paymentInterface.pay(payOrder);
            if(retObj.get(PayConstant.RETURN_PARAM_RETCODE).equals(PayConstant.RETURN_VALUE_SUCCESS)) {
                retObj.put("payOrderId", payOrder.getPayOrderId());
                // 使用StringEscapeUtils.unescapeJava去掉字符串中的转义符号(不采用,会导致json解析报错)
                //return StringEscapeUtils.unescapeJava(JEEPayUtil.makeRetData(retObj, payContext.getString("key")));
                return JEEPayUtil.makeRetData(retObj, payContext.getString("key"));
            }else {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL,
                        "调用支付渠道失败" + (retObj.get(PayConstant.RETURN_PARAM_RETMSG) == null ? "" : ("(" + retObj.get(PayConstant.RETURN_PARAM_RETMSG) + ")")),
                        null, retObj.getString("errCode"), retObj.getString("errDes")));
            }
        }catch (Exception e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, PayEnum.ERR_0010.getCode(), "请联系技术人员查看"));
        }
    }

    /**
     * 服务端跳转
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/api/jump.htm")
    public String toPay(HttpServletRequest request, ModelMap model) throws ServletException, IOException {
        String params = request.getParameter("params");
        if(StringUtils.isNotBlank(params)) {
            String jumpForm = new String(MyBase64.decode(params));
            model.put("jumpForm", jumpForm);
        }else {
            model.put("jumpForm", "跳转出现异常,请联系管理员.");
        }
        return "payment/jump";
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject payContext, HttpServletRequest request) {
        String riskLog = "[支付风控]";
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String appId = params.getString("appId");              // 应用ID
        String productId = params.getString("productId");      // 支付产品ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String amount = params.getString("amount"); 		    // 支付金额（单位分）
        String currency = params.getString("currency");        // 币种
        String clientIp = params.getString("clientIp");	    // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String returnUrl = params.getString("returnUrl"); 		// 支付结果同步请求url
        String notifyUrl = params.getString("notifyUrl"); 		// 支付结果回调URL
        String sign = params.getString("sign"); 				// 签名
        String subject = params.getString("subject");	        // 商品主题
        String body = params.getString("body");	                // 商品描述信息
        String payPassAccountId = params.getString("payPassAccountId"); // 支付通道子账户ID,非必填

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "请求参数[mchId]不能为空且为数值类型.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "商户不存在[mchId="+mchId+"].";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户状态不可用[mchId="+mchId+"].";
            return errorMessage;
        }
        // 判断请求IP是否允许
        String clintIp = IPUtility.getClientIp(request);
        boolean isAllow = JEEPayUtil.ipAllow(clintIp, mchInfo.getPayWhiteIp(), mchInfo.getPayBlackIp());
        if(!isAllow) {
            errorMessage = "IP["+clintIp+"]不允许访问";
            return errorMessage;
        }

        Integer productIdI = null;
        if(StringUtils.isBlank(productId) || !NumberUtils.isDigits(productId)) {
            errorMessage = "请求参数[productId]不能为空且为数值类型.";
            return errorMessage;
        }
        productIdI = Integer.parseInt(productId);

        if(StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "请求参数[mchOrderNo]不能为空.";
            return errorMessage;
        }

        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "请求参数[amount]应为数值类型.";
            return errorMessage;
        }
        Long amountL = Long.parseLong(amount);
        if(amountL <= 0) {
            errorMessage = "请求参数[amount]必须大于0.";
            return errorMessage;
        }
        if(StringUtils.isBlank(currency)) {
            errorMessage = "请求参数[currency]不能为空.";
            return errorMessage;
        }
        if(StringUtils.isBlank(notifyUrl)) {
            errorMessage = "请求参数[notifyUrl]不能为空.";
            return errorMessage;
        }
        if(StringUtils.isBlank(subject)) {
            errorMessage = "请求参数[subject]不能为空.";
            return errorMessage;
        }
        if(StringUtils.isBlank(body)) {
            errorMessage = "请求参数[body]不能为空.";
            return errorMessage;
        }
        if(StringUtils.isBlank(clientIp)) {
            clientIp = IPUtility.getClientIp(request);
        }
        String channelUser = "";

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "请求参数[sign]不能为空.";
            return errorMessage;
        }

        // 查询应用信息
        if(StringUtils.isNotBlank(appId)) {
            MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchIdL, appId);
            if(mchApp == null) {
                errorMessage = "应用不存在[appId=" + appId + "]";
                return errorMessage;
            }
            if(mchApp.getStatus() != MchConstant.PUB_YES) {
                errorMessage = "应用状态不可用[appId=" + appId + "]";
                return errorMessage;
            }
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "商户私钥为空,请配置商户私钥[mchId="+mchId+"].";
            return errorMessage;
        }
        payContext.put("key", key);

        // 查询商户对应的支付渠道
        String channelMchId;
        String channelType;
        String channelId;
        BigDecimal channelRate;
        BigDecimal mchRate;
        BigDecimal parentAgentRate = null;
        BigDecimal agentRate = null;
        Integer passageAccountId;

        MchPayPassage mchPayPassage = rpcCommonService.rpcMchPayPassageService.findByMchIdAndProductId(mchIdL, productIdI);

        if(mchPayPassage == null) {
            errorMessage = "商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+"]";
            return errorMessage;
        }
        if(mchPayPassage.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户该产品的支付通道[productId="+productId+",mchId="+mchId+"]已关闭";
            return errorMessage;
        }

        // 支付通道ID
        Integer payPassageId = null;
        PayPassageAccount payPassageAccount = null;
        if(StringUtils.isNotBlank(payPassAccountId) && NumberUtils.isDigits(payPassAccountId)) {
            payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(Integer.parseInt(payPassAccountId));
            if(payPassageAccount == null || payPassageAccount.getStatus() != MchConstant.PUB_YES) {
                errorMessage = "传入子账户不可用[payPassAccountId="+payPassAccountId+",mchId="+mchId+"]";
                return errorMessage;
            }
            payPassageId = payPassageAccount.getPayPassageId();
        }else {
            // 获取通道子账户
            Object obj = payOrderService.getPayPassageAccount(mchPayPassage, riskLog, amountL);
            if(obj instanceof String) {
                return obj;
            }
            if(obj instanceof PayPassageAccount) {
                payPassageAccount = (PayPassageAccount) obj;
                payPassageId = payPassageAccount.getPayPassageId();
            }
        }

        // 判断支付通道
        if(payPassageId == null) {
            errorMessage = "无法取得可用的支付通道[productId="+productId+",mchId="+mchId+"]";
            return errorMessage;
        }

        // 判断子账户
        if(payPassageAccount == null) {
            errorMessage = "该支付通道没有可用子账户[payPassageId="+payPassageId+"]";
            return errorMessage;
        }

        passageAccountId = payPassageAccount.getId();
        channelMchId = payPassageAccount.getPassageMchId();
        channelType = payPassageAccount.getIfTypeCode();
        channelId = payPassageAccount.getIfCode();
        channelRate = payPassageAccount.getPassageRate();
        mchRate = mchPayPassage.getMchRate();

        // 处理二级代理商
        Long agentId = mchInfo.getAgentId();
        Long parentAgentId = mchInfo.getParentAgentId();
        if(agentId != null) {
            AgentPassage agentPassage = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(agentId, productIdI);
            if(agentPassage != null && agentPassage.getStatus() == MchConstant.PUB_YES) {
                agentRate = agentPassage.getAgentRate();
            }
            if(agentRate == null) {
                errorMessage = "请设置二级代理商费率";
                return errorMessage;
            }
            //处理一级代理商
            if(parentAgentId != null && parentAgentId != 0) {
                AgentPassage agentPassage2 = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(parentAgentId, productIdI);
                if(agentPassage2 != null && agentPassage2.getStatus() == MchConstant.PUB_YES) {
                    parentAgentRate = agentPassage2.getAgentRate();
                }
                if(parentAgentRate == null) {
                    errorMessage = "请设置一级代理商费率";
                    return errorMessage;
                }
            }
        }

        if(channelType == null || channelId == null) {
            errorMessage = "商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+",channelType="+channelType+",channelId="+channelId+"]";
            return errorMessage;
        }

        // 根据不同渠道,判断extra参数
//        if(PayConstant.PAY_CHANNEL_WX_JSAPI.equalsIgnoreCase(channelId)) {
//            if(StringUtils.isEmpty(extra)) {
//                errorMessage = "request params[extra] error.";
//                return errorMessage;
//            }
//            JSONObject extraObject = JSON.parseObject(extra);
//            String openId = extraObject.getString("openId");
//            if(StringUtils.isBlank(openId)) {
//                errorMessage = "request params[extra.openId] error.";
//                return errorMessage;
//            }
//            channelUser = openId;
//        }else if(PayConstant.PAY_CHANNEL_WX_MWEB.equalsIgnoreCase(channelId)) {
//            if(StringUtils.isEmpty(extra)) {
//                errorMessage = "请求参数[extra]不能为空.";
//                return errorMessage;
//            }
//            JSONObject extraObject = JSON.parseObject(extra);
//            String sceneInfo = extraObject.getString("sceneInfo");
//            if(StringUtils.isBlank(sceneInfo)) {
//                errorMessage = "请求参数[extra.sceneInfo]不能为空.";
//                return errorMessage;
//            }
//            if(StringUtils.isBlank(clientIp)) {
//                errorMessage = "请求参数[clientIp]不能为空.";
//                return errorMessage;
//            }
//        }else if(PayConstant.PAY_CHANNEL_ACCOUNTPAY_BALANCE.equalsIgnoreCase(channelId)) {
//            if(StringUtils.isEmpty(extra)) {
//                errorMessage = "请求参数[extra]不能为空.";
//                return errorMessage;
//            }
//            JSONObject extraObject = JSON.parseObject(extra);
//            String userId = extraObject.getString("userId");
//            if(StringUtils.isBlank(userId)) {
//                errorMessage = "请求参数[extra.userId]不能为空.";
//                return errorMessage;
//            }
//            channelUser = userId;
//        }

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名失败.";
            return errorMessage;
        }

        // 验证参数通过,返回JSONObject对象
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(MySeq.getPay());
        payOrder.setMchId(mchIdL);
        payOrder.setMchType(mchInfo.getType());
        payOrder.setAppId(appId);
        payOrder.setMchOrderNo(mchOrderNo);
        payOrder.setAgentId(agentId);
        payOrder.setParentAgentId(parentAgentId);
        payOrder.setProductId(productIdI);                          // 支付产品ID
        payOrder.setProductType(mchPayPassage.getProductType());    // 产品类型
        payOrder.setPassageId(payPassageId);                        // 支付通道ID
        payOrder.setPassageAccountId(passageAccountId);             // 支付通道账户ID
        payOrder.setChannelType(channelType);
        payOrder.setChannelId(channelId);
        payOrder.setAmount(amountL);
        payOrder.setCurrency(currency);
        payOrder.setClientIp(clientIp);
        payOrder.setDevice(device);
        payOrder.setSubject(subject);
        payOrder.setBody(body);
        payOrder.setExtra(extra);
        payOrder.setChannelMchId(channelMchId);
        payOrder.setChannelUser(channelUser);
        // 设置费率
        payOrder.setChannelRate(channelRate);
        payOrder.setAgentRate(agentRate);
        payOrder.setParentAgentRate(parentAgentRate);
        payOrder.setMchRate(mchRate);
        // 计算订单:渠道成本费用,代理商费用,商户入账,平台利润
        OrderCostFeeVO orderCostFeeVO = JEEPayUtil.calOrderCostFeeAndIncome(amountL, channelRate, agentRate, parentAgentRate, mchRate);
        // 设置渠道成本及分润
        payOrder.setChannelCost(orderCostFeeVO.getChannelCostFee());
        payOrder.setPlatProfit(orderCostFeeVO.getPlatProfit());
        payOrder.setAgentProfit(orderCostFeeVO.getAgentProfit());
        payOrder.setParentAgentProfit(orderCostFeeVO.getParentAgentProfit());
        payOrder.setMchIncome(orderCostFeeVO.getMchIncome());

        payOrder.setParam1(param1);
        payOrder.setParam2(param2);
        payOrder.setNotifyUrl(notifyUrl);
        payOrder.setReturnUrl(returnUrl);
        return payOrder;
    }

}
