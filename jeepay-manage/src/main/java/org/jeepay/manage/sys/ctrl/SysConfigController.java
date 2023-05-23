package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/config")
public class SysConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询配置信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String type = getStringRequired(param, "type");
        JSONObject obj = rpcCommonService.rpcSysConfigService.getSysConfigObj(type);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

    /**
     * 修改配置信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改配置信息" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String type = request.getParameter("type");
        if("sett".equals(type)) {
            // 将金额元转成分
            handleParamAmount(param, "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount", "feeLevel", "drawFeeLimit");

        }
        rpcCommonService.rpcSysConfigService.update(param);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}