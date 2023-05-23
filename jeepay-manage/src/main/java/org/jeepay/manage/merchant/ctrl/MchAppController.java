package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MySeq;
import org.jeepay.core.entity.MchApp;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/13
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/app")
public class MchAppController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchApp mchApp = getObject(param, MchApp.class);
        int count = rpcCommonService.rpcMchAppService.count(mchApp);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchApp> mchAppList = rpcCommonService.rpcMchAppService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), mchApp);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchAppList, count));
    }

    /**
     * 查询应用信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String appId = getStringRequired(param, "appId");
        MchApp mchApp = rpcCommonService.rpcMchAppService.findById(appId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchApp));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "添加商户应用" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        String appName = getStringRequired(param, "appName");
        String remark = param.getString("remark");
        MchApp mchApp = new MchApp();
        mchApp.setMchId(mchId);
        mchApp.setAppName(mchInfo.getName());
        mchApp.setAppId(MySeq.getUUID());
        mchApp.setAppName(appName);
        mchApp.setStatus(MchConstant.PUB_YES);
        mchApp.setRemark(remark);
        int count = rpcCommonService.rpcMchAppService.add(mchApp);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户应用" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String appId = getStringRequired(param, "appId");
        String appName = param.getString("appName");
        String remark = param.getString("remark");
        Byte status = param.getByte("status");
        MchApp mchApp = new MchApp();
        mchApp.setAppId(appId);
        if(StringUtils.isNotBlank(appName)) mchApp.setAppName(appName);
        if(status != null && status.byteValue() == MchConstant.PUB_YES) {
            mchApp.setStatus(MchConstant.PUB_YES);
        }else if(status != null && status.byteValue() == MchConstant.PUB_NO) {
            mchApp.setStatus(MchConstant.PUB_NO);
        }
        if(StringUtils.isNotBlank(remark)) mchApp.setRemark(remark);
        int count = rpcCommonService.rpcMchAppService.update( mchApp);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
