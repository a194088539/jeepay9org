package org.jeepay.manage.merchant.ctrl;

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
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.MchQrCode;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_qrcode")
public class MchQrCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增二维码" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchQrCode mchQrCode = getObject(param, MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.add(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchQrCode mchQrCode = getObject(param, MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.count(mchQrCode);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchQrCode> mchQrCodeList = rpcCommonService.rpcMchQrCodeService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), mchQrCode);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchQrCodeList, count));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/view_code")
    @ResponseBody
    public ResponseEntity<?> viewCode(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        MchQrCode mchQrCode = rpcCommonService.rpcMchQrCodeService.findById(id);
        if(mchQrCode == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_QR_CODE_STOP));
        }
        if(MchConstant.PUB_YES != mchQrCode.getStatus().byteValue()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        StringBuilder sb = new StringBuilder(mainConfig.getMchQrUrl() + "?");
        sb.append("mchId=").append(mchQrCode.getMchId()).append("&appId=").append(mchQrCode.getAppId())
                .append("&codeId=").append(mchQrCode.getId());
        JSONObject data = new JSONObject();
        data.put("codeUrl", sb.toString());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        MchQrCode mchQrCode = rpcCommonService.rpcMchQrCodeService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchQrCode));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改二维码" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "minAmount", "maxAmount");
        MchQrCode mchQrCode = getObject(param, MchQrCode.class);
        int count = rpcCommonService.rpcMchQrCodeService.update(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
