package org.jeepay.pay.channel.tcpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: aragom
 * @date: 19/01/20
 * @description: 转卡通道回调
 */
@Service
public class TcpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(TcpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return TcpayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理"+getChannelName()+"支付回调】";
        _log.info("====== 开始处理"+getChannelName()+"支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = new JSONObject();
        String respString = TcpayConfig.RESPONSE_RESULT_FAIL;
        try {
            req.setCharacterEncoding("utf-8");
            String dt = req.getParameter("dt");             // 时间戳
            String no = req.getParameter("no");             // 订单号
            String money = req.getParameter("money");       // 订单金额
            String userids = req.getParameter("userids");   // 用户id/商户号/设备号
            String type = req.getParameter("type");         // 支付类型
            String version = req.getParameter("version");   // 收款助手版本号
            String mark = req.getParameter("mark");         // 订单备注
            String sign = req.getParameter("sign");         // 签名结果
            // 通知参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("dt", dt);
            paramMap.put("no", no);
            paramMap.put("money", money);
            paramMap.put("userids", userids);
            paramMap.put("type", type);
            paramMap.put("version", version);
            paramMap.put("mark", mark);
            paramMap.put("sign", sign);
            _log.info("{}异步通知内容：{}", logPrefix, paramMap);
            // 验证通知内容
            if (!verifySignature(TcpayConfig.SIGN_KEY, paramMap)) {
                _log.info("{}异步通知验签失败", logPrefix);
                retObj.put(PayConstant.RESPONSE_RESULT, respString);
                return retObj;
            }
            _log.info("{}异步通知：验签成功", logPrefix);
            // 金额为元，转为long类型分
            Long amountL = new BigDecimal(money.trim()).multiply(new BigDecimal(100)).longValue();
            PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByAmount(amountL, mark, TcpayConfig.PAY_ORDER_TIME_OUT);
            if(payOrder == null) {
                _log.info("{}查询订单不存在,注意是否掉单");
                // 增加个异常订单列表，方便补单处理
                retObj.put(PayConstant.RESPONSE_RESULT, respString);
                return retObj;
            }
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                    return retObj;
                }
                _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理"+getChannelName()+"支付回调通知 ======");
            // 返回上游信息
            respString = TcpayConfig.RESPONSE_RESULT_SUCCESS;
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
            retObj.put(PayConstant.RESPONSE_RESULT, respString);
            return retObj;
        }
    }

    /**
     * 签名校验
     * @return
     */
    private boolean verifySignature(String signKey, Map<String, Object> map){
        if(map.get("money") == null) {
            _log.info("金额为空");
            return false;
        };
        String mark = map.get("mark").toString();
        if(map.get("mark") == null) {
            _log.info("备注为空");
        }

        String signStr = map.get("sign").toString();
        map.remove("sign");

        // 商户key
        map.put("signkey", signKey);
        String signStr1 = getSign(map);
        _log.info("待签名的map===>>>：{}",map);
        _log.info("自己的sign值：{}",signStr1);
        if(signStr.equals(signStr1)){
            return true;
        }
        return false;
    }

    public static String getSign(Map<String, Object> paramMap) {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        // 签名格式
        // sign: dt+mark+money+no+type+signkey+userids+version(将此字符串MD5加签 先后顺序按字典排序不可乱 signkey自己生产  不同商户signkey不应相同)
        stringBuffer.append(paramMap.get("dt"));
        stringBuffer.append(paramMap.get("mark"));
        stringBuffer.append(paramMap.get("money"));
        stringBuffer.append(paramMap.get("no"));
        stringBuffer.append(paramMap.get("type"));
        stringBuffer.append(paramMap.get("signkey"));
        stringBuffer.append(paramMap.get("userids"));
        stringBuffer.append(paramMap.get("version"));

        String argPreSign = stringBuffer.toString();
        String signStr = MD5Util.string2MD5(argPreSign);
        return signStr;
    }

    public static void main(String[] args) {

        /*
        0 = {HashMap$Node@13641} "dt" -> "1548071732347"
        1 = {HashMap$Node@13642} "no" -> "21日19时55分"
        2 = {HashMap$Node@13643} "money" -> " 1.00"
        3 = {HashMap$Node@13644} "userids" -> "1002"
        4 = {HashMap$Node@13645} "sign" -> "35bb04b0b8f7b7bfbdd32b0dc8d6abc7"
        5 = {HashMap$Node@13646} "type" -> "1548071732347"
        6 = {HashMap$Node@13647} "version" -> "v20181101"
        7 = {HashMap$Node@13648} "mark" -> "3472"*/

        String dt = "1548074597448";
        String no = "21日20时42分";
        String money = " 1.50";
        String userids = "1002";
        String type = "华夏银行";
        String version = "v20181101";
        String mark = "3472";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("dt", dt);
        paramMap.put("no", no);
        paramMap.put("money", money);
        paramMap.put("userids", userids);
        paramMap.put("type", type);
        paramMap.put("version", version);
        paramMap.put("mark", mark);
        paramMap.put("signkey", TcpayConfig.SIGN_KEY);


        System.out.println("签名结果：" + getSign(paramMap));



        /*处理tcpay支付回调】通知内容resMap：{dt=1548074009, no=21日20时31分, money= 1.01, userids=1002, sign=07078077551fb2fc8219591b3f7dcdd0, type=华夏银行, version=v20181101, mark=3472}

        待签名的map===>>>：{dt=1548074009, no=21日20时31分, money= 1.01, userids=1002, signkey=ABCD123456780, type=华夏银行, version=v20181101, mark=3472}
        2019-01-21 20:34:51.080  INFO 79728 --- [nio-3020-exec-5] o.x.p.c.tcpay.TcpayPayNotifyService      : 自己的sign值：09d602347164fabca8de8c41f8e0969f*/

        /*
        待签名的map===>>>：{dt=1548074597448, no=21日20时42分, money= 1.50, userids=1002, signkey=ABCD1234567890, type=1548074597448, version=v20181101, mark=3472}
        2019-01-21 20:43:33.942  INFO 79728 --- [nio-3020-exec-9] o.x.p.c.tcpay.TcpayPayNotifyService      : 自己的sign值：a1ed2a6a31d42d6ab576ee59a67e61de
        */

        /*【处理tcpay支付回调】通知内容resMap：{dt=1548074597448, no=21日20时42分, money= 1.50, userids=1002, sign=9555fbcf8b1d6b7f9166a3e8534073dd, type=1548074597448, version=v20181101, mark=3472}
        : 待签名的map===>>>：{dt=1548074597448, no=21日20时42分, money= 1.50, userids=1002, signkey=ABCD1234567890, type=1548074597448, version=v20181101, mark=3472}*/


    }

}
