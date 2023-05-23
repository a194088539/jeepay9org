package org.jeepay.mch.statistics.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author: aragom
 * @date: 18/1/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/data")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 统计数据
     * @return
     */
    @RequestMapping("/7day")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        Random random = new Random();
        JSONArray array = new JSONArray();
        for(int i=1; i<8; i++) {
            String day = "2018-01-0" + i;
            long value = random.nextInt(10) * i;
            JSONArray dArray = new JSONArray();
            dArray.add(day);
            dArray.add(value);
            array.add(dArray);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(array));
    }

    /**
     * 账户数据
     * @return
     */
    @RequestMapping("/count4Account")
    @ResponseBody
    public ResponseEntity<?> count4Account(HttpServletRequest request) {
        MchAccount mchAccount = rpcCommonService.rpcMchAccountService.findByMchId(getUser().getId());
        JSONObject object = (JSONObject) JSON.toJSON(mchAccount);
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getId(), MchConstant.PRODUCT_TYPE_PAY, null, null);
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/count4dayIncome")
    @ResponseBody
    public ResponseEntity<?> count4DayIncome(HttpServletRequest request) {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";
        // 昨日
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String yesterdayStart = yesterday + " 00:00:00";
        String yesterdayEnd = yesterday + " 23:59:59";
        // 今日收款统计
        Map todayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getId(), MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 今日充值统计
        Map todayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getId(), MchConstant.PRODUCT_TYPE_RECHARGE, todayStart, todayEnd);
        // 今日代付统计
        Map todayAgentpayData = rpcCommonService.rpcMchAgentpayService.count4All(getUser().getId(), null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, todayStart, todayEnd);
        // 昨日收款统计
        Map yesterdayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getId(), MchConstant.PRODUCT_TYPE_PAY, yesterdayStart, yesterdayEnd);
        // 昨日充值统计
        Map yesterdayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, getUser().getId(), MchConstant.PRODUCT_TYPE_RECHARGE, yesterdayStart, yesterdayEnd);
        // 昨日代付统计
        Map yesterdayAgentpayData = rpcCommonService.rpcMchAgentpayService.count4All(getUser().getId(), null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, yesterdayStart, yesterdayEnd);
        JSONObject object = new JSONObject();
        object.put("todayPayData", doMapEmpty(todayPayData));
        object.put("todayRechargeData", doMapEmpty(todayRechargeData));
        object.put("todayAgentpayData", doMapEmpty(todayAgentpayData));
        object.put("yesterdayPayData", doMapEmpty(yesterdayPayData));
        object.put("yesterdayRechargeData", doMapEmpty(yesterdayRechargeData));
        object.put("yesterdayAgentpayData", doMapEmpty(yesterdayAgentpayData));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));

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
