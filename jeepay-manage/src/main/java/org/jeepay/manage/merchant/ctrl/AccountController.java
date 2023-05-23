package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.MchAccountHistory;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/account")
public class AccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AccountController.class);

    /**
     * 查询账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchAccount mchAccount = rpcCommonService.rpcMchAccountService.findByMchId(mchId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchAccount));
    }

    /**
     * 商户账户加钱
     * @return
     */
    @RequestMapping("/credit")
    @ResponseBody
    @MethodLog( remark = "商户资金增加" )
    public ResponseEntity<?> credit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        String bizItem = getStringRequired(param, "bizItem");
        String amount = getStringRequired(param, "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getStringRequired(param, "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {
            rpcCommonService.rpcMchAccountService.credit2Account(mchId, MchConstant.BIZ_TYPE_CHANGE_BALANCE, amountL, bizItem);
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }catch (ServiceException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 商户账户减钱
     * @return
     */
    @RequestMapping("/debit")
    @ResponseBody
    @MethodLog( remark = "商户资金减少" )
    public ResponseEntity<?> debit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        String bizItem = getStringRequired(param, "bizItem");
        String amount = getStringRequired(param, "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getStringRequired(param, "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {
            rpcCommonService.rpcMchAccountService.debit2Account(mchId, MchConstant.BIZ_TYPE_CHANGE_BALANCE, amountL, bizItem);
            return ResponseEntity.ok(BizResponse.buildSuccess());
        }catch (ServiceException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 查询资金流水列表
     * @return
     */
    @RequestMapping("/history_list")
    @ResponseBody
    public ResponseEntity<?> historyList(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchAccountHistory mchAccountHistory = getObject(param, MchAccountHistory.class);
        int count = rpcCommonService.rpcMchAccountHistoryService.count(mchAccountHistory, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchAccountHistory> mchAccountHistoryList = rpcCommonService.rpcMchAccountHistoryService
                .select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), mchAccountHistory, getQueryObj(param));
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchAccountHistoryList, count));
    }

    /**
     * 查询资金流水
     * @return
     */
    @RequestMapping("/history_get")
    @ResponseBody
    public ResponseEntity<?> historyGet(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        MchAccountHistory mchAccountHistory = rpcCommonService.rpcMchAccountHistoryService
                .findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchAccountHistory));
    }

    /**
     * 统计总数
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String orderId = getString(param, "orderId");
        Long agentId = getLong(param, "agentId");
        Long mchId = getLong(param, "mchId");
        Byte bizType = getByte(param, "bizType");
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchAccountHistoryService.count4Data2(mchId, agentId, orderId, bizType, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("totalCount", allMap.get("totalCount"));                         // 总笔数
        obj.put("totalAmount", allMap.get("totalAmount"));                       //
        obj.put("totalFee", allMap.get("totalFee"));
        obj.put("totalAgentProfit", allMap.get("totalAgentProfit"));          //代理商利润    
        obj.put("totalPlatProfit", allMap.get("totalPlatProfit"));         //总平台利润
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

}
