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
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.GoogleAuthenticator;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

/**
 * 商户安全中心
 */
@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/security")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class SecurityCenterController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(SecurityCenterController.class);

    /**
     * 得到用户谷歌验证二维码
     * @return
     */
    @RequestMapping("/google_qrcode")
    @ResponseBody
    public ResponseEntity<?> getGoogleAuthQrCode() {
        _log.info("接收获取谷歌验证码请求, mchId={}", getUser().getId());
        try {
            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
            Long mobile = mchInfo.getMobile();
            String googleAuthSecretKey = mchInfo.getGoogleAuthSecretKey();
            _log.info("查询到商户googleAuthSecretKey={}", googleAuthSecretKey);
            if(StringUtils.isBlank(googleAuthSecretKey)){
                googleAuthSecretKey = GoogleAuthenticator.generateSecretKey();
                MchInfo updateMchInfo = new MchInfo();
                updateMchInfo.setMchId(mchInfo.getMchId());
                updateMchInfo.setGoogleAuthSecretKey(googleAuthSecretKey);
                int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
                if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
            }
            String qrcode = GoogleAuthenticator.getQRBarcode("mch("+mobile+")", googleAuthSecretKey);
            String qrcodeUrl = mainConfig.getPayUrl() + "/qrcode_img_get?url=" + qrcode + "&widht=200&height=200";
            _log.info("生成谷歌绑定二维码Url={}", qrcodeUrl);
            return ResponseEntity.ok(JeePayResponse.buildSuccess(qrcodeUrl));
        }catch (Exception e) {
            _log.error(e, "生成谷歌绑定二维码异常");
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
    }

    /**
     * 绑定谷歌验证
     * @return
     */
    @RequestMapping("/google_bind")
    @ResponseBody
    public ResponseEntity<?> bindGoogleAuth(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 获取传的验证码
        Long code = getLongRequired(param, "code");
        if(!checkGoogleCode(getUser().getId(), code)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        // 修改数据中绑定状态
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setMchId(getUser().getId());
        updateMchInfo.setGoogleAuthStatus(MchConstant.PUB_YES);
        int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MCH_GOOGLEAUTH_SECRETKEY_BIND_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 设置登录验证方式
     * @return
     */
    @RequestMapping("/login_set")
    @ResponseBody
    @MethodLog( remark = "设置登录验证方式" )
    public ResponseEntity<?> setLogin(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Byte loginSecurityType = getByteRequired(param, "loginSecurityType");
        Long code = getLongRequired(param, "code");
        if(!checkGoogleCode(getUser().getId(), code)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        if(loginSecurityType == 1) {    // 登录密码+谷歌验证组合
            MchInfo queryMchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
            if(queryMchInfo.getGoogleAuthStatus() != MchConstant.PUB_YES) {
                // 没有绑定谷歌
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLEAUTH_NOT_BIND));
            }
        }
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setMchId(getUser().getId());
        updateMchInfo.setLoginSecurityType(loginSecurityType);
        int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 设置支付验证方式
     * @return
     */
    @MethodLog( remark = "设置支付验证方式" )
    @RequestMapping("/pay_set")
    @ResponseBody
    public ResponseEntity<?> setPay(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Byte paySecurityType = getByteRequired(param, "paySecurityType");
        Long code = getLongRequired(param, "code");
        if(!checkGoogleCode(getUser().getId(), code)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        if(paySecurityType == 2 || paySecurityType == 3) {    // 判断需要绑定谷歌验证
            MchInfo queryMchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
            if(queryMchInfo.getGoogleAuthStatus() != MchConstant.PUB_YES) {
                // 没有绑定谷歌
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLEAUTH_NOT_BIND));
            }
        }
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setMchId(getUser().getId());
        updateMchInfo.setPaySecurityType(paySecurityType);
        int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 验证谷歌验证码
     * @param mchId
     * @param code
     * @return
     */
    boolean checkGoogleCode(Long mchId, Long code) {
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        String googleAuthSecretKey = mchInfo.getGoogleAuthSecretKey();
        return checkGoogleCode(googleAuthSecretKey, code);
    }

}