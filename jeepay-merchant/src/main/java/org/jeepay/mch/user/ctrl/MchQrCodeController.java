package org.jeepay.mch.user.ctrl;

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
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_qrcode")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchQrCodeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增商户二维码" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "minAmount", "maxAmount");
        String appId = getStringRequired(param, "appId");
        String channels = getStringRequired(param, "channels");
        String codeName = getStringRequired(param, "codeName");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getId());
        mchQrCode.setAppId(appId);
        mchQrCode.setChannels(channels);
        mchQrCode.setCodeName(codeName);
        if(param.getString("minAmount") != null) mchQrCode.setMinAmount(param.getLong("minAmount"));
        if(param.getString("maxAmount") != null) mchQrCode.setMaxAmount(param.getLong("maxAmount"));
        int count = rpcCommonService.rpcMchQrCodeService.add(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String appId = getStringRequired(param, "appId");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getId());
        mchQrCode.setAppId(appId);
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
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getId());
        mchQrCode.setId(id);
        mchQrCode = rpcCommonService.rpcMchQrCodeService.find(mchQrCode);
        if(mchQrCode == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_QR_CODE_STOP));
        }
        if(MchConstant.PUB_YES != mchQrCode.getStatus().byteValue()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        StringBuilder sb = new StringBuilder(mainConfig.getMchApiUrl() + "/payment/qrcode?");
        sb.append("mchId=").append(mchQrCode.getMchId()).append("&appId=").append(mchQrCode.getAppId())
                .append("&codeId=").append(mchQrCode.getId());


        JSONObject data = new JSONObject();
        data.put("codeUrl", sb.toString());
        // codeUrl = '/api/payment/qrcode_img_get?' + 'url=' + data.payParams.codeUrl + '&widht=200&height=200';
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(sb.toString()) + "&widht=200&height=200");
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
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setMchId(getUser().getId());
        mchQrCode.setId(id);
        mchQrCode = rpcCommonService.rpcMchQrCodeService.find(mchQrCode);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchQrCode));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户二维码" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "minAmount", "maxAmount");
        Long id = getLongRequired(param, "id");
        String channels = param.getString("channels");
        String codeName = param.getString("codeName");
        Byte status = param.getByte("status");
        MchQrCode mchQrCode = new MchQrCode();
        mchQrCode.setId(id);
        if(StringUtils.isNotBlank(channels)) mchQrCode.setChannels(channels);
        if(StringUtils.isNotBlank(codeName)) mchQrCode.setCodeName(codeName);
        if(status != null) mchQrCode.setStatus(status);
        if(param.getString("minAmount") != null) mchQrCode.setMinAmount(param.getLong("minAmount"));
        if(param.getString("maxAmount") != null) mchQrCode.setMaxAmount(param.getLong("maxAmount"));
        int count = rpcCommonService.rpcMchQrCodeService.update(mchQrCode);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
