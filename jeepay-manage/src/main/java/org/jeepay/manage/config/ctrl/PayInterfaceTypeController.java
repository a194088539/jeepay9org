package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
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
import org.jeepay.core.entity.PayInterfaceType;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/05/03
 * @description: 支付接口类型
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_interface_type")
public class PayInterfaceTypeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayInterfaceType payInterfaceType = getObject(param, PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.count(payInterfaceType);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayInterfaceType> payInterfaceTypeList = rpcCommonService.rpcPayInterfaceTypeService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), payInterfaceType);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(payInterfaceTypeList, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String ifTypeCode = getStringRequired(param, "ifTypeCode");
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payInterfaceType));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付接口类型" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayInterfaceType payInterfaceType = getObject(param, PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.update(payInterfaceType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付接口类型" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayInterfaceType payInterfaceType = getObject(param, PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.add(payInterfaceType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }



}
