package org.jeepay.task.reconciliation.channel.swiftpay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.result.WxPayBillBaseResult;
import com.github.binarywang.wxpay.bean.result.WxPayBillResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.CheckBatch;
import org.jeepay.task.reconciliation.channel.BaseBill;
import org.jeepay.task.reconciliation.channel.swiftpay.util.MD5;
import org.jeepay.task.reconciliation.channel.swiftpay.util.SignUtils;
import org.jeepay.task.reconciliation.channel.swiftpay.util.XmlUtils;
import org.jeepay.task.reconciliation.entity.ReconciliationEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: aragom
 * @date: 18/4/01
 * @description: 威富通对账文件
 */
@Service
public class SwiftpayBillService extends BaseBill {

    private static final MyLog _log = MyLog.getLog(SwiftpayBillService.class);

    @Override
    public String getChannelName() {
        return null;
    }

    @Override
    public JSONObject downloadBill(JSONObject param, CheckBatch batch) {

        String logPrefix = "【威富通对账下载】";
        JSONObject retObj = new JSONObject();
        retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_SUCCESS);
        /*
        try{
            String billDate = DateUtil.date2Str(batch.getBillDate(), DateUtil.FORMAT_YYYY_MM_DD2);
            WxPayConfig wxPayConfig = WxpayUtil.getWxPayConfig(param.getString("payParam"));
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig);
            WxPayBillResult wxPayBillResult;
            try {
                wxPayBillResult = wxPayService.downloadBill(billDate, "ALL", null, null);
                map.put("bill", bill2Reconciliation(wxPayBillResult, batch));
                _log.info("{} >>> 下载成功", logPrefix);
            } catch (WxPayException e) {
                if("NO Bill Exist".equalsIgnoreCase(e.getReturnMsg()) || "Bill Creating".equalsIgnoreCase(e.getReturnMsg())) {
                    map.put("bill", new ArrayList<>());
                    map.put("errDes", e.getReturnMsg());
                }else {
                    _log.error(e, "下载失败");
                    //出现业务错误
                    _log.info("{}下载返回失败", logPrefix);
                    _log.info("err_code:{}", e.getErrCode());
                    _log.info("err_code_des:{}", e.getErrCodeDes());
                    map.put("errDes", e.getErrCodeDes());
                    map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                }

            }
        }catch (Exception e) {
            _log.error(e, "微信对账下载异常");
            map.put("errDes", "微信对账下载异常");
            map.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
        }*/


        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(param.getString("payParam"));
        SortedMap<String,String> map = new TreeMap();
        String billDate = DateUtil.date2Str(batch.getBillDate(), DateUtil.FORMAT_YYYY_MM_DD2);
        // 接口类型
        map.put("service", "pay.bill.merchant");
        // 账单日期
        map.put("bill_date", billDate);
        // 账单类型
        map.put("bill_type", "SUCCESS");
        String res;
        map.put("mch_id", swiftpayConfig.getMchId());
        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            String req = XmlUtils.parseXML(map);
            _log.info("Swiftpass请求数据:{}", req);
            StringEntity entityParams = new StringEntity(req, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    retObj.put("errDes", "验证签名不通过");
                    retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                    return retObj;
                }
                // 解析对账文件
                retObj.put("bill", new LinkedList<>());

                return retObj;
            }else{
                retObj.put("errDes", "操作失败!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "威富通对账单下载异常");
            retObj.put("errDes", "威富通对账单下载异常");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);

        } finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    _log.error(e, "");
                }
            }
        }

        return retObj;
    }

    /**
     * 将微信数据转为对账统一对象格式
     * @param wxPayBillResult
     * @return
     */
    List<ReconciliationEntity> bill2Reconciliation(WxPayBillResult wxPayBillResult, CheckBatch batch) {
        List<ReconciliationEntity> reconciliationEntityList = new LinkedList<>();
        List<WxPayBillBaseResult> wxPayBillBaseResults = wxPayBillResult.getWxPayBillBaseResultLst();
        if(CollectionUtils.isEmpty(wxPayBillBaseResults)) return reconciliationEntityList;
        // 设置批次数据
        batch.setBankTradeCount(Integer.parseInt(wxPayBillResult.getTotalRecord().trim()));
        batch.setBankTradeAmount(new BigDecimal(wxPayBillResult.getTotalFee().trim()).multiply(new BigDecimal(100)).longValue());
        batch.setBankRefundAmount(new BigDecimal(wxPayBillResult.getTotalRefundFee().trim()).multiply(new BigDecimal(100)).longValue());
        batch.setBankFee(new BigDecimal(wxPayBillResult.getTotalPoundageFee().trim()).multiply(new BigDecimal(100)).longValue());

        for(WxPayBillBaseResult result : wxPayBillBaseResults) {
            ReconciliationEntity entity = new ReconciliationEntity();
            // 设置支付时间
            entity.setOrderTime(DateUtil.str2date(result.getTradeTime()));
            // 设置微信流水号
            entity.setBankTrxNo(result.getTransationId());
            entity.setBankOrderNo(result.getTransationId());
            // 设置微信订单状态(默认全部是success)
            entity.setBankTradeStatus(result.getTradeState());
            // 设置微信账单金额(元转分)
            entity.setBankAmount(new BigDecimal(result.getTotalFee().trim()).multiply(new BigDecimal(100)).longValue());
            // 设置手续费(元转分)
            entity.setBankFee(new BigDecimal(result.getPoundage().trim()).multiply(new BigDecimal(100)).longValue());
            reconciliationEntityList.add(entity);
        }
        return reconciliationEntityList;
    }


}
