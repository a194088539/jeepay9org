package org.jeepay.core.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.PayEnum;
import org.jeepay.core.common.vo.OrderCostFeeVO;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 支付工具类
 * @author aragom qq194088539
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.jeepay.org
 */
public class JEEPayUtil {

    private static final MyLog _log = MyLog.getLog(JEEPayUtil.class);

    public static final BigDecimal MIN_SERVICE_CHARGE = new BigDecimal(400); //最低手续费
    public static final BigDecimal AMOUNT_INCREASE_BASENUM = new BigDecimal(1000400); //手续费倍增基数

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, String errCode, String errDesc) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(errCode != null) retMap.put(PayConstant.RESULT_PARAM_ERRCODE, errCode);
        if(errDesc != null) retMap.put(PayConstant.RESULT_PARAM_ERRDES, errDesc);
        return retMap;
    }

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, PayEnum payEnum) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(payEnum != null) {
            retMap.put(PayConstant.RESULT_PARAM_ERRCODE, payEnum.getCode());
            retMap.put(PayConstant.RESULT_PARAM_ERRDES, payEnum.getMessage());
        }
        return retMap;
    }

    public static String makeRetData(Map retMap, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retMap.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retMap, resKey);
            retMap.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }
        _log.info("生成响应数据:{}", retMap);
        return JSON.toJSONString(retMap);
    }

    public static String makeRetData(JSONObject retObj, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retObj.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retObj, resKey);
            retObj.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }

        _log.info("生成响应数据:{}", retObj);
        return JSON.toJSONString(retObj);
    }

    public static String makeRetFail(Map retMap) {
        _log.info("生成响应数据:{}", retMap);
        return JSON.toJSONString(retMap);
    }

    /**
     * 验证支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    /**
     * 验证VV平台支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key, String... noSigns) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        if(noSigns != null && noSigns.length > 0) {
            for (String noSign : noSigns) {
                params.remove(noSign);
            }
        }
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    public static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=");
            if(paraMap.get(key) instanceof String) {
                urlParam.append(URLEncoder.encode((String) paraMap.get(key)));
            }else {
                urlParam.append(paraMap.get(key));
            }
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

    public static String genUrlParams2(Map<String, String> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=").append(paraMap.get(key));
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

    /**
     * 发起HTTP/HTTPS请求(method=POST)
     * @param url
     * @return
     */
    public static String call4Post(String url) {
        try {
            URL url1 = new URL(url);
            if("https".equals(url1.getProtocol())) {
                return HttpClient.callHttpsPost(url);
            }else if("http".equals(url1.getProtocol())) {
                return HttpClient.callHttpPost(url);
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断返回结果是否成功
     * @param retMap
     * @return
     */
    public static Boolean isSuccess(Map retMap) {
        if(retMap == null) return false;
        if(retMap.get("retCode") == null) return false;
        return "SUCCESS".equalsIgnoreCase(retMap.get("retCode").toString());
    }

    /**
     * 根据费率类型和金额,返回费用(type )
     * @param type      费用类型 1:费率,2:固定金额
     * @param feeRate   费率
     * @param feeEvery  每笔金额
     * @param amount    需要算的金额
     * @return
     */
    public static Long getFee(Byte type, BigDecimal feeRate, Long feeEvery, Long amount) {
        Long fee = 0l;
        if(type == 1) {
            fee = JEEPayUtil.calOrderMultiplyRate(amount, feeRate);
        }else if(type == 2) {
            fee = feeEvery;
        }
        return fee;
    }

    /**
     * 判断IP是否允许
     * @param ip
     * @param whiteIps
     * @param blackIps
     * @return
     */
    public static Boolean ipAllow(String ip, String whiteIps, String blackIps) {
        if(StringUtils.isBlank(ip)) {
            return true;
        }
        String[] whiteIp_s = {};
        if(StringUtils.isNotBlank(whiteIps)) {
            whiteIp_s = whiteIps.split(",");
        }
        String[] blackIp_s = {};
        if(StringUtils.isNotBlank(blackIps)) {
            blackIp_s = blackIps.split(",");
        }
        // 白名单为空,黑名单为空
        if(whiteIp_s.length == 0 && blackIp_s.length == 0) {
            return true;
        }
        // 白名单为空,黑名单不为空
        if(whiteIp_s.length == 0 && blackIp_s.length > 0) {
            return !contain(blackIp_s, ip);
        }
        // 白名单不为空,黑名单为空
        if(whiteIp_s.length > 0 && blackIp_s.length == 0) {
            return contain(whiteIp_s, ip);
        }
        // 白名单不为空,黑名单不为空
        if(whiteIp_s.length > 0 && blackIp_s.length > 0) {
            if(contain(blackIp_s, ip)) {    // 如果在黑名单,则返回false
                return false;
            }
            return contain(whiteIp_s, ip);
        }
        return false;
    }

    /**
     * 判断IP是否允许(强校验)
     * 1. 必须在白名单中
     * 2. 如果在黑名单,则白名单中失效
     * @param ip
     * @param whiteIps
     * @param blackIps
     * @return
     */
    public static Boolean ipAllow4Strong(String ip, String whiteIps, String blackIps) {
        // 没有IP则返回false
        if(StringUtils.isBlank(ip)) {
            return false;
        }
        String[] whiteIp_s = {};
        if(StringUtils.isNotBlank(whiteIps)) {
            whiteIp_s = whiteIps.split(",");
        }
        String[] blackIp_s = {};
        if(StringUtils.isNotBlank(blackIps)) {
            blackIp_s = blackIps.split(",");
        }
        // 白名单为空,返回false
        if(whiteIp_s.length == 0) {
            return false;
        }
        // 如果不在白名单,返回false
        if(!contain(whiteIp_s, ip)) {
            return false;
        }
        // 如果黑名单不为空,则判断是否在黑名单中
        if(blackIp_s.length > 0) {
            return !contain(blackIp_s, ip);
        }
        return true;
    }

    /**
     * 判断是否包含IP
     * @param ips   ip数组
     * @param ip    ip地址
     * @return
     */
    public static boolean contain(String[] ips, String ip) {
        if(ips == null || ips.length == 0) return false;
        if(StringUtils.isBlank(ip)) return false;
        for(String p : ips) {
            if(p.equals(ip)) return true;
        }
        return false;
    }

    /**
     * 判断是否包含IP
     * @param ips   使用半角逗号分隔的ip
     * @param ip    ip地址
     * @return
     */
    public static boolean contain(String ips, String ip) {
        if(StringUtils.isBlank(ips)) return false;
        String[] ip_s = {};
        if(StringUtils.isNotBlank(ips)) {
            ip_s = ips.split(",");
        }
        return contain(ip_s, ip);
    }

    /**
     * <p><b>Description: </b>计算订单的分润情况 和 各种费用
     * <p>2018年9月20日 下午4:13:47
     * @author matf
     * @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param channelRate 通道费率   （保持与数据库的格式一致 ，百分比之前的数字，如费率为0.55%，则传入 0.55）
     * @param agentRate 代理商设置费率，说明同上，  如果为null  说明商家没有代理商
     * @param parentAgentRate 一级代理商设置费率，说明同上
     * @param mchRate 商家设置费率，说明同上
     * @return
     */
    public static OrderCostFeeVO calOrderCostFeeAndIncome(Long amount, BigDecimal channelRate, BigDecimal agentRate, BigDecimal parentAgentRate, BigDecimal mchRate){
        //通道手续费
        Long channelCostFee = calOrderMultiplyRate(amount, channelRate);

        //一级代理商成本费用   即  ： 平台需要支付给一级代理商的费用
        Long parentAgentCostFee = 0L; //当该二级代理不存在一级代理商时 一级代理商费用为0
        if(parentAgentRate != null){
            parentAgentCostFee = calOrderMultiplyRate(amount, parentAgentRate);
        }

        //二级代理商成本费用   即  ：平台需要支付给二级代理商的费用
        Long agentCostFee = 0L; //当该二级代理不存在一级代理商时 二级代理商费用为0
        if(agentRate != null){
            agentCostFee = calOrderMultiplyRate(amount, agentRate);
        }

        //商家成本费用  即 ： 商家需要支付代理商或者平台的费用
        Long mchCostFee = calOrderMultiplyRate(amount, mchRate);

        //商户入账金额
        Long mchIncome = amount - mchCostFee;
        // 平台利润 商家成本费用 - 一级代理商费用 - 二级代理商费用 - 通道费用
        Long platProfit = mchCostFee - parentAgentCostFee - agentCostFee - channelCostFee;

        return new OrderCostFeeVO(channelCostFee, agentCostFee, parentAgentCostFee, mchCostFee, platProfit, agentCostFee, parentAgentCostFee, mchIncome);
    }

    // 原系统的记润算法，不适用
    public static OrderCostFeeVO calOrderCostFeeAndIncomeold(Long amount, BigDecimal channelRate, BigDecimal agentRate, BigDecimal parentAgentRate, BigDecimal mchRate){

        //通道手续费
        Long channelCostFee = calOrderMultiplyRate(amount, channelRate);

        //一级代理商成本费用   即  ：一级代理商需要支付给平台的费用
        Long parentAgentCostFee = 0L; //当该二级代理不存在一级代理商时 一级代理商费用为0
        if(parentAgentRate != null){
            parentAgentCostFee = calOrderMultiplyRate(amount, parentAgentRate);
        }

        //二级代理商成本费用   即  ：二级代理商需要支付给平台或一级代理的费用
        Long agentCostFee = 0L; //当该二级代理不存在一级代理商时 二级代理商费用为0
        if(agentRate != null){
            agentCostFee = calOrderMultiplyRate(amount, agentRate);
        }

        //商家成本费用  即 ： 商家需要支付代理商或者平台的费用
        Long mchCostFee = calOrderMultiplyRate(amount, mchRate);

        //平台利润  : （一级代理商费用 - 通道费用）  或者  （二级代理商费用 - 通道费用）  或者  （商家费用 - 通道费用）
        Long platProfit;
        if (parentAgentRate != null) {
            platProfit = parentAgentCostFee - channelCostFee;
        }else {
            platProfit = (agentRate != null ? agentCostFee : mchCostFee) - channelCostFee;
        }

        //一级代理商利润 ： (二级代理商费用 - 一级代理商费用) 或者  0
        Long parentAgentProfit = parentAgentRate != null ? agentCostFee - parentAgentCostFee  : 0L;

        //二级代理商利润 ： (商家费用 - 二级代理商费用) 或者  0
        Long agentProfit = agentRate != null ? mchCostFee - agentCostFee  : 0L;

        //商户入账金额
        Long mchIncome = amount - mchCostFee;

        //计算结果不允许出现负值
        if(agentProfit < 0) {
            _log.warn("[代理商&商户]费率设置异常:agentProfit={}, amount={}, channelRate={}, agentRate={}, parentAgentRate={}, mchRate={}", agentProfit, amount, channelRate, agentRate, parentAgentRate, mchRate);
            agentProfit = 0L;
        }

        if(parentAgentProfit < 0) {
            _log.warn("[一级代理&二级代理]费率设置异常:agentProfit={}, amount={}, channelRate={}, agentRate={}, parentAgentRate={}, mchRate={}", agentProfit, amount, channelRate, agentRate, parentAgentRate, mchRate);
            parentAgentProfit = 0L;
        }

        if(platProfit < 0) {
            _log.warn("[代理商&通道]费率设置异常:platProfit={}, amount={}, channelRate={}, agentRate={}, parentAgentRate={}, mchRate={}", platProfit, amount, channelRate, agentRate, parentAgentRate, mchRate);
            platProfit = 0L;
        }

        return new OrderCostFeeVO(channelCostFee, agentCostFee, parentAgentCostFee, mchCostFee, platProfit, agentProfit, parentAgentProfit, mchIncome);

    }

    /**
     * <p><b>Description: </b>计算订单的各种费用  （订单金额 * 费率  结果四舍五入并保留0位小数 ）
     * 适用于计算
     * <p>2018年9月20日 下午2:16:34
     * @author matf
     * @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param rate 费率   （保持与数据库的格式一致 ，百分比之前的数字，如费率为0.55%，则传入 0.55）
     * @return
     */
    public static Long calOrderMultiplyRate(Long amount, BigDecimal rate){
        //费率还原 回真实数值即/100, 并乘以订单金额   结果四舍五入并保留0位小数
        return new BigDecimal(amount).multiply(rate).divide(BigDecimal.valueOf(100), 0, BigDecimal.ROUND_HALF_UP).longValue();

    }

}
