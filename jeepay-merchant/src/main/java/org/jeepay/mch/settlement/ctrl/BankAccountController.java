package org.jeepay.mch.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchBankAccount;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/7
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/bank_account")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class BankAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(BankAccountController.class);

    /**
     * 添加银行账户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "添加结算账户" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchBankAccount mchBankAccount = getObject(param, MchBankAccount.class);
        if(mchBankAccount != null) {
            mchBankAccount.setMchId(getUser().getId());
            mchBankAccount.setName(getUser().getName());
        }
        // 判断账号是否被使用
        String accountNo = mchBankAccount.getAccountNo();
        if(rpcCommonService.rpcMchBankAccountService.findByAccountNo(accountNo) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }
        // 如果是默认,先将其他都修改为非默认
        if(mchBankAccount.getIsDefault() == 1) {
            MchBankAccount updateMchBankAccount = new MchBankAccount();
            updateMchBankAccount.setMchId(getUser().getId());
            updateMchBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcMchBankAccountService.updateByMchId(updateMchBankAccount, getUser().getId());
        }
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
        if(mchBankAccount == null) mchBankAccount = new MchBankAccount();
        mchBankAccount.setMchId(getUser().getId());
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
    @MethodLog( remark = "修改结算账户" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchBankAccount mchBankAccount = getObject(param, MchBankAccount.class);
        if(mchBankAccount != null) mchBankAccount.setMchId(getUser().getId());
        // 判断账号是否被使用
        String accountNo = mchBankAccount.getAccountNo();
        MchBankAccount queryMchBankAccount = rpcCommonService.rpcMchBankAccountService.findById(mchBankAccount.getId());
        if(!queryMchBankAccount.getAccountNo().equals(accountNo) && rpcCommonService.rpcMchBankAccountService.findByAccountNo(accountNo) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_BANK_ACCOUNTNO_USED));
        }

        // 如果是默认,先将其他都修改为非默认
        if(mchBankAccount.getIsDefault() == 1) {
            MchBankAccount updateMchBankAccount = new MchBankAccount();
            updateMchBankAccount.setMchId(getUser().getId());
            updateMchBankAccount.setIsDefault(MchConstant.PUB_NO);
            rpcCommonService.rpcMchBankAccountService.updateByMchId(updateMchBankAccount, getUser().getId());
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
    @MethodLog( remark = "删除结算账户" )
    public ResponseEntity<?> delete(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        int count = rpcCommonService.rpcMchBankAccountService.delete(id);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        MchBankAccount mchBankAccount = new MchBankAccount();
        mchBankAccount.setMchId(getUser().getId());
        mchBankAccount.setId(id);
        mchBankAccount = rpcCommonService.rpcMchBankAccountService.find(mchBankAccount);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchBankAccount));
    }

    /**
     * 查询默认账号
     * @return
     */
    @RequestMapping("/default_get")
    @ResponseBody
    public ResponseEntity<?> defaultAccount(HttpServletRequest request) {
        MchBankAccount mchBankAccount = new MchBankAccount();
        mchBankAccount.setMchId(getUser().getId());
        mchBankAccount.setIsDefault(MchConstant.PUB_YES);
        mchBankAccount = rpcCommonService.rpcMchBankAccountService.find(mchBankAccount);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchBankAccount));
    }

    /**
     * 设置默认账号
     * @return
     */
    @RequestMapping("/default_set")
    @ResponseBody
    @MethodLog( remark = "设置默认结算账户" )
    public ResponseEntity<?> setDefault(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        // 设置所有状态为非默认
        MchBankAccount mchBankAccount = new MchBankAccount();
        mchBankAccount.setMchId(getUser().getId());
        mchBankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcMchBankAccountService.update(mchBankAccount);
        _log.info("set all bankAccount default is no, id={}, result={}", id, count);
        // 设置当前为默认
        MchBankAccount mchBankAccount2 = new MchBankAccount();
        mchBankAccount2.setId(id);
        mchBankAccount2.setIsDefault(MchConstant.PUB_YES);
        count = rpcCommonService.rpcMchBankAccountService.update(mchBankAccount2);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 取消默认账号
     * @return
     */
    @RequestMapping("/default_cancel")
    @ResponseBody
    @MethodLog( remark = "取消默认结算账户" )
    public ResponseEntity<?> cancelDefault(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        // 设置当前为非默认
        MchBankAccount mchBankAccount = new MchBankAccount();
        mchBankAccount.setId(id);
        mchBankAccount.setIsDefault(MchConstant.PUB_NO);
        int count = rpcCommonService.rpcMchBankAccountService.update(mchBankAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
