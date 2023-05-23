package org.jeepay.manage.agent.ctrl;

import com.alibaba.fastjson.JSONObject;
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
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.AgentAccountHistory;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_account")
public class AgentAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AgentAccountController.class);

    /**
     * 查询账户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        AgentAccount agentAccount = rpcCommonService.rpcAgentAccountService.findByAgentId(agentId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentAccount));
    }

    /**
     * 代理商账户加钱
     * @return
     */
    @RequestMapping("/credit")
    @ResponseBody
    @MethodLog( remark = "代理商资金增加" )
    public ResponseEntity<?> credit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        String amount = getStringRequired(param, "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getStringRequired(param, "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {
            rpcCommonService.rpcAgentAccountService.credit2Account(agentId, MchConstant.AGENT_BIZ_TYPE_CHANGE_BALANCE, amountL, MchConstant.BIZ_ITEM_BALANCE);
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
     * 代理商账户减钱
     * @return
     */
    @RequestMapping("/debit")
    @ResponseBody
    @MethodLog( remark = "代理商资金减少" )
    public ResponseEntity<?> debit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        String amount = getStringRequired(param, "amount");         // 变更金额,前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amount.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        // 判断输入的超级密码是否正确
        String password = getStringRequired(param, "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        try {
            rpcCommonService.rpcAgentAccountService.debit2Account(agentId, MchConstant.AGENT_BIZ_TYPE_CHANGE_BALANCE, amountL, MchConstant.BIZ_ITEM_BALANCE);
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
        AgentAccountHistory agentAccountHistory = getObject(param, AgentAccountHistory.class);
        int count = rpcCommonService.rpcAgentAccountHistoryService.count(agentAccountHistory);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<AgentAccountHistory> agentAccountHistoryList = rpcCommonService.rpcAgentAccountHistoryService
                .select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), agentAccountHistory);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(agentAccountHistoryList, count));
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
        AgentAccountHistory agentAccountHistory = rpcCommonService.rpcAgentAccountHistoryService
                .findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentAccountHistory));
    }

}
