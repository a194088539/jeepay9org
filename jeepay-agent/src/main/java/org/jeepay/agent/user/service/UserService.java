package org.jeepay.agent.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.agent.secruity.JwtTokenUtil;
import org.jeepay.agent.secruity.JwtUser;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchInfo;

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

    public AgentInfo findByUserName(String userName) {
        return rpcCommonService.rpcAgentInfoService.findByUserName(userName);
    }

    public AgentInfo findByAgentId(Long agentId) {
        return rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
    }

    public AgentInfo findByLoginName(String loginName) {
        return rpcCommonService.rpcAgentInfoService.findByLoginName(loginName);
    }

    public AgentInfo findByMobile(Long mobile) {
        return rpcCommonService.rpcAgentInfoService.findByMobile(mobile);
    }

    public AgentInfo findByEmail(String email) {
        return rpcCommonService.rpcAgentInfoService.findByEmail(email);
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
