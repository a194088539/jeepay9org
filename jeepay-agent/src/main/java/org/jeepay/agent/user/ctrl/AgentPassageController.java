package org.jeepay.agent.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.core.entity.PayPassageAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.agent.common.ctrl.BaseController;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.AgentPassage;
import org.jeepay.core.entity.PayProduct;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/06
 * @description: 代理商支付通道配置
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/agent_passage")
public class AgentPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        Long agentId = getUser().getId();

        //若agentId不为空，则查询该agentId的支付通道，否则查询当前用户的通道
        String idstr = request.getParameter("agentId");
        if (StringUtils.isNotBlank(idstr)) {
            Long id = Long.valueOf(idstr);
            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(id);
            if (agentInfo != null){
                if (agentId.equals(agentInfo.getParentAgentId())) {
                    agentId = id;
                }
            }
        }

        // 得到代理商已经配置的支付通道
        List<AgentPassage> agentPassageList = rpcCommonService.rpcAgentPassageService.selectAllByAgentId(agentId);
        // 支付产品很多时,要考虑内存溢出问题
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll();
        Map<String, PayProduct> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product);
        }
        List<JSONObject> objects = new LinkedList<>();
        for(AgentPassage agentPassage : agentPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(agentPassage);
            if(payProductMap.get(String.valueOf(agentPassage.getProductId())) != null) {
                PayProduct product = payProductMap.get(String.valueOf(agentPassage.getProductId()));
                object.put("productName", product.getProductName());
                String fixedAmount = "";
                PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(product.getPayPassageAccountId());
                if(payPassageAccount == null || StringUtils.isBlank(payPassageAccount.getFixedAmount())){
                    PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(product.getPayPassageId());
                    if(payPassage != null && StringUtils.isNotBlank(payPassage.getFixedAmount())){
                        fixedAmount = payPassage.getFixedAmount();
                    }
                }else{
                    fixedAmount = payPassageAccount.getFixedAmount();
                }
                object.put("fixedAmount",fixedAmount);
            }
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}
