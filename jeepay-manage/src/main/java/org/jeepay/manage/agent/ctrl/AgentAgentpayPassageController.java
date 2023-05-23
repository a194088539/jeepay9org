package org.jeepay.manage.agent.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentAgentpayPassage;
import org.jeepay.core.entity.AgentpayPassage;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/08/30
 * @description: 代理商代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_agentpay_passage")
public class AgentAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        // 得到代理已经配置的代付通道
        List<AgentAgentpayPassage> agentAgentpayPassageList = rpcCommonService.rpcAgentAgentpayPassageService.selectAllByAgentId(agentId);
        Map<String, AgentAgentpayPassage> agentAgentpayPassageMap = new HashMap<>();
        for(AgentAgentpayPassage agentAgentpayPassage : agentAgentpayPassageList) {
            agentAgentpayPassageMap.put(String.valueOf(agentAgentpayPassage.getAgentpayPassageId()), agentAgentpayPassage);
        }
        // 得到所有代付通道
        AgentpayPassage queryAgentpayPassage = new AgentpayPassage();
        queryAgentpayPassage.setStatus(MchConstant.PUB_YES);
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAll(queryAgentpayPassage);
        Map<String, AgentpayPassage> agentpayPassageMap = new HashMap<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            agentpayPassageMap.put(String.valueOf(agentpayPassage.getId()), agentpayPassage);
        }
        // 合并后返回的代理代付通道
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            AgentAgentpayPassage agentAgentpayPassage = agentAgentpayPassageMap.get(String.valueOf(agentpayPassage.getId()));
            if(agentAgentpayPassage == null) {
                agentAgentpayPassage = new AgentAgentpayPassage();
                agentAgentpayPassage.setAgentId(agentId);
                agentAgentpayPassage.setAgentpayPassageId(agentpayPassage.getId());
                agentAgentpayPassage.setStatus(MchConstant.PUB_NO);
            }
            JSONObject object = (JSONObject) JSON.toJSON(agentAgentpayPassage);
            object.put("passageName", agentpayPassage.getPassageName());
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        Integer agentpayPassageId = getIntegerRequired(param, "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) return ResponseEntity.ok(JeePayResponse.buildSuccess(null));
        AgentAgentpayPassage agentAgentpayPassage = rpcCommonService.rpcAgentAgentpayPassageService.findByAgentIdAndPassageId(agentId, agentpayPassageId);
        if(agentAgentpayPassage == null) {
            agentAgentpayPassage = new AgentAgentpayPassage();
            agentAgentpayPassage.setAgentId(agentId);
            agentAgentpayPassage.setAgentpayPassageId(agentpayPassageId);
            agentAgentpayPassage.setStatus(MchConstant.PUB_NO);
        }
        JSONObject object = (JSONObject) JSON.toJSON(agentAgentpayPassage);
        object.put("passageName", agentpayPassage.getPassageName());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "配置代理商代付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 将参数有字符元转为长整型分
        handleParamAmount(param, "feeEvery");
        AgentAgentpayPassage agentAgentpayPassage = getObject(param, AgentAgentpayPassage.class);
        // 修改
        int count;
        if(agentAgentpayPassage.getId() != null) {
            count = rpcCommonService.rpcAgentAgentpayPassageService.update(agentAgentpayPassage);
        }else {
            count = rpcCommonService.rpcAgentAgentpayPassageService.add(agentAgentpayPassage);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
