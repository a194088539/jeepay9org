package org.jeepay.mch.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.DateUtil;
import org.jeepay.core.entity.MchTradeOrder;
import org.jeepay.core.entity.PayProduct;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/trade_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchTradeOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String tradeOrderId = getStringRequired(param, "tradeOrderId");
        if(StringUtils.isBlank(tradeOrderId)) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(getUser().getId(), tradeOrderId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchTradeOrder));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchTradeOrder mchTradeOrder = getObject(param, MchTradeOrder.class);
        if(mchTradeOrder == null) mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setMchId(getUser().getId());
        Byte tradeType = getByte(param, "tradeType");
        if(tradeType != null) mchTradeOrder.setTradeType(tradeType);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getString(param, "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getString(param, "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcMchTradeOrderService.count(mchTradeOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.select(
                (getPageIndex(param) -1) * getPageSize(param), getPageSize(param), mchTradeOrder, createTimeStart, createTimeEnd);

        Map<String, PayProduct> payProductMap = rpcCommonService.rpcCommonService.getPayProdcutMap(null);
        List<JSONObject> objects = new LinkedList<>();
        for(MchTradeOrder order : mchTradeOrderList) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            PayProduct payProduct = payProductMap.get(String.valueOf(order.getProductId()));
            object.put("productName", payProduct == null ? "" : payProduct.getProductName());  // 产品名称
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getUser().getId();
        String tradeOrderId = getString(param, "tradeOrderId");
        String payOrderId = getString(param, "payOrderId");
        Byte tradeType = getByte(param, "tradeType");
        Byte status = getByte(param, "status");

        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchTradeOrderService.count4All(mchId, tradeOrderId, payOrderId, tradeType, status, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalMchIncome", allMap.get("totalMchIncome"));                 // 入账金额
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

}
