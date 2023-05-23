package org.jeepay.agent.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.agent.common.ctrl.BaseController;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentAgentpayPassage;
import org.jeepay.core.entity.AgentpayPassage;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/09/13
 * @description: 代理商代付通道配置
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/agent_agentpay_passage")
public class AgentAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        Long agentId = getUser().getId();
        // 得到代理商已经配置的代付通道
        List<AgentAgentpayPassage> agentAgentpayPassageList = rpcCommonService.rpcAgentAgentpayPassageService.selectAllByAgentId(agentId);
        // 得到所有代付通道
        AgentpayPassage queryAgentpayPassage = new AgentpayPassage();
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAll(queryAgentpayPassage);
        Map<String, AgentpayPassage> agentpayPassageMap = new HashMap<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            agentpayPassageMap.put(String.valueOf(agentpayPassage.getId()), agentpayPassage);
        }
        List<JSONObject> objects = new LinkedList<>();
        for(AgentAgentpayPassage agentAgentpayPassage : agentAgentpayPassageList) {
            AgentpayPassage ap = agentpayPassageMap.get(String.valueOf(agentAgentpayPassage.getAgentpayPassageId()));
            // 如果通道已关闭,则不显示
            if(ap.getStatus() != MchConstant.PUB_YES) continue;
            JSONObject object = (JSONObject) JSON.toJSON(agentAgentpayPassage);
            object.put("passageName", ap.getPassageName());
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}
