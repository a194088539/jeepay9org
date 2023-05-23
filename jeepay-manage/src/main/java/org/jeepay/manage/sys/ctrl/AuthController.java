package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.MenuTreeBuilder;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.CookieUtil;
import org.jeepay.core.common.util.RandomValidateCodeUtil;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.SysResource;
import org.jeepay.core.entity.SysUser;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.secruity.JwtAuthenticationRequest;
import org.jeepay.manage.sys.service.SysUserService;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH)
@RestController
public class AuthController extends BaseController {

    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 登录鉴权
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/auth")
    @MethodLog( remark = "登录" )
    public ResponseEntity<?> auth(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException{
        JSONObject param = getJsonParam(request);
        String username = getStringRequired(param, "username");
        String password = getStringRequired(param, "password");
        JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest(username, password);
        String token;
        try {
           token = sysUserService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
        JSONObject data = new JSONObject();
        data.put("access_token", token);
        // 添加cookie
        Cookie cookie = new Cookie(tokenCookie, token);
        cookie.setPath("/");
        //cookie.setDomain("jeepay.org");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(expiration);// 秒
        response.addCookie(cookie);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String token = CookieUtil.getCookieByName(request, tokenCookie);
        String refreshedToken;
        try {
            refreshedToken = sysUserService.refreshToken(token);
        }catch (ServiceException e) {
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        }
        if(refreshedToken == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        } else {
            JSONObject data = new JSONObject();
            data.put("token", token);
            // 添加cookie
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            //cookie.setDomain("jeepay.org");
            cookie.setMaxAge(expiration);// 秒
            response.addCookie(cookie);
            return ResponseEntity.ok(JeePayResponse.buildSuccess(data));
        }
    }

    /**
     * 修改当前用户登录密码
     * @return
     */
    @RequestMapping("/pwd_update")
    @ResponseBody
    @MethodLog( remark = "修改密码" )
    public ResponseEntity<?> updatePassword(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 旧密码
        String oldRawPassword = getStringRequired(param, "oldPassWord");
        // 新密码
        String rawPassword = getStringRequired(param, "passWord");
        // 验证旧密码是否正确
        SysUser sysUser = rpcCommonService.rpcSysService.findByUserId(getUser().getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldRawPassword, sysUser.getPassWord())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        sysUser = new SysUser();
        sysUser.setUserId(getUser().getId());
        sysUser.setPassWord(encoder.encode(rawPassword));
        sysUser.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查询当前用户信息
     * @return
     */
    @RequestMapping("/current")
    @ResponseBody
    public ResponseEntity<?> current() {
        SysUser sysUser = sysUserService.findByUserId(getUser().getId());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(sysUser));
    }

    /**
     * 查询用户菜单
     * @param request
     * @return
     */
    @RequestMapping("/menu")
    @ResponseBody
    public ResponseEntity<?> menu(HttpServletRequest request) {
        List<SysResource> sysResourceList;
        if(MchConstant.PUB_YES == getUser().getIsSuperAdmin()) {
            // 如果是超级管理用户,则得到所有权限资源
            sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.SYSTEM_MGR);
        }else {
            // 得到用户的权限资源
            sysResourceList = rpcCommonService.rpcSysService.selectResourceByUserId(getUser().getId());
        }
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            MenuTreeBuilder.Node node = new MenuTreeBuilder.Node();
            node.setResourceId(sysResource.getResourceId());
            node.setName(sysResource.getName());
            node.setTitle(sysResource.getTitle());
            if(StringUtils.isNotBlank(sysResource.getJump())) node.setJump(sysResource.getJump());
            if(StringUtils.isNotBlank(sysResource.getIcon())) node.setIcon(sysResource.getIcon());
            node.setParentId(sysResource.getParentId());
            nodeList.add(node);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(JSONArray.parseArray(MenuTreeBuilder.buildTree(nodeList))));
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/auth/auth_code_get")
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map randomMap = RandomValidateCodeUtil.getRandcode(120, 40, 6, 20);
        // TODO 将验证码存储,用于验证
        String randomString = randomMap.get("randomString").toString();
        BufferedImage randomImage = (BufferedImage)randomMap.get("randomImage");
        ImageIO.write(randomImage, "JPEG", response.getOutputStream());
    }

}
