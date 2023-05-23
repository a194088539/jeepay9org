package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.entity.SysLog;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/syslog")
public class SysLogController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysLog sysLog = getObject(param, SysLog.class);

        int count = rpcCommonService.rpcSysLogService.count(sysLog);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SysLog> result = rpcCommonService.rpcSysLogService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), sysLog);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(result, count));
    }
}