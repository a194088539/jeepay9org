package org.jeepay.mch.message.ctrl;

import com.alibaba.fastjson.JSONObject;
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
import org.jeepay.core.entity.SysMessage;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/25
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/message")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MessageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询系统消息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        SysMessage sysMessage = rpcCommonService.rpcSysMessageService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(sysMessage));
    }

    /**
     * 系统消息列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysMessage sysMessage = getObject(param, SysMessage.class);
        int count = rpcCommonService.rpcSysMessageService.count(sysMessage);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SysMessage> sysMessageList = rpcCommonService.rpcSysMessageService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), sysMessage);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(sysMessageList, count));
    }

}
