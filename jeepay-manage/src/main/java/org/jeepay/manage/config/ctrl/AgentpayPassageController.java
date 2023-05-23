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
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentpayPassage;
import org.jeepay.core.entity.PayInterface;
import org.jeepay.core.entity.PayInterfaceType;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/agentpay_passage")
public class AgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassage agentpayPassage = getObject(param, AgentpayPassage.class);
        int count = rpcCommonService.rpcAgentpayPassageService.count(agentpayPassage);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), agentpayPassage);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();

        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassage info : agentpayPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentpayPassage));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassage agentpayPassage = getObject(param, AgentpayPassage.class);
        String ifCode = agentpayPassage.getIfCode();
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
        }
        agentpayPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型
        if(agentpayPassage.getFeeType() == 1) {
            agentpayPassage.setFeeEvery(null);
        }else if(agentpayPassage.getFeeType() == 2){
            Long feeEvery = getLongRequired(param, "feeEvery");
            agentpayPassage.setFeeEvery(feeEvery * 100);
            agentpayPassage.setFeeRate(null);
        }
        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/risk_update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道风控" )
    public ResponseEntity<?> updateRisk(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "maxDayAmount"); // 元转分
        AgentpayPassage agentpayPassage = getObject(param, AgentpayPassage.class);
        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/rate_update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道费率" )
    public ResponseEntity<?> updateRate(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        Byte feeType = getByteRequired(param, "feeType");
        AgentpayPassage agentpayPassage = new AgentpayPassage();
        agentpayPassage.setId(id);
        agentpayPassage.setFeeType(feeType);
        if(feeType == 1) {
            String feeRate = getStringRequired(param, "feeRate");
            agentpayPassage.setFeeRate(new BigDecimal(feeRate));
        }else if(feeType == 2) {
            Long feeEvery = getLongRequired(param, "feeEvery");
            agentpayPassage.setFeeEvery(feeEvery * 100);
        }
        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代付通道" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassage agentpayPassage = getObject(param, AgentpayPassage.class);
        String ifCode = agentpayPassage.getIfCode();
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface == null) {
            ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
        }
        agentpayPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型
        if(agentpayPassage.getFeeType() == 1) {
            agentpayPassage.setFeeEvery(null);
        }else if(agentpayPassage.getFeeType() == 2){
            Long feeEvery = getLongRequired(param, "feeEvery");
            agentpayPassage.setFeeEvery(feeEvery * 100);
            agentpayPassage.setFeeRate(null);
        }
        int count = rpcCommonService.rpcAgentpayPassageService.add(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 根据代付通道ID,获取支付账号配置定义描述
     * @param request
     * @return
     */
    @RequestMapping("/pay_config_get")
    @ResponseBody
    public ResponseEntity<?> getPayConfig(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer agentpayPassageId = getIntegerRequired(param, "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        String ifCode = agentpayPassage.getIfCode();
        String ifTypeCode = agentpayPassage.getIfTypeCode();

        // 如果接口配置了定义描述,则使用接口
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface != null && StringUtils.isNotBlank(payInterface.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterface);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterface.getIfTypeCode()));
            return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
        }
        // 使用接口类型配置的定义描述
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        if(payInterfaceType != null && StringUtils.isNotBlank(payInterfaceType.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterfaceType);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterfaceType.getIfTypeCode()));
            return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
        }
        return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
    }

}
