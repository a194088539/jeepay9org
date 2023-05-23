package org.jeepay.manage.order.ctrl;

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
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/trans_order")
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
        TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(transOrder));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        TransOrder transOrder = getObject(param, TransOrder.class);
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

    /**
     * 查询订单统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String transOrderId = getString(param, "transOrderId");
        String mchTransNo = getString(param, "mchTransNo");
        Long mchId = getLong(param, "mchId");

        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcTransOrderService.count4All(mchId, transOrderId, mchTransNo, createTimeStartStr, createTimeEndStr);
        Map successMap = rpcCommonService.rpcTransOrderService.count4Success(mchId, transOrderId, mchTransNo, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 总金额
        obj.put("successTotalCount", successMap.get("totalCount"));                 // 成功订单数
        obj.put("successTotalAmount", successMap.get("totalAmount"));               // 成功金额
        obj.put("successTotalChannelCost", successMap.get("totalChannelCost"));     // 成功渠道成本
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

}
