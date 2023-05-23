package org.jeepay.mch.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.core.entity.PayPassageAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.MchPayPassage;
import org.jeepay.core.entity.PayProduct;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/02/11
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_pay_passage")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchPayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        Long mchId = getUser().getId();

        // 支付产品很多时,要考虑内存溢出问题
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll();
        Map<String, PayProduct> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product);
        }

        // 得到商户配置的支付通道
        List<MchPayPassage> mchPayPassageList = rpcCommonService.rpcMchPayPassageService.selectAllByMchId(mchId);
        Map<String, MchPayPassage> mchPayPassageMap = new HashMap<>();
        for(MchPayPassage mchPayPassage : mchPayPassageList) {
            mchPayPassageMap.put(String.valueOf(mchPayPassage.getProductId()), mchPayPassage);
        }

        List<JSONObject> objects = new LinkedList<>();
        for(MchPayPassage mchPayPassage : mchPayPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(mchPayPassage);
            if(payProductMap.get(String.valueOf(mchPayPassage.getProductId())) != null) {
                object.put("productName", payProductMap.get(String.valueOf(mchPayPassage.getProductId())).getProductName());
            }
            String fixedAmount = "";
            PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(mchPayPassage.getPayPassageAccountId());
            if(payPassageAccount == null || StringUtils.isBlank(payPassageAccount.getFixedAmount())){
                PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(mchPayPassage.getPayPassageId());
                if(payPassage != null && StringUtils.isNotBlank(payPassage.getFixedAmount())){
                    fixedAmount = payPassage.getFixedAmount();
                }
            }else{
                fixedAmount = payPassageAccount.getFixedAmount();
            }
            object.put("fixedAmount",fixedAmount);
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}
