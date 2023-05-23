package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.*;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: aragom
 * @date: 18/1/17
 * @description: 通用配置
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/common")
public class CommonConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 所有支付类型列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_type_all")
    @ResponseBody
    public ResponseEntity<?> payTypeaAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayType payType = getObject(param, PayType.class);
        List<PayType> payTypeList = rpcCommonService.rpcPayTypeService.selectAll(payType);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payTypeList));
    }

    /**
     * 所有支付产品列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_product_all")
    @ResponseBody
    public ResponseEntity<?> payProductAll(HttpServletRequest request) {
        PayProduct payProduct = new PayProduct();
        payProduct.setStatus(MchConstant.PUB_YES);
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(payProduct);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payProductList));
    }

    /**
     * 所有支付接口类型列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_interface_type_all")
    @ResponseBody
    public ResponseEntity<?> payInterfaceTypeAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayInterfaceType payInterfaceType = getObject(param, PayInterfaceType.class);
        List<PayInterfaceType> payInterfaceTypeList = rpcCommonService.rpcPayInterfaceTypeService.selectAll(payInterfaceType);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payInterfaceTypeList));
    }

    /**
     * 所有支付接口列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_interface_all")
    @ResponseBody
    public ResponseEntity<?> payInterfaceAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayInterface payInterface = getObject(param, PayInterface.class);
        List<PayInterface> payInterfaceList = rpcCommonService.rpcPayInterfaceService.selectAll(payInterface);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payInterfaceList));
    }

    /**
     * 所有支付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_all")
    @ResponseBody
    public ResponseEntity<?> payPassageAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassage payPassage = getObject(param, PayPassage.class);
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.selectAll(payPassage);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payPassageList));
    }

    /**
     * 所有代付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/agentpay_passage_all")
    @ResponseBody
    public ResponseEntity<?> agentpayPassageAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentpayPassage agentpayPassage = getObject(param, AgentpayPassage.class);
        agentpayPassage.setStatus(MchConstant.PUB_YES);
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAll(agentpayPassage);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentpayPassageList));
    }

    /**
     * 根据产品ID得到对应的支付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_product")
    @ResponseBody
    public ResponseEntity<?> payPassage4ProductId(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer productId = getInteger(param, "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));
        }
        String payType = payProduct.getPayType();
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.selectAllByPayType(payType);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payPassageList));
    }

    /**
     * 根据支付通道ID得到子账户列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_account")
    @ResponseBody
    public ResponseEntity<?> payPassageAccount4PassageId(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer payPassageId = getInteger(param, "payPassageId");
        List<PayPassageAccount> payPassageAccountList = new LinkedList<>();
        if(payPassageId != null) {
            payPassageAccountList = rpcCommonService.rpcPayPassageAccountService.selectAllByPassageId(payPassageId);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payPassageAccountList));
    }

    /**
     * 根据代付通道ID得到子账户列表
     * @param request
     * @return
     */
    @RequestMapping("/agentpay_passage_account")
    @ResponseBody
    public ResponseEntity<?> agentpayPassageAccount4PassageId(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer agentpayPassageId = getInteger(param, "agentpayPassageId");
        List<AgentpayPassageAccount> agentpayPassageAccountList = new LinkedList<>();
        if(agentpayPassageId != null) {
            agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.selectAllByPassageId(agentpayPassageId);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentpayPassageAccountList));
    }

}
