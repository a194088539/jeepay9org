package org.jeepay.agent.statistics.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.agent.common.ctrl.BaseController;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.MchInfo;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: aragom
 * @date: 18/1/17
 * @description:
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/data")
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 账户数据
     * @return
     */
    @RequestMapping("/count4Account")
    @ResponseBody
    public ResponseEntity<?> count4Account(HttpServletRequest request) {
        AgentAccount agentAccount = rpcCommonService.rpcAgentAccountService.findByAgentId(getUser().getId());
        JSONObject object = (JSONObject) JSON.toJSON(agentAccount);
        // 下级商户数量
        MchInfo queryMchInfo = new MchInfo();
        queryMchInfo.setStatus((byte) 1);
        queryMchInfo.setAgentId(getUser().getId());
        int mchCount = rpcCommonService.rpcMchInfoService.count(queryMchInfo);
        object.put("mchCount", mchCount);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/count4Income")
    @ResponseBody
    public ResponseEntity<?> count4Income(HttpServletRequest request) {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";

        // 今日收入情况
        Map todayIncome = rpcCommonService.rpcPayOrderService.count4Income(getUser().getId(), null, MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 昨日收入情况
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(getUser().getId(), null, MchConstant.PRODUCT_TYPE_PAY, null, null);
        JSONObject object = new JSONObject();
        object.put("todayIncome", doMapEmpty(todayIncome));
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计代理商数据
     * @return
     */
    @RequestMapping("/count4agent")
    @ResponseBody
    public ResponseEntity<?> count4Agent(HttpServletRequest request) {
        // 代理商分润数据
        Map agentProfitObj = new JSONObject();
        Long payProfit = 0l;
        Long agentpayProfit = 0l;
        Long rechargeProfit = 0l;
        Long totalProfit = 0l;
        List<Map> mapList = rpcCommonService.rpcAgentAccountHistoryService.count4AgentProfit(getUser().getId());
        for(Map map : mapList) {
            String bizItem = map.get("bizItem").toString();
            Long profilt = Long.parseLong(map.get("totalProfit").toString());
            switch (bizItem) {
                case MchConstant.BIZ_ITEM_PAY:
                    totalProfit += profilt;
                    payProfit += profilt;
                    break;
                case MchConstant.BIZ_ITEM_AGENTPAY:
                    totalProfit += profilt;
                    agentpayProfit += profilt;
                    break;
                case MchConstant.BIZ_ITEM_OFF:
                    totalProfit += profilt;
                    rechargeProfit += profilt;
                    break;
                case MchConstant.BIZ_ITEM_ONLINE:
                    totalProfit += profilt;
                    rechargeProfit += profilt;
                    break;
            }
        }
        agentProfitObj.put("agentpayProfit", agentpayProfit);
        agentProfitObj.put("payProfit", payProfit);
        agentProfitObj.put("rechargeProfit", rechargeProfit);
        agentProfitObj.put("totalProfit", totalProfit);
        JSONObject object = new JSONObject();
        object.put("agentProfitObj", doMapEmpty(agentProfitObj));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 商户充值排行
     * @return
     */
    @RequestMapping("/count4MchTop")
    @ResponseBody
    public ResponseEntity<?> count4mchTop(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        Long mchId = getLong(param, "mchId");
        Byte productType = getByte(param, "productType");
        // 商户充值排行
        List<Map> mchTopList = rpcCommonService.rpcPayOrderService.count4MchTop(getUser().getId(), mchId, productType, createTimeStart, createTimeEnd);
        List<Map> mchTopList2 = mchTopList.stream().map(this::doMapEmpty).collect(Collectors.toCollection(LinkedList::new));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchTopList2));
    }

    private Map doMapEmpty(Map map) {
        if(map == null) return map;
        if(null == map.get("totalCount")) map.put("totalCount", 0);
        if(null == map.get("totalAmount")) map.put("totalAmount", 0);
        if(null == map.get("totalMchIncome")) map.put("totalMchIncome", 0);
        if(null == map.get("totalAgentProfit")) map.put("totalAgentProfit", 0);
        if(null == map.get("totalPlatProfit")) map.put("totalPlatProfit", 0);
        if(null == map.get("totalChannelCost")) map.put("totalChannelCost", 0);
        if(null == map.get("totalBalance")) map.put("totalBalance", 0);
        if(null == map.get("totalSettAmount")) map.put("totalSettAmount", 0);
        return map;
    }

}
