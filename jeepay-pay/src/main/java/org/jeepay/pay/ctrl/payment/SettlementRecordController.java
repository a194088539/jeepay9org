package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.PayEnum;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.IPUtility;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.SettRecord;
import org.jeepay.pay.ctrl.agentpay.AgentpayController;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 申请结算
 * @date 2018-10-02
 * @version V1.0
 */
@Controller
public class SettlementRecordController extends BaseController {

    private final MyLog _log = MyLog.getLog(AgentpayController.class);


    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 申请结算接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/settlement/create_order")
    @ResponseBody
    public String settlementOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户代付请求 ######");
        try {

        JSONObject po = getJsonParam(request);

        String logPrefix = "【商户申请结算接口】";

        _log.info("{}请求参数:{}", logPrefix, po);

        JSONObject settlementContext = new JSONObject();

        SettRecord settRecord = null;

        // 验证参数有效性
        Object object = validateParams(po, settlementContext, request);
        if (object instanceof String) {
            _log.info("{}参数校验不通过:{}", logPrefix, object);
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, object.toString(), null, null));
        }

        if (object instanceof SettRecord) settRecord = (SettRecord) object;
        if(settRecord == null) return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "申请结算失败", null, null));

        SettRecord settRecordOld = rpcCommonService.rpcSettRecordService.findBySettThirdOrderId(settRecord.getThirdOrderId());
        if (settRecordOld != null){
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "参数[mchOrderNo]重复，mchOrderNo="+settRecord.getThirdOrderId(), null, null));
        }

        int count = rpcCommonService.rpcSettRecordService.applySett(MchConstant.SETT_INFO_TYPE_MCH, settRecord.getInfoId(),
                settRecord.getSettAmount(), settRecord.getBankName(),settRecord.getBankNetName(), settRecord.getAccountName(), settRecord.getAccountNo(), settRecord.getProvince(), settRecord.getCity(),settRecord.getThirdOrderId());
        if(count != 1) {
            return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "申请结算失败", null, PayEnum.ERR_0010.getCode(), "DB插入支付订单失败"));
        }
        settRecord = rpcCommonService.rpcSettRecordService.findBySettOrderId(settRecord.getSettOrderId());
        Map<String, Object> map = JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
        // 返回的参数
        map.put("settOrderId", settRecord.getSettOrderId());
        map.put("thirdOrderId", settRecord.getThirdOrderId());
        map.put("status", settRecord.getSettStatus());
        map.put("amount", settRecord.getSettAmount());
        return JEEPayUtil.makeRetData(map, po.getString("key"));
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
        String bankName;										// 银行名称
		try {
        	bankName = params.getString("bankName");
        } catch (Exception e) {
        	bankName = null;
        }
        String bankNetName;										// 开户行
		try {
        	bankNetName = params.getString("bankNetName");
        } catch (Exception e) {
        	bankNetName = null;
        }
        String remark = params.getString("remark");	            // 备注
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
        boolean isAllow = JEEPayUtil.ipAllow(clintIp, mchInfo.getPayWhiteIp(), mchInfo.getPayBlackIp());
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
        //if(StringUtils.isBlank(bankNetName)) {
        //    errorMessage = "参数[bankNetName]必填";
        //    return errorMessage;
        //}
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

        SettRecord settRecord = new SettRecord();
        settRecord.setInfoId(Long.valueOf(mchId));
        settRecord.setThirdOrderId(mchOrderNo);
        settRecord.setSettAmount(Long.parseLong(amount));
        settRecord.setAccountName(accountName);
        settRecord.setAccountNo(accountNo);
        settRecord.setProvince(province);
        settRecord.setCity(city);
        settRecord.setBankName(bankName);
        settRecord.setBankNetName(bankNetName);
        settRecord.setRemark(remark);

        return settRecord;
    }
}
