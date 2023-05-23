package org.jeepay.manage.reconciliation.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.CheckBatch;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bill/check_batch")
public class CheckBatchController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        CheckBatch checkBatch = rpcCommonService.rpcCheckService.findByBatchId(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(checkBatch));
    }

    /**
     * 记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer page = getInteger(param, "page");
        Integer limit = getInteger(param, "limit");
        CheckBatch checkBatch = getObject(param, CheckBatch.class);
        int count = rpcCommonService.rpcCheckService.countCheckBatch(checkBatch);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<CheckBatch> checkBatchList = rpcCommonService.rpcCheckService.selectCheckBatch(
                (getPageIndex(page) -1) * getPageSize(limit), getPageSize(limit), checkBatch);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(checkBatchList, count));
    }

}
