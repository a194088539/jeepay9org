package org.jeepay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.SettRecord;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class QuerySettlementRecordController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryPayOrderController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询支付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/settlement/query_order")
    public String queryPayOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户结算订单查询请求 ######");
        String logPrefix = "【商户结算订单查询】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            // 验证参数有效性
            String errorMessage = validateParams(po, payContext);
            if (!"success".equalsIgnoreCase(errorMessage)) {
                _log.warn(errorMessage);
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, errorMessage, null, null));
            }
            _log.debug("请求参数及签名校验通过");
            Long mchId = po.getLong("mchId"); 			        // 商户ID
            String thirdOrderId = po.getString("mchOrderNo"); 	// 商户订单号
            String settleOrderId = po.getString("settleOrderId"); 	// 结算订单号
            SettRecord settRecord = rpcCommonService.rpcSettRecordService.findBySettThirdOrderIdOrOrderId(mchId,thirdOrderId,settleOrderId);
            _log.info("{}查询商户结算订单,结果:{}", logPrefix, settRecord);
            if (settRecord == null){
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "结算订单不存在", null, null));
            }
            Map<String, Object> map = buildRetMap(settRecord);
            _log.info("###### 商户结算订单处理完成 ######");
            return JEEPayUtil.makeRetData(map, payContext.getString("key"));
         }catch (Exception e) {
        _log.error(e, "");
        return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "支付中心系统异常", null, null));
    }
    }
    /**
     * 构建返回Map
     * @param
     * @return
     */
    Map buildRetMap(SettRecord settRecord) {
        Map<String, Object> map = new HashedMap();
        map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        map.put("mchId", StrUtil.toString(settRecord.getInfoId()));
        map.put("settleOrderId", StrUtil.toString(settRecord.getSettOrderId()));
        map.put("mchSettleOrderNo", StrUtil.toString(settRecord.getThirdOrderId()));
        map.put("amount", StrUtil.toString(settRecord.getSettAmount()));
        map.put("status", StrUtil.toString(settRecord.getSettStatus())); //结算状态:1-等待审核,2-已审核,3-审核不通过,4-打款中,5-打款成功,6-打款失败
        map.put("accountName", StrUtil.toString(settRecord.getAccountName()));
        map.put("accountNo", StrUtil.toString(settRecord.getAccountNo()));
        map.put("remark", StrUtil.toString(settRecord.getRemark()));
        return map;
    }
    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String settleOrderId = params.getString("settleOrderId"); 	// 支付订单号

        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "参数[mchId] 必填,且为数值类型.mchId=" + mchId;
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        if(StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(settleOrderId)) {
            errorMessage = "参数[mchOrderNo or settleOrderId]需要填写其中一个.mchOrderNo=" + mchOrderNo +"settleOrderId=" + settleOrderId;
            return errorMessage;
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "签名[sign]必填";
            return errorMessage;
        }

        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "商户不存在[mchId="+mchId+"] ";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户状态不可用. [mchId="+mchId+"] ";
            return errorMessage;
        }

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key)) {
            errorMessage = "商户没有配置私钥.mchId=" + mchId;
            return errorMessage;
        }
        payContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名不通过";
            return errorMessage;
        }

        return "success";
    }

}
