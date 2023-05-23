package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.SysMessage;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/19
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/message")
public class SysMessageController extends BaseController {

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
     * 新增系统消息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增系统消息" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysMessage sysMessage = getObject(param, SysMessage.class);
        int count = rpcCommonService.rpcSysMessageService.add(sysMessage);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改系统消息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改系统消息" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysMessage sysMessage = getObject(param, SysMessage.class);
        int count = rpcCommonService.rpcSysMessageService.update(sysMessage);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
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

    /**
     * 删除系统消息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除系统消息" )
    public ResponseEntity<?> delete(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String ids = getStringRequired(param, "ids");
        String[] idss = ids.split(",");
        List<Long> _ids = new LinkedList<>();
        for(String id : idss) {
            if(NumberUtils.isDigits(id)) _ids.add(Long.parseLong(id));
        }
        rpcCommonService.rpcSysMessageService.batchDelete(_ids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
