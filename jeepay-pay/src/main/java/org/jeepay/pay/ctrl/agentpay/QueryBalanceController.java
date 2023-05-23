package org.jeepay.pay.ctrl.agentpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.pay.ctrl.common.BaseController;
import org.jeepay.pay.service.AgentpayService;
import org.jeepay.pay.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 查询代付余额
 * @date 2017-08-31
 * @version V1.0
 */
@RestController
public class QueryBalanceController extends BaseController {

    private final MyLog _log = MyLog.getLog(QueryBalanceController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询商户代付余额接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/agentpay/query_balance")
    public String queryBalance(HttpServletRequest request) {
        _log.info("###### 开始接收商户查询代付余额请求 ######");
        String logPrefix = "【商户代付余额单查询】";
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
            Long mchId = po.getLong("mchId"); 			                    // 商户ID

            MchAccount mchAccount = rpcCommonService.rpcMchAccountService.findByMchId(mchId);
            if(mchAccount == null || mchAccount.getStatus() != MchConstant.PUB_YES) {
                return JEEPayUtil.makeRetFail(JEEPayUtil.makeRetMap(PayConstant.RETURN_VALUE_FAIL, "商户账户不存在或冻结", null, null));
            }
            Map<String, Object> map = new HashMap<>();
            map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
            map.put("agentpayBalance", mchAccount.getAgentpayBalance());                    // 代付余额
            map.put("availableAgentpayBalance", mchAccount.getAvailableAgentpayBalance());  // 可用代付余额
            _log.info("###### 商户查询代付余额处理完成 ######");
            return JEEPayUtil.makeRetData(map, payContext.getString("key"));
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
    private String validateParams(JSONObject params, JSONObject payContext) {
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 业务参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String reqTime = params.getString("reqTime");           // 请求时间
        String sign = params.getString("sign"); 				// 签名

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "参数[mchI]d必填,且为数值类型.mchId=" + mchId;
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);

        if(!DateUtil.isValidDateTime(reqTime)) {
            errorMessage = "参数[reqTime]必填,且格式为yyyyMMddHHmmss";
            return errorMessage;
        }
        // 签名信息
        if (StringUtils.isBlank(sign)) {
            errorMessage = "参数[sign]必填";
            return errorMessage;
        }

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
        payContext.put("key", key);

        // 验证签名数据
        boolean verifyFlag = JEEPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            errorMessage = "验证签名不通过.";
            return errorMessage;
        }

        return "success";
    }

}
