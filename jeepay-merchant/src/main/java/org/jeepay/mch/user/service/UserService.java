package org.jeepay.mch.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.common.service.RpcCommonService;
import org.jeepay.mch.secruity.JwtTokenUtil;
import org.jeepay.mch.secruity.JwtUser;

import java.util.Date;

/**
 * Created by aragom on 17/11/28.
 */
@Component
public class UserService {

    @Autowired
    private RpcCommonService rpcCommonService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final MyLog _log = MyLog.getLog(UserService.class);

    /**
     * 注册商户(申请)
     * @param mchInfo
     * @return
     */
    public int register(MchInfo mchInfo) {
        mchInfo.setStatus(MchConstant.STATUS_AUDIT_ING);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = mchInfo.getPassword();
        mchInfo.setPassword(encoder.encode(rawPassword));
        mchInfo.setLastPasswordResetTime(new Date());
        mchInfo.setRole(MchConstant.MCH_ROLE_NO);    // 注册时默认角色

        MchInfo usedMchInfo = rpcCommonService.rpcMchInfoService.findByMobile(mchInfo.getMobile());
        if(usedMchInfo != null) {
            throw new ServiceException(RetEnum.RET_MCH_MOBILE_USED);
        }

        usedMchInfo = rpcCommonService.rpcMchInfoService.findByEmail(mchInfo.getEmail());
        if(usedMchInfo != null) {
            throw new ServiceException(RetEnum.RET_MCH_EMAIL_USED);
        }

        return rpcCommonService.rpcMchInfoService.register(mchInfo);
    }

    /**
     * 修改信息
     * @param mchInfo
     * @return
     */
    public int update(MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.update(mchInfo);
    }

    /**
     * 查询
     * @param mchId
     * @return
     */
    public MchInfo findByMchId(Long mchId) {
        return rpcCommonService.rpcMchInfoService.findByMchId(mchId);
    }

    public MchInfo findByLoginName(String loginName) {
        return rpcCommonService.rpcMchInfoService.findByLoginName(loginName);
    }

    public MchInfo findByMobile(Long mobile) {
        return rpcCommonService.rpcMchInfoService.findByMobile(mobile);
    }

    public MchInfo findByEmail(String email) {
        return rpcCommonService.rpcMchInfoService.findByEmail(email);
    }

    public String login(String username, String password) throws ServiceException {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        try{
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e) {
            _log.error(e, "鉴权失败");
            throw new ServiceException(RetEnum.RET_MCH_AUTH_FAIL);
        }

        // Reload password post-security so we can generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 查看商户状态
        JwtUser jwtUser = (JwtUser) userDetails;
        Byte status = jwtUser.getStatus();
        if(status == MchConstant.STATUS_AUDIT_ING) {
            throw new ServiceException(RetEnum.RET_MCH_STATUS_AUDIT_ING);
        }else if(status == MchConstant.STATUS_STOP) {
            throw new ServiceException(RetEnum.RET_MCH_STATUS_STOP);
        }

        String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    public String refreshToken(String oldToken) {
        String token = oldToken;
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        // 查看商户状态
        Byte status = user.getStatus();
        if(status == MchConstant.STATUS_AUDIT_ING) {
            throw new ServiceException(RetEnum.RET_MCH_STATUS_AUDIT_ING);
        }else if(status == MchConstant.STATUS_STOP) {
            throw new ServiceException(RetEnum.RET_MCH_STATUS_STOP);
        }

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

}
