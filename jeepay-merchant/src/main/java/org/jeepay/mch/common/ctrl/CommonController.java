package org.jeepay.mch.common.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.PayProduct;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/17
 * @description: 通用配置
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/common")
public class CommonController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 所有支付产品列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_product_all")
    @ResponseBody
    public ResponseEntity<?> payProductAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayProduct payProduct = new PayProduct();
        Byte productType = getByte(param, "productType");
        if(productType != null) {
            payProduct.setProductType(productType);
        }
        payProduct.setStatus(MchConstant.PUB_YES);
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(payProduct);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payProductList));
    }

}
