package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.util.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.*;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 商户支付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_pay_passage")
public class MchPayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        // 得到代理商已经配置的支付通道
        List<AgentPassage> agentPassageList = new LinkedList<>();
        if(mchInfo.getAgentId() != null) {
            agentPassageList = rpcCommonService.rpcAgentPassageService.selectAllByAgentId(mchInfo.getAgentId());
        }
        Map<String, AgentPassage> agentPassageMap = new HashMap<>();
        for(AgentPassage agentPassage : agentPassageList) {
            agentPassageMap.put(String.valueOf(agentPassage.getProductId()), agentPassage);
        }

        // 得到商户已经配置的支付通道
        List<MchPayPassage> mchPayPassageList = rpcCommonService.rpcMchPayPassageService.selectAllByMchId(mchId);
        Map<String, MchPayPassage> mchPayPassageMap = new HashMap<>();
        for(MchPayPassage mchPayPassage : mchPayPassageList) {
            mchPayPassageMap.put(String.valueOf(mchPayPassage.getProductId()), mchPayPassage);
        }
        // 得到所有支付产品
        PayProduct queryPayProduct = new PayProduct();
        queryPayProduct.setStatus(MchConstant.PUB_YES);
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(queryPayProduct);
        /*Map<String, PayProduct> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product);
        }*/
        // 合并后返回的商户支付通道
        List<JSONObject> objects = new LinkedList<>();
        for(PayProduct payProduct : payProductList) {
            MchPayPassage mchPayPassage = mchPayPassageMap.get(String.valueOf(payProduct.getId()));
            //PayProduct payProduct = payProductMap.get(String.valueOf(mchPayPassage.getProductId()));
            if(mchPayPassage == null) {
                mchPayPassage = new MchPayPassage();
                mchPayPassage.setMchId(mchId);
                mchPayPassage.setProductId(payProduct.getId());
                mchPayPassage.setProductType(payProduct.getProductType());
                mchPayPassage.setMchRate(payProduct.getMchRate());
                mchPayPassage.setIfMode(payProduct.getIfMode() == null ? ((byte) 1) : payProduct.getIfMode());
                mchPayPassage.setStatus(MchConstant.PUB_NO);
            }
            JSONObject object = (JSONObject) JSON.toJSON(mchPayPassage);
            object.put("productName", payProduct.getProductName());
            if(agentPassageMap.get(String.valueOf(mchPayPassage.getProductId())) != null) {
                object.put("agentRate", agentPassageMap.get(String.valueOf(mchPayPassage.getProductId())).getAgentRate());
            }else if(payProduct.getAgentRate() != null) {
                object.put("agentRate", payProduct.getAgentRate());
            }
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
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        Integer productId = getIntegerRequired(param, "productId");
        AgentPassage agentPassage = null;
        if(mchInfo.getAgentId() != null) {
            agentPassage = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(mchInfo.getAgentId(), productId);
        }
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) return ResponseEntity.ok(JeePayResponse.buildSuccess(null));
        MchPayPassage mchPayPassage = rpcCommonService.rpcMchPayPassageService.findByMchIdAndProductId(mchId, productId);
        if(mchPayPassage == null) {
            mchPayPassage = new MchPayPassage();
            mchPayPassage.setMchId(mchId);
            mchPayPassage.setProductId(productId);
            mchPayPassage.setProductType(payProduct.getProductType());
            mchPayPassage.setMchRate(payProduct.getMchRate());
            mchPayPassage.setIfMode(payProduct.getIfMode() == null ? ((byte) 1) : payProduct.getIfMode());
            mchPayPassage.setPayPassageId(payProduct.getPayPassageId());
            mchPayPassage.setPayPassageAccountId(payProduct.getPayPassageAccountId());
            mchPayPassage.setPollParam(payProduct.getPollParam());
            mchPayPassage.setStatus(MchConstant.PUB_NO);
        }
        JSONObject object = (JSONObject) JSON.toJSON(mchPayPassage);
        object.put("productName", payProduct.getProductName());
        if(agentPassage != null) {
            object.put("agentRate", agentPassage.getAgentRate());
        }else if(payProduct.getAgentRate() != null) {
            object.put("agentRate", payProduct.getAgentRate());
        }else {
            object.put("agentRate", "");
        }
        object.put("agentId", mchInfo.getAgentId() == null ? "" : mchInfo.getAgentId());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "配置商户支付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchPayPassage mchPayPassage = getObject(param, MchPayPassage.class);
        int count;
        if(mchPayPassage.getId() != null) {
            // 修改
            if(mchPayPassage.getIfMode() == 1) { // 单独
                mchPayPassage.setPollParam(null);
                if(mchPayPassage.getPayPassageAccountId() == null) mchPayPassage.setPayPassageAccountId(0);
            }else if(mchPayPassage.getIfMode() == 2) { // 轮询
                mchPayPassage.setPayPassageId(null);
                mchPayPassage.setPayPassageAccountId(null);
            }
            count = rpcCommonService.rpcMchPayPassageService.update(mchPayPassage);
        }else {
            // 新增
            if(mchPayPassage.getIfMode() == 1) { // 单独
                mchPayPassage.setPollParam(null);
            }else if(mchPayPassage.getIfMode() == 2) { // 轮询
                mchPayPassage.setPayPassageId(null);
                mchPayPassage.setPayPassageAccountId(null);
            }
            count = rpcCommonService.rpcMchPayPassageService.add(mchPayPassage);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 轮询查询
     * @return
     */
    @RequestMapping("/poll_get")
    @ResponseBody
    public ResponseEntity<?> getPoll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        Integer productId = getIntegerRequired(param, "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));
        }
        MchPayPassage mchPayPassage = rpcCommonService.rpcMchPayPassageService.findByMchIdAndProductId(mchId, productId);
        String pollParam = "";
        if(mchPayPassage != null) {
            pollParam = mchPayPassage.getPollParam();
        }
        // 得到该类型下的支付通道
        String payType = payProduct.getPayType();
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.selectAllByPayType(payType);
        if(CollectionUtils.isEmpty(payPassageList)) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST));
        }
        // 得到已经配置的轮询参数,放入map中
        Map<String, JSONObject> pollMap = new HashMap<>();

        if(StringUtils.isNotBlank(pollParam)) {
            JSONArray array = JSONArray.parseArray(pollParam);
            if(CollectionUtils.isNotEmpty(array)) {
                for(int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String pid = object.getString("payPassageId");
                    pollMap.put(pid, object);
                }
            }
        }
        //
        List<JSONObject> objects = new LinkedList<>();
        for(PayPassage payPassage : payPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(payPassage);
            String payPassgeId = payPassage.getId().toString();

            JSONObject pollObj = pollMap.get(payPassgeId);
            if(pollObj == null) {
                object.put("LAY_CHECKED", false);
                object.put("weight", 1);
            }else {
                object.put("LAY_CHECKED", true);
                object.put("weight", pollObj.getIntValue("weight"));
            }
            objects.add(object);

        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}
