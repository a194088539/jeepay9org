package org.jeepay.mch.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentpayPassage;
import org.jeepay.core.entity.MchAgentpayPassage;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 商户代付通道
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_agentpay_passage")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getUser().getId();

        // 得到商户已经配置的代付通道
        List<MchAgentpayPassage> mchAgentpayPassageList = rpcCommonService.rpcMchAgentpayPassageService.selectAllByMchId(mchId);
        Map<String, MchAgentpayPassage> mchAgentpayPassageMap = new HashMap<>();
        for(MchAgentpayPassage mchAgentpayPassage : mchAgentpayPassageList) {
            mchAgentpayPassageMap.put(String.valueOf(mchAgentpayPassage.getAgentpayPassageId()), mchAgentpayPassage);
        }
        // 得到所有代付通道
        AgentpayPassage queryAgentpayPassage = new AgentpayPassage();
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAll(queryAgentpayPassage);
        Map<String, AgentpayPassage> agentpayPassageMap = new HashMap<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            agentpayPassageMap.put(String.valueOf(agentpayPassage.getId()), agentpayPassage);
        }
        // 合并后返回的商户代付通道
        List<JSONObject> objects = new LinkedList<>();
        for(MchAgentpayPassage mchAgentpayPassage : mchAgentpayPassageList) {
            AgentpayPassage ap = agentpayPassageMap.get(String.valueOf(mchAgentpayPassage.getAgentpayPassageId()));
            // 如果通道已关闭,则不显示
            if(ap.getStatus() != MchConstant.PUB_YES) continue;
            JSONObject object = (JSONObject) JSON.toJSON(mchAgentpayPassage);
            object.put("passageName", ap.getPassageName());
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}
