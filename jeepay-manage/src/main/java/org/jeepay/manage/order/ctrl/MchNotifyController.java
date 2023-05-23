package org.jeepay.manage.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.MchNotify;
import org.jeepay.core.entity.PayOrder;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_notify")
public class MchNotifyController extends BaseController {

    private static final MyLog _log = MyLog.getLog(MchNotifyController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条商户通知记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String orderId = getStringRequired(param, "orderId");
        MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(orderId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchNotify));
    }

    /**
     * 商户通知记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer page = getInteger(param, "page");
        Integer limit = getInteger(param, "limit");
        MchNotify mchNotify = getObject(param, MchNotify.class);
        int count = rpcCommonService.rpcMchNotifyService.count(mchNotify);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchNotify> mchNotifyList = rpcCommonService.rpcMchNotifyService.select(
                (getPageIndex(page) -1) * getPageSize(limit), getPageSize(limit), mchNotify);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchNotifyList, count));
    }

    /**
     * 重发通知(待实现)
     * @return
     */
    @RequestMapping("/resend")
    @ResponseBody
    @MethodLog( remark = "重发商户通知" )
    public ResponseEntity<?> resend(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String orderIdsParam = getStringRequired(param, "orderIds");
        JSONArray orderIds = JSON.parseArray(orderIdsParam);

        JSONObject resultJSON = new JSONObject();
        if(orderIds.size() <= 0 ){
            resultJSON.put("errMsg", "请选择重发订单！");
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resultJSON));
        }

        if(orderIds.size() > 10 ){
            resultJSON.put("errMsg", "批量重发商户通知个数不得大于10个！");
            return ResponseEntity.ok(JeePayResponse.buildSuccess(resultJSON));
        }

        int sendCount = 0 ;
        for(Object id: orderIds){
            MchNotify mchNotify = rpcCommonService.rpcMchNotifyService.findByOrderId(id.toString());
            if(mchNotify.getStatus() == PayConstant.MCH_NOTIFY_STATUS_SUCCESS || StringUtils.isBlank(mchNotify.getNotifyUrl())) continue;

            try {
                byte updateCount = (byte) (mchNotify.getNotifyCount() + 1);
                String result = JEEPayUtil.call4Post(mchNotify.getNotifyUrl());
                if("success".equalsIgnoreCase(result)){
                    rpcCommonService.rpcMchNotifyService.updateMchNotifySuccess(mchNotify.getOrderId(), result, updateCount);
                }else{
                    rpcCommonService.rpcMchNotifyService.updateMchNotifyFail(mchNotify.getOrderId(), result, updateCount);
                }
                sendCount ++;
            } catch (Exception e) {
                _log.error("重发失败 id={}", id, e);
            }
        }
        resultJSON.put("sendCount", sendCount);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(resultJSON));
    }

}