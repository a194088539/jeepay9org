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
import org.jeepay.core.entity.TransOrder;
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
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/trans_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class TransOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条转账记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String transOrderId = getStringRequired(param, "transOrderId");
        TransOrder transOrder = new TransOrder();
        transOrder.setMchId(getUser().getId());
        transOrder.setTransOrderId(transOrderId);
        transOrder = rpcCommonService.rpcTransOrderService.find(transOrder);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(transOrder));
    }

    /**
     * 退款订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        TransOrder transOrder = getObject(param, TransOrder.class);
        if(transOrder == null) transOrder = new TransOrder();
        transOrder.setMchId(getUser().getId());
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getString(param, "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getString(param, "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcTransOrderService.count(transOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((getPageIndex(param) -1) * getPageSize(param),
                getPageSize(param), transOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(transOrderList, count));
    }

}
