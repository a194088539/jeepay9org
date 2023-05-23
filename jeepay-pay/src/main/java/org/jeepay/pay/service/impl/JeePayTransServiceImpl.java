package org.jeepay.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.PayEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.MySeq;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.TransOrder;
import org.jeepay.core.service.IJeePayTransService;
import org.jeepay.pay.channel.TransInterface;
import org.jeepay.pay.mq.BaseNotify4MchTrans;
import org.jeepay.pay.service.RpcCommonService;
import org.jeepay.pay.util.SpringUtil;

/**
 * @author: aragom
 * @date: 2018/5/29
 * @description:
 */
@Service(version = "1.0.0")
public class JeePayTransServiceImpl implements IJeePayTransService {

    private static final MyLog _log = MyLog.getLog(JeePayTransServiceImpl.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    public BaseNotify4MchTrans baseNotify4MchTrans;

    @Override
    public String executeTrans(TransOrder transOrder) {
        _log.info("调取rpc转账接口,transOrder={}", transOrder);
        TransOrder dbTransOrder = rpcCommonService.rpcTransOrderService.selectByMchIdAndMchTransNo(transOrder.getMchId(), transOrder.getMchTransNo());

        if(null != dbTransOrder
                && (dbTransOrder.getStatus() == PayConstant.TRANS_RESULT_FAIL || dbTransOrder.getStatus() == PayConstant.TRANS_STATUS_TRANING)) {
            return dbTransOrder.getTransOrderId();
        }

        int result = -999;
        String transOrderId = null;
        JSONObject retObj = null;
        TransInterface transInterface = null;
        try{
            transInterface = (TransInterface) SpringUtil.getBean(transOrder.getChannelType() + "TransService");
        }catch (BeansException e) {
            _log.error("不支持的转账渠道,停止转账处理.transOrderId={},channelType={}", transOrder.getTransOrderId(), transOrder.getChannelType());
        }
        if(null == dbTransOrder) {

            // 如果该通道重新定义了订单号,那么使用新的订单号
            String orderId = transInterface.getOrderId(transOrder);
            if(StringUtils.isNotBlank(orderId)) {
                transOrderId = orderId;
            } else {
                // 生成转账ID
                transOrderId = MySeq.getTrans();
            }
            transOrder.setTransOrderId(transOrderId);

            result = rpcCommonService.rpcTransOrderService.createTransOrder(transOrder);
            _log.info("创建转账订单,结果:{}", result);
            result = rpcCommonService.rpcTransOrderService.updateStatus4Ing(transOrderId, "");
            if(result != 1) {
                _log.info("更改转账为转账中({})失败,不能转账.transOrderId={}", PayConstant.TRANS_STATUS_TRANING, transOrderId);
                return transOrderId;
            }
        } else {
            transOrderId = dbTransOrder.getTransOrderId();
            transOrder.setTransOrderId(transOrderId);
        }

        retObj = transInterface.trans(transOrder);

        if(PayConstant.retIsSuccess(retObj)) {
            // 判断业务结果
            // 1. 处理中 2. 成功 3. 失败
            Integer status = retObj.getInteger("status");
            if(status == 1) {
                // 不处理
            }else if(status == 2) {
                // 更新转账状态为成功
                String channelOrderNo = retObj.getString("channelOrderNo");
                result = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, result);
            }else if(status == 3) {
                // 更新转账状态为失败
                String channelErrCode = retObj.getString("channelErrCode");
                String channelErrMsg = retObj.getString("channelErrMsg");
                result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
                _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
            }
        }else {
            // 更新转账状态为失败
            String channelErrCode = retObj.getString("channelErrCode");
            String channelErrMsg = retObj.getString("channelErrMsg");
            result = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg);
            _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, result);
        }
        return transOrderId;
    }

    @Override
    public JSONObject queryBalance(String channelType, String payParam) {
        JSONObject retObj = null;
        try{
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            retObj = transInterface.balance(payParam);
        }catch (BeansException e) {
            _log.warn("不支持的查询余额渠道.channelType={}", channelType);
        }
        return retObj;
    }

    @Override
    public JSONObject queryTrans(String transOrderId) {
        JSONObject retObj = null;
        String channelType= "";
        try{
            TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            if(transOrder == null) return retObj;
            channelType = transOrder.getChannelType();
            TransInterface transInterface = (TransInterface) SpringUtil.getBean(channelType + "TransService");
            retObj = transInterface.query(transOrder);
            // 查询成功
            if(JEEPayUtil.isSuccess(retObj)) {
                // 1-处理中 2-成功 3-失败
                int status = retObj.getInteger("status");
                if(status == 2) {
                    String channelOrderNo = retObj.getString("channelOrderNo");
                    int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Success(transOrderId, channelOrderNo);
                    _log.info("更新转账订单状态为成功({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_SUCCESS, transOrderId, updateTransOrderRows);
                    if (updateTransOrderRows == 1) {
                        // 通知业务系统
                        baseNotify4MchTrans.doNotify(transOrder, true);
                    }
                }else if(status == 3) {
                    String channelOrderNo = retObj.getString("channelOrderNo");
                    String channelErrCode = retObj.getString("channelErrCode");
                    String channelErrMsg = retObj.getString("channelErrMsg");
                    int updateTransOrderRows = rpcCommonService.rpcTransOrderService.updateStatus4Fail(transOrderId, channelErrCode, channelErrMsg, channelOrderNo);
                    _log.info("更新转账订单状态为失败({}),transOrderId={},返回结果:{}", PayConstant.TRANS_STATUS_FAIL, transOrderId, updateTransOrderRows);
                    if (updateTransOrderRows == 1) {
                        // 通知业务系统
                        baseNotify4MchTrans.doNotify(transOrder, true);
                    }
                }
            }
        }catch (BeansException e) {
            _log.warn("不支持的代付查询渠道.channelType={}", channelType);
        }
        return retObj;
    }

}
