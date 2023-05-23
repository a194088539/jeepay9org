package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.AgentpayPassage;
import org.jeepay.core.entity.AgentpayPassageAccount;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 代付通道账户
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/agentpay_passage_account")
public class AgentpayPassageAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    private static final MyLog _log = MyLog.getLog(AgentpayPassageAccountController.class);

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassageAccount agentpayPassageAccount = getObject(param, AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.count(agentpayPassageAccount);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<AgentpayPassageAccount> agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), agentpayPassageAccount);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassageAccount info : agentpayPassageAccountList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            // TODO 列表中查询,数据量多时会因三方接口导致列表查询慢
            object.put("balance", queryBalance2(info.getIfTypeCode(), info.getParam()));
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentpayPassageAccount));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道子账户" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "maxDayAmount"); // 元转分
        AgentpayPassageAccount agentpayPassageAccount = getObject(param, AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.update(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代付通道子账户" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassageAccount agentpayPassageAccount = getObject(param, AgentpayPassageAccount.class);
        Integer agentpayPassageId = getIntegerRequired(param, "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST));
        }
        agentpayPassageAccount.setIfCode(agentpayPassage.getIfCode());            // 设置支付接口代码
        agentpayPassageAccount.setIfTypeCode(agentpayPassage.getIfTypeCode());    // 设置接口类型代码
        int count = rpcCommonService.rpcAgentpayPassageAccountService.add(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/balance")
    @ResponseBody
    public ResponseEntity<?> balance(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(id);
        String balanceStr = "";
        try {
            balanceStr = queryBalance(agentpayPassageAccount.getIfTypeCode(), agentpayPassageAccount.getParam());
        }catch (Exception e) {
            _log.error(e, "查询代付余额异常");
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(balanceStr));
    }

    String queryBalance(String channelType, String payParam) {
        JSONObject resObj = rpcCommonService.rpcJeePayTransService.queryBalance(channelType, payParam);
        StringBuffer sb = new StringBuffer();
        if(resObj != null && JEEPayUtil.isSuccess(resObj)) {
            String cashBalance = resObj.getString("cashBalance");
            String payBalance = resObj.getString("payBalance");
            if(StringUtils.isNotBlank(cashBalance)) {
                sb.append("[现金余额:").append(cashBalance).append("元]");
            }
            if(StringUtils.isNotBlank(payBalance)) {
                sb.append("[可代付余额:").append(payBalance).append("元]");
            }
        }else {
            sb.append("查询失败");
        }
        return sb.toString();
    }

    String queryBalance2(String channelType, String payParam) {
        String result = "查询失败";
        try{
            JSONObject resObj = rpcCommonService.rpcJeePayTransService.queryBalance(channelType, payParam);
            if(resObj != null && JEEPayUtil.isSuccess(resObj)) {
                String cashBalance = resObj.getString("cashBalance");
                return cashBalance;
            }
        }catch (Exception e) {
            _log.error(e, "");
        }
        return result;
    }

}
