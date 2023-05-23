package org.jeepay.pay.ctrl.agentpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.*;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.service.IJeePayAgentpayService;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 代付
 * @date 2018-10-02
 * @version V1.0
 */
@RestController
public class AgentpayController extends BaseController {

    private final MyLog _log = MyLog.getLog(AgentpayController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    IJeePayAgentpayService jeePayAgentpayService;

    /**
     * 商户代付接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建商户代付订单
     * 3)发起转账请求
     * 4)返回下单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/agentpay/apply")
    public String transOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户代付请求 ######");
        try {
            JSONObject po = getJsonParam(request);

            String logPrefix = "【商户API代付】";
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject agentpayContext = new JSONObject();
            MchAgentpayRecord mchAgentpayRecord = null;
            // 验证参数有效性
            Object object = validateParams(po, agentpayContext, request);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
            }            if (object instanceof MchAgentpayRecord) mchAgentpayRecord = (MchAgentpayRecord) object;
            if(mchAgentpayRecord == null) return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付网关代付失败", null, null));


            int result = jeePayAgentpayService.applyAgentpay((MchInfo) agentpayContext.get("mchInfo"), mchAgentpayRecord);

            _log.info("{}创建代付订单,结果:{}", logPrefix, result);
            if(result != 1) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "申请代付失败", null, null));
            }
            // 查询最新代付记录
            mchAgentpayRecord = rpcCommonService.rpcMchAgentpayService.findByAgentpayOrderId(mchAgentpayRecord.getAgentpayOrderId());
            Map<String, Object> map = JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            // 返回的参数
            map.put("agentpayOrderId", mchAgentpayRecord.getAgentpayOrderId());
            map.put("status", mchAgentpayRecord.getStatus());
            map.put("fee", mchAgentpayRecord.getFee());
            map.put("transMsg", mchAgentpayRecord.getTransMsg());
            map.put("extra", mchAgentpayRecord.getExtra());
            return JEEPayUtil.makeRetData(map, agentpayContext.getString("key"));
        }catch (ServiceException e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, e.getErrMsg(), null, null));
        }catch (Exception e) {
            _log.error(e, "");
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付网关系统异常", null, null));
        }
    }


    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject agentpayContext, HttpServletRequest request) {

        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 代付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户代付单号
        String amount = params.getString("amount"); 		    // 代付金额（单位分）
        Byte accountAttr = params.getByteValue("accountAttr");  // 账户属性:0-对私,1-对公,默认对私
        String accountName = params.getString("accountName");   // 收款人账户名
        String accountNo = params.getString("accountNo");       // 收款人账户号
        String province;										// 开户行所在省份
        try {
        	province = params.getString("province");
        } catch (Exception e) {
        	province = null;
        }
        String city;											// 开户行所在市
        try {
        	city = params.getString("city");
        } catch (Exception e) {
        	city = null;
        }
        String bankName;         								// 开户行名称
        try {
        	bankName = params.getString("bankName");
        } catch (Exception e) {
        	bankName = null;
        }
        String bankNumber;										// 联行号
        try {
        	bankNumber = params.getString("bankNumber");
        } catch (Exception e) {
        	bankNumber = null;
        }
        String notifyUrl = params.getString("notifyUrl"); 		// 转账结果回调URL
        String remark = params.getString("remark");	            // 备注
        String extra = params.getString("extra");		        // 扩展域
        String reqTime = params.getString("reqTime");           // 请求时间
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "参数[mchId]必填,且为数值类型.mchId=" + mchId;
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "商户不存在.mchId=" + mchId;
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户状态不可用.mchId=" + mchId;
            return errorMessage;
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "商户没有配置私钥.mchId=" + mchId;
            return errorMessage;
        }
        agentpayContext.put("key", key);
        agentpayContext.put("mchInfo", mchInfo);

        // 判断请求IP是否允许
        String clintIp = IPUtility.getClientIp(request);
        boolean isAllow = JEEPayUtil.ipAllow4Strong(clintIp, mchInfo.getAgentpayWhiteIp(), mchInfo.getAgentpayBlackIp());
        if(!isAllow) {
            errorMessage = "IP["+clintIp+"]不允许访问";
            return errorMessage;
        }

        if(StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "参数[mchOrderNo]必填";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(amount)) {
            errorMessage = "参数[amount]必填且为数字";
            return errorMessage;
        }
        if(Long.parseLong(amount) <= 0) {
            errorMessage = "参数[amount]必须大于0";
            return errorMessage;
        }
        if(StringUtils.isBlank(accountName)) {
            errorMessage = "参数[accountName]必填";
            return errorMessage;
        }
        if(!NumberUtils.isDigits(accountNo)) {
            errorMessage = "参数[accountNo]必填且为数值";
            return errorMessage;
        }
        //if(accountAttr != null && accountAttr == 1) {
        //if(StringUtils.isBlank(province)) {
        //    errorMessage = "参数[province]必填";
        //    return errorMessage;
        //}
        //if(StringUtils.isBlank(city)) {
        //    errorMessage = "参数[city]必填";
        //    return errorMessage;
        //}
        //if(StringUtils.isBlank(bankName)) {
        //    errorMessage = "参数[bankName]必填";
        //    return errorMessage;
        //}
        //if(bankNumber == null) {
        //    errorMessage = "参数[bankNumber]必填";
        //    return errorMessage;
        //}
        //}
        if(StringUtils.isBlank(remark)) {
            errorMessage = "参数[remark]必填";
            return errorMessage;
        }
        if(!DateUtil.isValidDateTime(reqTime)) {
            errorMessage = "参数[reqTime]必填,且格式为yyyyMMddHHmmss";
            return errorMessage;
        }
        // 签名信息
        if (StringUtils.isBlank(sign)) {
            errorMessage = "参数[sign]必填";
            return errorMessage;
        }

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名不通过.";
            return errorMessage;
        }

        // 验证参数通过,MchAgentpayRecord
        MchAgentpayRecord mchAgentpayRecord = new MchAgentpayRecord();
        mchAgentpayRecord.setAgentpayOrderId(MySeq.getAgentpay());
        mchAgentpayRecord.setMchOrderNo(mchOrderNo);
        mchAgentpayRecord.setMchId(mchIdL);
        mchAgentpayRecord.setMchType(mchInfo.getType());
        mchAgentpayRecord.setAccountName(accountName);              // 账户名
        mchAgentpayRecord.setAccountNo(accountNo);                  // 账号
        mchAgentpayRecord.setAmount(Long.parseLong(amount));        // 代付金额
        mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_API);    // 设置API代付
        mchAgentpayRecord.setDevice("");
        mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
        mchAgentpayRecord.setAccountAttr(accountAttr);
        mchAgentpayRecord.setProvince(province);
        mchAgentpayRecord.setCity(city);
        mchAgentpayRecord.setBankName(bankName);
        mchAgentpayRecord.setBankNumber(bankNumber);
        mchAgentpayRecord.setNotifyUrl(notifyUrl);
        mchAgentpayRecord.setRemark(remark);
        mchAgentpayRecord.setExtra(extra);
        return mchAgentpayRecord;
    }

}
