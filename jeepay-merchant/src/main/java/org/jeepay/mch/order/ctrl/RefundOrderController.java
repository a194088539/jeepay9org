package org.jeepay.mch.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.entity.RefundOrder;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/refund_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class RefundOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条退款记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String refundOrderId = getStringRequired(param, "refundOrderId");
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setMchId(getUser().getId());
        refundOrder.setRefundOrderId(refundOrderId);
        refundOrder = rpcCommonService.rpcRefundOrderService.find(refundOrder);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(refundOrder));
    }

    /**
     * 退款订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        RefundOrder refundOrder = getObject(param, RefundOrder.class);
        if(refundOrder == null) refundOrder = new RefundOrder();
        refundOrder.setMchId(getUser().getId());
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getString(param, "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getString(param, "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcRefundOrderService.count(refundOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<RefundOrder> refundOrderList = rpcCommonService.rpcRefundOrderService.select((getPageIndex(param) -1) * getPageSize(param),
                getPageSize(param), refundOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(refundOrderList, count));
    }

}
