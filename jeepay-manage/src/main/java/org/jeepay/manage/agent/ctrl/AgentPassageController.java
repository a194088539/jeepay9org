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
import org.jeepay.core.entity.AgentPassage;
import org.jeepay.core.entity.PayProduct;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/06
 * @description: 代理商通道配置
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_passage")
public class AgentPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");

        // 得到代理商已经配置的支付通道
        List<AgentPassage> agentPassageList = rpcCommonService.rpcAgentPassageService.selectAllByAgentId(agentId);
        Map<String, AgentPassage> agentPassageMap = new HashMap<>();
        for(AgentPassage agentPassage : agentPassageList) {
            agentPassageMap.put(String.valueOf(agentPassage.getProductId()), agentPassage);
        }
        // 得到所有支付产品
        PayProduct queryPayProduct = new PayProduct();
        queryPayProduct.setStatus(MchConstant.PUB_YES);
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(queryPayProduct);
        /*Map<String, PayProduct> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product);
        }*/
        // 合并后返回的代理商支付通道
        List<JSONObject> objects = new LinkedList<>();
        for(PayProduct product : payProductList) {
            AgentPassage agentPassage = agentPassageMap.get(String.valueOf(product.getId()));
            if(agentPassage == null) {
                agentPassage = new AgentPassage();
                agentPassage.setAgentId(agentId);
                agentPassage.setProductId(product.getId());
                agentPassage.setAgentRate(product.getAgentRate());
                agentPassage.setStatus(MchConstant.PUB_NO);
            }
            JSONObject object = (JSONObject) JSON.toJSON(agentPassage);
            object.put("productName", product.getProductName());
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
        Integer productId = getIntegerRequired(param, "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) return ResponseEntity.ok(JeePayResponse.buildSuccess(null));
        AgentPassage agentPassage = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(agentId, productId);
        if(agentPassage == null) {
            agentPassage = new AgentPassage();
            agentPassage.setAgentId(agentId);
            agentPassage.setProductId(productId);
            agentPassage.setAgentRate(payProduct.getAgentRate());
            agentPassage.setStatus(MchConstant.PUB_NO);
        }
        JSONObject object = (JSONObject) JSON.toJSON(agentPassage);
        object.put("productName", payProduct.getProductName());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "配置代理商支付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentPassage agentPassage = getObject(param, AgentPassage.class);
        Long parentAgentId = rpcCommonService.rpcAgentInfoService.findByAgentId(agentPassage.getAgentId()).getParentAgentId();
        if (parentAgentId != null && parentAgentId!=0){
            AgentPassage ap = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(parentAgentId,agentPassage.getProductId());
            BigDecimal parentAgentRate = null;
            if (ap != null){
                parentAgentRate = ap.getAgentRate();
            }
            if (parentAgentRate == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_PARENTAGENTRATE_NOT_EXIST));
            if (parentAgentRate.compareTo(agentPassage.getAgentRate()) == 1){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_AGENTRATE_SET_MIRROR));
            }
        }
        int count;
        if(agentPassage.getId() != null) {
            count = rpcCommonService.rpcAgentPassageService.update(agentPassage);
        }else {
            count = rpcCommonService.rpcAgentPassageService.add(agentPassage);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
