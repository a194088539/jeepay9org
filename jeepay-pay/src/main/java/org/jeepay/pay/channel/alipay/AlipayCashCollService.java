package org.jeepay.pay.channel.alipay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import org.springframework.stereotype.Service;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.util.AmountUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayCashCollConfig;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.core.entity.PayOrderCashCollRecord;
import org.jeepay.core.entity.PayPassageAccount;
import org.jeepay.pay.channel.BaseCashColl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlipayCashCollService extends BaseCashColl {

    private static final MyLog _log = MyLog.getLog(AlipayCashCollService.class);

    @Override
    public JSONObject coll(PayOrder payOrder) {

        JSONObject result = new JSONObject();
        result.put("result", "fail");
        try {
            PayPassageAccount account = rpcCommonService.rpcPayPassageAccountService.findById(payOrder.getPassageAccountId());
            if(account.getCashCollStatus() != MchConstant.PUB_YES){
                result.put("msg", "未开启需资金归集服务"); return result;
            }

            PayCashCollConfig selectCondition = new PayCashCollConfig();
            selectCondition.setStatus(MchConstant.PUB_YES); //仅查询开启状态

            if(account.getCashCollMode() == 1){ //继承
                selectCondition.setBelongPayAccountId(0); //查询系统全局配置
            }else{
                selectCondition.setBelongPayAccountId(account.getId()); //查询子账户的特有配置账号
            }

            List<PayCashCollConfig> configList = rpcCommonService.rpcPayCashCollConfigService.selectAll(selectCondition);

            if(configList == null || configList.isEmpty()){
                result.put("msg", "资金归集账号未配置"); return result;
            }

            //判断归集配置信息 是否合法
            BigDecimal totalPercentage = BigDecimal.ZERO;
            for(PayCashCollConfig config : configList) {
                BigDecimal transInPercentage = config.getTransInPercentage();
                if(transInPercentage.compareTo(BigDecimal.ZERO) <= 0 ){
                    result.put("msg", "资金归集账号配置有误，比例不得小于等于0");  return result;
                }
                totalPercentage.add(transInPercentage);
            }

            if(totalPercentage.compareTo(new BigDecimal(100)) > 0 ){
                result.put("msg", "资金归集账号配置有误，总比例不得高于100%");
                return result;
            }

            //是否全部分配完成
            boolean isAllColl = totalPercentage.equals(new BigDecimal(100));

            //组装请求报文
            AlipayConfig alipayConfig = new AlipayConfig(getPayParam(payOrder));
            AlipayClient client = new DefaultAlipayClient(alipayConfig.getReqUrl(), alipayConfig.getAppId(), alipayConfig.getPrivateKey(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);

            AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();

            JSONObject bizContent = new JSONObject();
            bizContent.put("out_request_no", payOrder.getPayOrderId());
            bizContent.put("trade_no", payOrder.getChannelOrderNo());

            //订单可分账余额 = 订单金额 - 通道费用
            Long availableAmount = payOrder.getAmount() - payOrder.getChannelCost() ;

            //累加已分账金额
            Long sumCollAmount = 0L;

            List<PayOrderCashCollRecord> recordList = new ArrayList<>(); //返回记录结果集
            JSONArray jsonArray = new JSONArray();

            for(int i = 0 ; i< configList.size() ; i++) {

                PayCashCollConfig config = configList.get(i);
                PayOrderCashCollRecord record = new PayOrderCashCollRecord();
                record.setPayOrderId(payOrder.getPayOrderId());
                record.setChannelOrderNo(payOrder.getChannelOrderNo());
                record.setRequestNo(payOrder.getPayOrderId());
                record.setTransInUserName(config.getTransInUserName());
                record.setTransInUserAccount(config.getTransInUserAccount());
                record.setTransInUserId(config.getTransInUserId());
                record.setTransInPercentage(config.getTransInPercentage());

                //使用比例方式 计算本账户归集金额
                Long collAmount = JEEPayUtil.calOrderMultiplyRate(availableAmount, config.getTransInPercentage());

                //如果分配比例为100%，并且当前为最后一个，将剩余金额作为本账户的归集金额
                if(isAllColl && i == (configList.size() - 1)){
                    collAmount = availableAmount - sumCollAmount;
                }

                record.setTransInAmount(collAmount);
                recordList.add(record);

                if(collAmount <= 0) continue; //当金额小于等于0， 仅记录， 不上送分账金额为0的 分账账户信息。

                JSONObject singleAccount = new JSONObject();
                singleAccount.put("trans_in", config.getTransInUserId());
                singleAccount.put("amount", AmountUtil.convertCent2Dollar(collAmount + "")); //单位：元
                singleAccount.put("desc", payOrder.getPayOrderId() + "订单分账");
                jsonArray.add(singleAccount);

                sumCollAmount += collAmount;
            }

            result.put("records", recordList);

            bizContent.put("royalty_parameters", jsonArray);
            request.setBizContent(bizContent.toString());
            AlipayTradeOrderSettleResponse response = client.execute(request);
            if(response.isSuccess()){
                result.put("result", "success");
                result.put("msg", "成功");
                return result;
            }

            _log.error("资金归集接口返回失败, payOrderId={}, code={}, msg={}, subCode={}, subMsg={}",
                    payOrder.getPayOrderId(), response.getCode(), response.getMsg(), response.getSubCode(), response.getSubMsg());
            result.put("msg", "资金归集接口返回失败[subCode="+response.getSubCode()+", subMsg="+response.getSubMsg()+"]");

            // 根据错误码判断是否关闭该账号
            if("ACQ.TRADE_SETTLE_ERROR".equals(response.getSubCode())){
                result.put("closeAccount" , payOrder.getPassageAccountId());
            }

            return result;

        } catch (AlipayApiException e) {
            _log.error("资金归集渠道异常：", e);
            result.put("msg", e.getErrMsg());
            return result;
        }
    }
}
