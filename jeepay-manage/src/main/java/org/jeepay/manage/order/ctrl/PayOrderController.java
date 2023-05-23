package org.jeepay.manage.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.common.ctrl.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/pay_order")
public class PayOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private static final MyLog _log = MyLog.getLog(PayOrderController.class);

    /**
     * 查询单条支付记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String payOrderId = getStringRequired(param, "payOrderId");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payOrder));
    }

    /**
     * 支付订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer page = getInteger(param, "page");
        Integer limit = getInteger(param, "limit");
        PayOrder payOrder = getObject(param, PayOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getString(param, "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getString(param, "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcPayOrderService.count(payOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayOrder> payOrderList = rpcCommonService.rpcPayOrderService.select(
                (getPageIndex(page) -1) * getPageSize(limit), getPageSize(limit), payOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(payOrderList, count));
    }

    /**
     * 查询订单统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String payOrderId = getString(param, "payOrderId");
        String mchOrderNo = getString(param, "mchOrderNo");
        String channelId = getString(param, "channelId");
        Integer passageAccountId = getInteger(param, "passageAccountId");
        Long productId = getLong(param, "productId");
        Long mchId = getLong(param, "mchId");
        Byte productType = getByte(param, "productType");
        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcPayOrderService.count4All(null, channelId, passageAccountId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);
        Map successMap = rpcCommonService.rpcPayOrderService.count4Success(null, channelId, passageAccountId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);
        Map failMap = rpcCommonService.rpcPayOrderService.count4Fail(null, channelId, passageAccountId, mchId, productId, payOrderId, mchOrderNo, productType, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 总金额
        obj.put("successTotalCount", successMap.get("totalCount"));                 // 成功订单数
        obj.put("successTotalAmount", successMap.get("totalAmount"));               // 成功金额
        obj.put("successTotalMchIncome", successMap.get("totalMchIncome"));         // 成功商户收入
        obj.put("successTotalAgentProfit", successMap.get("totalAgentProfit"));     // 成功代理商利润
        obj.put("successTotalPlatProfit", successMap.get("totalPlatProfit"));       // 成功平台利润
        obj.put("failTotalCount", failMap.get("totalCount"));                       // 未完成订单数
        obj.put("failTotalAmount", failMap.get("totalAmount"));                     // 未完成金额
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

    /**
     * 补单
     * 1. 将订单为 支付中 状态的修改为支付成功
     * 2. 给商户下发一次通知
     * @return
     */
    @RequestMapping("/reissue")
    @ResponseBody
    public ResponseEntity<?> reissue(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);

        // 判断输入的超级密码是否正确
        String password = getStringRequired(param, "password");
        if(!MchConstant.MGR_SUPER_PASSWORD.equals(password)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_SUPER_PASSWORD_NOT_MATCH));
        }
        // 是否通知商户
        boolean isNotifyMch = false;
        // 修改订单状态

        String payOrderId = getStringRequired(param, "payOrderId");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if(payOrder.getStatus() == PayConstant.PAY_STATUS_PAYING) { // 初始或支付中
            // 修改状态为支付成功,
            int updateCount = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrderId);
            _log.info("[补单]userId={},payOrderId={},将支付中修改为支付成功,返回结果:{}", getUser().getId(), payOrder.getPayOrderId(), updateCount);
            if(updateCount == 1) isNotifyMch = true;
        }

        // 发送商户通知
        if(isNotifyMch) {
            rpcCommonService.rpcJeePayNotifyService.executePayNotify(payOrderId);
        }

        return ResponseEntity.ok(JeePayResponse.buildSuccess(payOrder));
    }

    @RequestMapping("/channel_order_query")
    @ResponseBody
    public ResponseEntity<?> channelOrderQuery(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String payOrderId = getStringRequired(param, "payOrderId");
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if(payOrder.getStatus() == PayConstant.PAY_STATUS_SUCCESS
                || payOrder.getStatus() == PayConstant.PAY_STATUS_COMPLETE) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess("已支付或已完成订单不允许查单"));
        }

        if(payOrder.getStatus() == PayConstant.PAY_STATUS_FAILED
                || payOrder.getStatus() == PayConstant.PAY_STATUS_EXPIRED) {
            return ResponseEntity.ok(JeePayResponse.buildSuccess("订单支付失败或已过期"));
        }
        // 初始或支付中
        if(payOrder.getStatus() == PayConstant.PAY_STATUS_PAYING
                || payOrder.getStatus() == PayConstant.PAY_STATUS_INIT) {
            JSONObject resObj = rpcCommonService.rpcJeePayOrderService.channelOrderQuery(payOrder);
            if(JEEPayUtil.isSuccess(resObj)) {
                JSONObject jsonObject = resObj.getJSONObject("channelObj");
                return ResponseEntity.ok(JeePayResponse.buildSuccess(jsonObject == null ? "转账接口没有返回channelObj" : jsonObject.toJSONString()));
            }else {
                return ResponseEntity.ok(JeePayResponse.buildSuccess("查询通道异常【" + resObj.getString(PayConstant.RESULT_PARAM_ERRDES) + "】"));
            }
        }

        return ResponseEntity.ok(JeePayResponse.buildSuccess("订单状态未知，未调用渠道查单接口"));
    }

}