package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
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
import org.jeepay.core.entity.AgentPassage;
import org.jeepay.core.entity.MchPayPassage;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.core.entity.PayProduct;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/04
 * @description: 支付产品
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_product")
public class PayProductController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayProduct payProduct = getObject(param, PayProduct.class);
        int count = rpcCommonService.rpcPayProductService.count(payProduct);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), payProduct);
        // 支付类型Map
        Map payTypeMap = commonConfigService.getPayTypeMap();
        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(PayProduct info : payProductList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("payTypeName", payTypeMap.get(info.getPayType()));  // 转换支付类型名称
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payProduct));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付产品" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayProduct payProduct = getObject(param, PayProduct.class);

        if(payProduct.getIfMode() == 1) { // 单独
            payProduct.setPollParam(null);
            if(payProduct.getPayPassageAccountId() == null) payProduct.setPayPassageAccountId(0);
        }else if(payProduct.getIfMode() == 2) { // 轮询
            payProduct.setPayPassageId(null);
            payProduct.setPayPassageAccountId(null);
        }

        int count = rpcCommonService.rpcPayProductService.update(payProduct);
        if(count != 1) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        String updateMsg = "更新完成";
        String updateAgentRate = getString(param, "updateAgentRate");
        // 更新所有已配置该支付产品的代理商费率
        if("1".equals(updateAgentRate)) {
            AgentPassage updateAgentPassage = new AgentPassage();
            updateAgentPassage.setAgentRate(payProduct.getAgentRate()); // 代理商费率
            AgentPassage queryAgentPassage = new AgentPassage();
            queryAgentPassage.setProductId(payProduct.getId());
            int updateAgentCount = rpcCommonService.rpcAgentPassageService.update(updateAgentPassage, queryAgentPassage);
            updateMsg += "[更新代理:" + updateAgentCount + "个]";
        }

        String updateMchRate = getString(param, "updateMchRate");
        String updatePayPassage = getString(param, "updatePayPassage");
        MchPayPassage updateMchPayPassage = new MchPayPassage();

        if("1".equals(updateMchRate)) {
            updateMchPayPassage.setMchRate(payProduct.getMchRate());    // 商户费率
        }
        if("1".equals(updatePayPassage)) {
            updateMchPayPassage.setIfMode(payProduct.getIfMode());    // 商户费率
            if(payProduct.getIfMode() == 1) {   // 接口单独
                updateMchPayPassage.setPayPassageId(payProduct.getPayPassageId());  // 支付通道ID
                // 如果没有选择子账户ID,需要把已有的设置为0
                if(payProduct.getPayPassageAccountId() == null) {
                    updateMchPayPassage.setPayPassageAccountId(0);
                }else {
                    updateMchPayPassage.setPayPassageAccountId(payProduct.getPayPassageAccountId());
                }
            }else if(payProduct.getIfMode() == 2) { // 接口轮询
                updateMchPayPassage.setPollParam(payProduct.getPollParam());
            }
        }
        if("1".equals(updateMchRate) || "1".equals(updatePayPassage)) {
            MchPayPassage queryMchPayPassage = new MchPayPassage();
            queryMchPayPassage.setProductId(payProduct.getId());
            int updateMchCount = rpcCommonService.rpcMchPayPassageService.update(updateMchPayPassage, queryMchPayPassage);
            updateMsg += "[更新商户:" + updateMchCount + "个]";
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(updateMsg));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付产品" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayProduct payProduct = getObject(param, PayProduct.class);
        int count = rpcCommonService.rpcPayProductService.add(payProduct);
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

        Integer productId = getIntegerRequired(param, "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));
        }

        String pollParam = payProduct.getPollParam();
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
