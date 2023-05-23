package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.jeepay.core.entity.PayPassage;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/04
 * @description: 支付通道账户
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_passage_account")
public class PayPassageAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassageAccount payPassageAccount = getObject(param, PayPassageAccount.class);
        int count = rpcCommonService.rpcPayPassageAccountService.count(payPassageAccount);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayPassageAccount> payPassageAccountList = rpcCommonService.rpcPayPassageAccountService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), payPassageAccount);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(PayPassageAccount info : payPassageAccountList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        PayPassageAccount payPassageAccount = rpcCommonService.rpcPayPassageAccountService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payPassageAccount));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道子账户" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassageAccount payPassageAccount = getObject(param, PayPassageAccount.class);

        if(payPassageAccount.getRiskMode() != null && payPassageAccount.getRiskMode() == 2) {
            Long maxDayAmount = payPassageAccount.getMaxDayAmount();
            Long maxEveryAmount = payPassageAccount.getMaxEveryAmount();
            Long minEveryAmount = payPassageAccount.getMinEveryAmount();
            // 元转分
            if(maxDayAmount != null) payPassageAccount.setMaxDayAmount(maxDayAmount * 100);
            if(maxEveryAmount != null) payPassageAccount.setMaxEveryAmount(maxEveryAmount * 100);
            if(minEveryAmount != null) payPassageAccount.setMinEveryAmount(minEveryAmount * 100);
        }

        int count = rpcCommonService.rpcPayPassageAccountService.update(payPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付通道子账户" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassageAccount payPassageAccount = getObject(param, PayPassageAccount.class);
        Integer payPassageId = getIntegerRequired(param, "payPassageId");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(payPassageId);
        if(payPassage == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST));
        }
        payPassageAccount.setIfCode(payPassage.getIfCode());            // 设置支付接口代码
        payPassageAccount.setIfTypeCode(payPassage.getIfTypeCode());    // 设置接口类型代码
        payPassageAccount.setPassageRate(payPassage.getPassageRate());  // 设置通道费率
        int count = rpcCommonService.rpcPayPassageAccountService.add(payPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
