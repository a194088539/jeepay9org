package org.jeepay.pay.channel.ecpsspay;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.pay.channel.BaseTrans;
import org.jeepay.pay.channel.ecpsspay.util.SignUtil;
import org.jeepay.pay.channel.ecpsspay.util.XmlUtil;
import org.jeepay.pay.channel.swiftpay.util.XmlUtils;
import org.jeepay.pay.mq.BaseNotify4MchPay;
import org.jeepay.pay.mq.Mq4TransQuery;
import org.jeepay.pay.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sand.sandutil.tool.Base64Decoder;
import com.sand.sandutil.tool.Base64Encoder;

@Service
public class EcpsspayTransService extends BaseTrans {

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    @Autowired
    private Mq4TransQuery mq4TransQuery;

    private static final MyLog _log = MyLog.getLog(EcpsspayTransService.class);

    @Override
    public String getChannelName() {
        return EcpsspayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject trans(TransOrder transOrder) {
        String logPrefix = "【汇潮转账】";
        JSONObject retObj = buildRetObj();
        try {
            String transOrderId = transOrder.getTransOrderId();
            EcpsspayConfig ecpsspayConfig = new EcpsspayConfig(getTransParam(transOrder));
            SortedMap<String,Object> map = new TreeMap();
            // 接口类型
            map.put("accountNumber", ecpsspayConfig.getMchId());
            // 接口类型
            map.put("tt", "0");
            // 接口类型
            map.put("signType", "RSA");
            // 通知地址
            map.put("notifyURL", payConfig.getNotifyUrl(getChannelName()));
            SortedMap<String,String> transMap = new TreeMap();
            // 商户订单号
            transMap.put("transId", transOrderId);
            // 帐户名
            transMap.put("bankCode", transOrder.getBankCode());
            // 帐户名
            transMap.put("provice", transOrder.getProvince());
            // 帐户名
            transMap.put("city", transOrder.getCity());
            // 帐户名
            transMap.put("accountName", transOrder.getAccountName());
            // 银行卡号
            transMap.put("cardNo", transOrder.getAccountNo());
            // 金额
            transMap.put("amount", AmountUtil.convertCent2Dollar(transOrder.getAmount().toString()));
            // 通知地址
            transMap.put("remark", transOrder.getRemarkInfo());
            String signStr = "transId="+map.get("transId")+"&"
                +"accountNumber="+map.get("accountNumber")+"&"
                +"cardNo="+map.get("cardNo")+"&"
                +"amount="+map.get("amount");
            //签名
            transMap.put("secureCode", SignUtil.sign(ecpsspayConfig.getMchKey().getBytes(SignUtil.CHARACTER_ENCODING_UTF_8), signStr));
            map.put("transferList",transMap);
            String url = ecpsspayConfig.getReqUrl()+"/transfer/transferFixed";

            String req = XmlUtil.mapToSignXml(map);
            _log.info("汇潮支付转账请求数据:{}", req);
            Map<String,String> paramMap = new HashMap<>(1);
            paramMap.put("transData", Base64Encoder.encode(req.getBytes()));
            String result = HttpUtils.getInstance().post(url, paramMap);
            _log.info("返回数据：{}",result);
            if(StringUtils.isBlank(result)) {
                _log.info("{} >>> 请求汇潮转账没有响应,将转账转为失败", logPrefix);
                retObj.put("status", 3);    // 失败
            }else {
                result = new String(Base64Decoder.decode(result));
                _log.info("解码后数据：{}",result);
                JSONObject resultObj = JSON.parseObject(result);
                String status = resultObj.getString("errCode");
                retObj.put("channelErrCode", status);
                retObj.put("channelErrMsg", status);
                // 0000成功
                if("0000" == status) {
                    JSONObject listJsonObject = resultObj.getJSONObject("transferList");
                    String resCode = listJsonObject.getString("resCode");
                    if("0000" == resCode){
                        // 交易处理中
                        _log.info("{} >>> 转账处理中", logPrefix);
                        retObj.put("status", 1);    // 处理中
                        JSONObject msgObj = new JSONObject();
                        msgObj.put("count", 1);
                        msgObj.put("transOrderId", transOrderId);
                        msgObj.put("channelName", getChannelName());
                        mq4TransQuery.send(msgObj.toJSONString(), 10 * 1000);  // 10秒后查询
                    }else{
                        // 交易失败
                        _log.info("{} >>> 转账失败", logPrefix);
                        retObj.put("status", 3);    // 失败
                    }
                }else{
                    // 交易失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    retObj.put("status", 3);    // 失败
                }
            }
            return retObj;
        }catch (Exception e){
            _log.error(e, "汇潮支付转账异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }
    @Override
    public JSONObject query(TransOrder transOrder) {
        String logPrefix = "【汇潮支付转账查询】";
        JSONObject retObj = buildRetObj();
        try{
            EcpsspayConfig ecpsspayConfig = new EcpsspayConfig(getTransParam(transOrder));
            String url = ecpsspayConfig.getReqUrl();
            url += "/transfer/transferQueryFixed";
            String transOrderId = transOrder.getTransOrderId();
            SortedMap<String,String> textMap = new TreeMap<>();
            textMap.put("merchantNumber", ecpsspayConfig.getMchId());
            textMap.put("mertransferID", transOrderId);
            textMap.put("signType", "RSA");
            textMap.put("queryTimeBegin", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            textMap.put("queryTimeEnd", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
            textMap.put("requestTime", DateUtil.getCurrentTimeStr(DateUtil.FORMAT_YYYYMMDDHHMMSS));
            String signStr = ecpsspayConfig.getMchId()+"&"+textMap.get("requestTime");
            textMap.put("sign", SignUtil.sign(ecpsspayConfig.getMchKey().getBytes("UTF-8"), signStr));
            _log.info("汇潮转账查询,请求URL:{},参数:{}",url,textMap);
            String req = XmlUtils.parseXML(textMap);
            _log.info("汇潮转账查询请求数据:{}", req);
            Map<String,String> paramMap = new HashMap<>(1);
            paramMap.put("requestDomain", Base64Encoder.encode(req.getBytes()));
            String result = HttpUtils.getInstance().post(url, paramMap);
            _log.info("汇潮支付查询请求,响应结果:{}",result);
            if(StringUtils.isBlank(result)) {
                _log.info("{} >>> 请求汇潮没有响应,将转账转为失败", logPrefix);
                retObj.put("status", 3);    // 失败
            }else {
                JSONObject resultObj = JSON.parseObject(result);
                String status = resultObj.getString("code");
                String respDesc = resultObj.getString("code");
                retObj.put("channelErrCode", status);
                retObj.put("channelErrMsg", respDesc);
                retObj.put("channelObj", resultObj);
                // 0000-成功
                if("0000" == status) {
                    JSONObject transferJsonObject = resultObj.getJSONObject("transfer");
                    String state = transferJsonObject.getString("state");
                    if("00" == state){
                        // 交易成功
                        _log.info("{} >>> 转账成功", logPrefix);
                        retObj.put("transOrderId", transOrderId);
                        retObj.put("status", 2);            // 成功
                        retObj.put("channelOrderNo", transferJsonObject.getString("mertransferID"));
                    }else if("11" == state){
                        // 交易失败
                        _log.info("{} >>> 转账失败", logPrefix);
                        retObj.put("status", 3);    // 失败
                        retObj.put("channelOrderNo", transOrderId);
                    }else{
                        // 交易处理中
                        _log.info("{} >>> 转账处理中", logPrefix);
                        retObj.put("status", 1);    // 处理中
                    }
                }else {
                    // 交易失败
                    _log.info("{} >>> 转账失败", logPrefix);
                    retObj.put("status", 3);    // 失败
                    retObj.put("channelOrderNo", transOrderId);
                }
            }
            return retObj;
        }catch (Exception e) {
            _log.error(e, "汇潮代付查询异常");
            retObj = buildFailRetObj();
            return retObj;
        }
    }

    @Override
    public JSONObject balance(String payParam) {
        return buildFailRetObj();
    }

}
