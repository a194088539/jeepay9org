package org.jeepay.manage.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.SysUser;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.secruity.JwtTokenUtil;
import org.jeepay.manage.secruity.JwtUser;

/**
 * Created by aragom on 17/11/28.
 */
@Component
public class SysUserService {

    @Autowired
    private RpcCommonService rpcCommonService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final MyLog _log = MyLog.getLog(SysUserService.class);

    /**
     * 查询
     * @param userId
     * @return
     */
    public SysUser findByUserId(Long userId) {
        return rpcCommonService.rpcSysService.findByUserId(userId);
    }

    public SysUser findByUserName(String userName) {
        return rpcCommonService.rpcSysService.findByUserName(userName);
    }

    public String login(String username, String password) throws ServiceException {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        try{
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e) {
            _log.error(e, "登录失败");
            throw new ServiceException(RetEnum.RET_MGR_LOGIN_FAIL);
        }

        // Reload password post-security so we can generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 查看商户状态
        JwtUser jwtUser = (JwtUser) userDetails;
        Byte status = jwtUser.getStatus();
        if(status == MchConstant.MGR_STATUS_STOP) {
            throw new ServiceException(RetEnum.RET_MGR_USER_STOP);
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
        if(status == MchConstant.MGR_STATUS_STOP) {
            throw new ServiceException(RetEnum.RET_MGR_USER_STOP);
        }
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

}
