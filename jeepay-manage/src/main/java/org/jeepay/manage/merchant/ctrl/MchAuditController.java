package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
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
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.merchant.service.MchInfoService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_audit")
public class MchAuditController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchAuditController.class);

    @Autowired
    private MchInfoService mchInfoService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = getObject(param, MchInfo.class);
        int count = mchInfoService.countAudit(mchInfo);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchInfo> mchInfoList = mchInfoService.getMchAuditInfoList((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), mchInfo);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchInfoList, count));
    }

    @RequestMapping(value = "/audit")
    @ResponseBody
    @MethodLog( remark = "审核商户" )
    public ResponseEntity<?> audit(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 返回对象
        Long mchId = getLongRequired(param, "mchId");
        Byte status = getByteRequired(param, "status");
        int count = mchInfoService.audit(mchId, status);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}