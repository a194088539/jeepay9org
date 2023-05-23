package org.jeepay.manage.settlement.ctrl;

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
import org.jeepay.core.entity.MchBankAccount;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/7
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bank_account")
public class BankAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 添加银行账户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增结算账号" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchBankAccount mchBankAccount = getObject(param, MchBankAccount.class);
        int count = rpcCommonService.rpcMchBankAccountService.add(mchBankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchBankAccount mchBankAccount = getObject(param, MchBankAccount.class);
        int count = rpcCommonService.rpcMchBankAccountService.count(mchBankAccount);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchBankAccount> mchAccountHistoryList = rpcCommonService.rpcMchBankAccountService
                .select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), mchBankAccount);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchAccountHistoryList, count));
    }

    /**
     * 修改银行账户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改结算账号" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchBankAccount mchBankAccount = getObject(param, MchBankAccount.class);
        // 判断账号是否被使用
        String accountNo = mchBankAccount.getAccountNo();
        if(rpcCommonService.rpcMchBankAccountService.findByAccountNo(accountNo) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }
        int count = rpcCommonService.rpcMchBankAccountService.update(mchBankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 删除银行账户信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除结算账号" )
    public ResponseEntity<?> delete(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        int count = rpcCommonService.rpcMchBankAccountService.delete(id);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询银行账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        MchBankAccount mchBankAccount = rpcCommonService.rpcMchBankAccountService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchBankAccount));
    }

}
