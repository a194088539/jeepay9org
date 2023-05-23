package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: aragom
 * @date: 18/1/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH)
public class DataController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 统计用户数据
     * @return
     */
    @RequestMapping("/statistics/count4user")
    @ResponseBody
    public ResponseEntity<?> count4User(HttpServletRequest request) {
        // 商户数据
        Map mchObj = rpcCommonService.rpcMchInfoService.count4Mch();
        // 代理商数据
        Map agentObj = rpcCommonService.rpcAgentInfoService.count4Agent();

        JSONObject object = new JSONObject();
        object.put("mchObj", doMapEmpty(mchObj));
        object.put("agentObj", doMapEmpty(agentObj));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计收入数据
     * @return
     */
    @RequestMapping("/statistics/count4income")
    @ResponseBody
    public ResponseEntity<?> count4Income(HttpServletRequest request) {
        Map totalIncome = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, null, null);
        if(null == totalIncome.get("totalAgentProfit")) totalIncome.put("totalAgentProfit", 0);
        if(null == totalIncome.get("totalParentAgentProfit")) totalIncome.put("totalParentAgentProfit", 0);
        totalIncome.put("totalAgentProfit", Long.valueOf(String.valueOf((totalIncome.get("totalAgentProfit")))) +  Long.valueOf(String.valueOf((totalIncome.get("totalParentAgentProfit")))));
        JSONObject object = new JSONObject();
        object.put("totalIncome", doMapEmpty(totalIncome));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计今昨日数据
     * @return
     */
    @RequestMapping("/statistics/count4dayIncome")
    @ResponseBody
    public ResponseEntity<?> count4DayIncome(HttpServletRequest request) {
        // 今日
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayStart = today + " 00:00:00";
        String todayEnd = today + " 23:59:59";
        _log.info("今日统计数据时间：{} - {}", todayStart, todayEnd);
        _log.info("当前时间：{}", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
        // 昨日
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String yesterdayStart = yesterday + " 00:00:00";
        String yesterdayEnd = yesterday + " 23:59:59";
        // 今日收款统计
        Map todayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, todayStart, todayEnd);
        // 今日充值统计
        Map todayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_RECHARGE, todayStart, todayEnd);
        // 今日代付统计
        Map todayAgentpayData = rpcCommonService.rpcMchAgentpayService.count4All(null, null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, todayStart, todayEnd);
        // 昨日收款统计
        Map yesterdayPayData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_PAY, yesterdayStart, yesterdayEnd);
        // 昨日充值统计
        Map yesterdayRechargeData = rpcCommonService.rpcPayOrderService.count4Income(null, null, MchConstant.PRODUCT_TYPE_RECHARGE, yesterdayStart, yesterdayEnd);
        // 昨日代付统计
        Map yesterdayAgentpayData = rpcCommonService.rpcMchAgentpayService.count4All(null, null, null, null, PayConstant.AGENTPAY_STATUS_SUCCESS, null, yesterdayStart, yesterdayEnd);
        JSONObject object = new JSONObject();
        object.put("todayPayData", doMapEmpty(todayPayData));
        object.put("todayRechargeData", doMapEmpty(todayRechargeData));
        object.put("todayAgentpayData", doMapEmpty(todayAgentpayData));
        object.put("yesterdayPayData", doMapEmpty(yesterdayPayData));
        object.put("yesterdayRechargeData", doMapEmpty(yesterdayRechargeData));
        object.put("yesterdayAgentpayData", doMapEmpty(yesterdayAgentpayData));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }


    /**
     * 统计代理商数据
     * @return
     */
    @RequestMapping("/statistics/count4agent")
    @ResponseBody
    public ResponseEntity<?> count4Agent(HttpServletRequest request) {
        // 代理商数据
        Map agentObj = rpcCommonService.rpcAgentInfoService.count4Agent();
        // 代理商分润数据
        Map agentProfitObj = new JSONObject();
        Long rechargeProfit = 0l;
        Long totalProfit = 0l;
        Long payProfit = 0l;
        Long agentpayProfit = 0l;
        List<Map> mapList = rpcCommonService.rpcAgentAccountHistoryService.count4AgentProfit(null);
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
        object.put("agentObj", doMapEmpty(agentObj));
        object.put("agentProfitObj", doMapEmpty(agentProfitObj));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计商户数据
     * @return
     */
    @RequestMapping("/statistics/count4mch")
    @ResponseBody
    public ResponseEntity<?> count4Mch(HttpServletRequest request) {
        // 商户数据
        Map mchObj = rpcCommonService.rpcMchInfoService.count4Mch();
        JSONObject object = new JSONObject();
        object.put("mchObj", doMapEmpty(mchObj));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计平台数据
     * @return
     */
    @RequestMapping("/statistics/count4plat")
    @ResponseBody
    public ResponseEntity<?> count4Plat(HttpServletRequest request) {
        // 收款数据
        Map payDataObj = rpcCommonService.rpcMchAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_TRANSACT);
        // 充值数据
        Map rechargeDataObj = rpcCommonService.rpcMchAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_RECHARGE);
        // 代付数据
        Map agentpayDataObj = rpcCommonService.rpcMchAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_AGENTPAY);

        Long payPlatProfit = Long.parseLong(payDataObj.get("totalPlatProfit").toString());
        Long agentpayPlatProfit = Long.parseLong(agentpayDataObj.get("totalPlatProfit").toString());
        Long rechargePlatProfit = Long.parseLong(rechargeDataObj.get("totalPlatProfit").toString());
        Long totalPlatProfit = payPlatProfit + agentpayPlatProfit + rechargePlatProfit;

        JSONObject object = new JSONObject();
        object.put("payDataObj", doMapEmpty(payDataObj));
        object.put("rechargeDataObj", doMapEmpty(rechargeDataObj));
        object.put("agentpayDataObj", doMapEmpty(agentpayDataObj));
        object.put("totalPlatProfit", totalPlatProfit);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 统计代付数据
     * @return
     */
    @RequestMapping("/statistics/count4agentpay")
    @ResponseBody
    public ResponseEntity<?> count4Agentpay(HttpServletRequest request) {
        // 商户代付数据
        Map agentpayDataObj = rpcCommonService.rpcMchAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_AGENTPAY);
        // 商户充值数据
        JSONObject object = new JSONObject();
        Map rechargeDataObj = rpcCommonService.rpcMchAccountHistoryService.count4Data(MchConstant.BIZ_TYPE_RECHARGE);
        object.put("agentpayDataObj", doMapEmpty(agentpayDataObj));
        object.put("rechargeDataObj", doMapEmpty(rechargeDataObj));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 商户充值排行
     * @return
     */
    @RequestMapping("/data/count4MchTop")
    @ResponseBody
    public ResponseEntity<?> count4mchTop(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        Long agentId = getLong(param, "agentId");
        Long mchId = getLong(param, "mchId");
        Byte productType = getByte(param, "productType");
        // 商户充值排行
        List<Map> mchTopList = rpcCommonService.rpcPayOrderService.count4MchTop(agentId, mchId, productType, createTimeStart, createTimeEnd);
        List<Map> mchTopList2 = new LinkedList<>();
        for(Map map : mchTopList) {
            mchTopList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchTopList2));
    }

    /**
     * 代理商分润排行
     * @return
     */
    @RequestMapping("/data/count4AgentTop")
    @ResponseBody
    public ResponseEntity<?> count4AgentTop(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLong(param, "agentId");
        String bizType = getString(param, "bizType");
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        // 代理商分润排行
        List<Map> agentTopList = rpcCommonService.rpcMchAccountHistoryService.count4AgentTop(agentId, bizType,createTimeStart, createTimeEnd);
        List<Map> agentTopList2 = new LinkedList<>();
        for(Map map : agentTopList) {
            agentTopList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentTopList2));
    }

    /**
     * 支付产品统计
     * @return
     */
    @RequestMapping("/data/count4PayProduct")
    @ResponseBody
    public ResponseEntity<?> count4PayProduct(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        // 支付产品统计
        List<Map> payProductList = rpcCommonService.rpcPayOrderService.count4PayProduct(createTimeStart, createTimeEnd);
        List<Map> payProductList2 = payProductList.stream().map(this::doMapEmpty).collect(Collectors.toCollection(LinkedList::new));
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payProductList2));
    }

    @RequestMapping("/data/selectMchInfo")
    @ResponseBody
    public JeePayResponse selectMchInfo(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLong(param, "mchId");
        if (mchId != null) {
            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
            if (mchInfo == null) { return JeePayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST); }
        }
        return JeePayResponse.buildSuccess();
    }
    
    /**
     * 支付产品总金额
     *
     * @return
     */
    @RequestMapping("/data/count4PayProductAll")
    @ResponseBody
    public ResponseEntity<?> count4PayProductAll(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        // 支付产品总金额统计
        Long totalAmount  = rpcCommonService.rpcPayOrderService.count4PayProducts(createTimeStart, createTimeEnd);
        Map<String,Object> map=new HashMap<>();
        map.put("totalAmount",totalAmount);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(map));
    }

    /**
     * 按天成功率统计
     * @return
     */
    @RequestMapping("/data/selectSuccessRate")
    @ResponseBody
    public ResponseEntity<?> selectSuccessRate(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        Long mchId = getLong(param, "mchId");
        List<Map> successRateList = rpcCommonService.rpcPayOrderService.daySuccessRate((getPageIndex(param) -1) * getPageSize(param), getPageSize(param),createTimeStart,createTimeEnd,mchId);
        Map<String,Object> countMap = rpcCommonService.rpcPayOrderService.countDaySuccessRate(createTimeStart,createTimeEnd,mchId);
        int count = (int)(long)countMap.get("totalCount");
        List<Map> successRateList2 = new LinkedList<>();
        for(Map map : successRateList) {
            String successRate = ((BigDecimal) map.get("successRate")).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "%";
            String dateType = new StringBuilder((String) map.get("dateType")).insert(6,"-").insert(4,"-").toString();
            map.put("dateType",dateType);
            map.put("successRate",successRate);
            successRateList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(successRateList2,count));
    }

    /**
     * 按小时成功率统计
     * @return
     */
    @RequestMapping("/data/hourSuccessRate")
    @ResponseBody
    public ResponseEntity<?> hourSuccessRate(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String createTimeStart = getString(param, "createTimeStart");
        String createTimeEnd = getString(param, "createTimeEnd");
        Long mchId = getLong(param, "mchId");
        if(!timeDiff(createTimeStart,createTimeEnd)) {
            System.out.println(!timeDiff(createTimeStart,createTimeEnd));
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_TIME_DIFF));
        }
        List<Map> successRateList = rpcCommonService.rpcPayOrderService.hourSuccessRate((getPageIndex(param) -1) * getPageSize(param), getPageSize(param),createTimeStart,createTimeEnd,mchId);
        Map<String,Object> countMap = rpcCommonService.rpcPayOrderService.countHourSuccessRate(createTimeStart,createTimeEnd,mchId);
        int count = (int)(long)countMap.get("totalCount");
        List<Map> successRateList2 = new LinkedList<>();
        for(Map map : successRateList) {
            String successRate = ((BigDecimal) map.get("successRate")).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString() + "%";
            String dateType = map.get("dateType") + " 时";
            map.put("dateType",dateType);
            map.put("successRate",successRate);
            successRateList2.add(doMapEmpty(map));
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(successRateList2,count));
    }

    /**
     * 订单近七日成功率
     * @return
     */
    @RequestMapping("/data/dateRate")
    @ResponseBody
    public ResponseEntity<?> dateRate(HttpServletRequest request) {
        Map object = new LinkedHashMap();
        Map dateRate = new LinkedHashMap();
        for(int i = 0 ; i < 7 ; i++){
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-24*60*60*1000*i);
            String dayStart = day + " 00:00:00";
            String dayEnd = day + " 23:59:59";
            // 订单近七日成功率查询
            dateRate = rpcCommonService.rpcPayOrderService.dateRate(dayStart, dayEnd);
            if (dateRate != null) {
                BigDecimal successRate = ((BigDecimal) dateRate.get("successRate")).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                dateRate.put("successRate",successRate);
            }
            object.put(i,day);
            int y = i + 7;
            object.put(y, doMapEmpty(dateRate));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 订单近十二小时成功率
     * @return
     */
    @RequestMapping("/data/hourRate")
    @ResponseBody
    public ResponseEntity<?> hourRate(HttpServletRequest request) {
        Map object = new LinkedHashMap();
        Map hourRate = new LinkedHashMap();
        for(int i = 0 ; i < 12 ; i++){
            String day = new SimpleDateFormat("yyyy-MM-dd HH").format(new Date().getTime()-60*60*1000*i);
            String dayStart = day + ":00:00";
            String dayEnd = day + ":59:59";
            // 订单近十二小时成功率查询
            hourRate = rpcCommonService.rpcPayOrderService.hourRate(dayStart, dayEnd);
            if (hourRate != null) {
                BigDecimal successRate = ((BigDecimal) hourRate.get("successRate")).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                hourRate.put("successRate",successRate);
            }
            String day2 = new SimpleDateFormat("HH时").format(new Date().getTime()-60*60*1000*i);
            object.put(i,day2);
            int y = i + 12;
            object.put(y, doMapEmpty(hourRate));
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 近七日平台交易金额
     * @return
     */
    @RequestMapping("/statistics/orderDayAmount")
    @ResponseBody
    public ResponseEntity<?> orderDayAmount(HttpServletRequest request) {
        Map object = new LinkedHashMap();
        Map dayAmount = new LinkedHashMap();
        for(int i = 0 ; i < 7 ; i++){
            String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-24*60*60*1000*i);
            String dayStart = day + " 00:00:00";
            String dayEnd = day + " 23:59:59";
            // 每日交易金额查询
            dayAmount = rpcCommonService.rpcPayOrderService.orderDayAmount(null,dayStart, dayEnd);
            object.put(i,day);
            int y = i + 7;
            object.put(y, doMapEmpty(dayAmount));
        }
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
        if(null == map.get("payProfit")) map.put("payProfit", 0);
        if(null == map.get("agentpayProfit")) map.put("agentpayProfit", 0);
        return map;
    }

    private boolean timeDiff(String createTimeStart,String createTimeEnd){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeStart,new ParsePosition(0)));

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTimeEnd,new ParsePosition(0)));
        Long timeDiff = calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
        if (timeDiff > 24*60*60*1000){
            return false;
        }
        return true;
    }

}
