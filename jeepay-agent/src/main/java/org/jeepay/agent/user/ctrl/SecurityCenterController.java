package org.jeepay.agent.user.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.jeepay.agent.common.ctrl.BaseController;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.GoogleAuthenticator;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 代理商安全中心
 */
@Controller
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/security")
@PreAuthorize("hasRole('"+ MchConstant.AGENT_ROLE_NORMAL+"')")
public class SecurityCenterController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 得到用户谷歌验证二维码
     * @return
     */
    @RequestMapping("/google_qrcode")
    @ResponseBody
    public ResponseEntity<?> getGoogleAuthQrCode() {
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getId());
        Long mobile = agentInfo.getMobile();
        String googleAuthSecretKey = agentInfo.getGoogleAuthSecretKey();
        if(StringUtils.isBlank(googleAuthSecretKey)){
            googleAuthSecretKey = GoogleAuthenticator.generateSecretKey();
            AgentInfo updateAgentInfo = new AgentInfo();
            updateAgentInfo.setAgentId(agentInfo.getAgentId());
            updateAgentInfo.setGoogleAuthSecretKey(googleAuthSecretKey);
            int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
            if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        String qrcode = GoogleAuthenticator.getQRBarcode("agent("+mobile+")", googleAuthSecretKey);
        String qrcodeUrl = mainConfig.getPayUrl() + "/qrcode_img_get?url=" + qrcode + "&widht=200&height=200";
        return ResponseEntity.ok(JeePayResponse.buildSuccess(qrcodeUrl));
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
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(getUser().getId());
        updateAgentInfo.setGoogleAuthStatus(MchConstant.PUB_YES);
        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
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
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(getUser().getId());
        updateAgentInfo.setLoginSecurityType(loginSecurityType);
        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 设置支付验证方式
     * @return
     */
    @RequestMapping("/pay_set")
    @ResponseBody
    @MethodLog( remark = "设置支付验证方式" )
    public ResponseEntity<?> setPay(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Byte paySecurityType = getByteRequired(param, "paySecurityType");
        Long code = getLongRequired(param, "code");
        if(!checkGoogleCode(getUser().getId(), code)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        if(paySecurityType == 2 || paySecurityType == 3) {    // 判断需要绑定谷歌验证
            AgentInfo queryAgentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getId());
            if(queryAgentInfo.getGoogleAuthStatus() != MchConstant.PUB_YES) {
                // 没有绑定谷歌
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLEAUTH_NOT_BIND));
            }
        }
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(getUser().getId());
        updateAgentInfo.setPaySecurityType(paySecurityType);
        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
        if(count != 1) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 验证谷歌验证码
     * @param agentId
     * @param code
     * @return
     */
    boolean checkGoogleCode(Long agentId, Long code) {
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        String googleAuthSecretKey = agentInfo.getGoogleAuthSecretKey();
        return checkGoogleCode(googleAuthSecretKey, code);
    }

}