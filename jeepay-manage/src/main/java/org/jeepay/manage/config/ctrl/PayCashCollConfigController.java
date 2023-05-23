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
import org.jeepay.core.entity.PayCashCollConfig;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_cash_coll")
public class PayCashCollConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayCashCollConfig payProduct = getObject(param, PayCashCollConfig.class);
        int count = rpcCommonService.rpcPayCashCollConfigService.count(payProduct);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayCashCollConfig> payProductList = rpcCommonService.rpcPayCashCollConfigService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), payProduct);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(payProductList, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        PayCashCollConfig payProduct = rpcCommonService.rpcPayCashCollConfigService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payProduct));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改资金归集账号" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayCashCollConfig payProduct = getObject(param, PayCashCollConfig.class);
        int count = rpcCommonService.rpcPayCashCollConfigService.update(payProduct);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增资金归集账号" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayCashCollConfig payProduct = getObject(param, PayCashCollConfig.class);
        int count = rpcCommonService.rpcPayCashCollConfigService.add(payProduct);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
