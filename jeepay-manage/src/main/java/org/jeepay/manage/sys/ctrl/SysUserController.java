package org.jeepay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.jeepay.core.common.util.MyBase64;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.*;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.sys.service.PermTreeBuilder;
import org.jeepay.manage.sys.service.SysUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/user")
public class SysUserController extends BaseController {

    private final static MyLog _log = MyLog.getLog(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询用户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        SysUser sysUser = sysUserService.findByUserId(userId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(sysUser));
    }

    /**
     * 新增用户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增用户" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysUser sysUser = getObject(param, SysUser.class);
        // 判断密码
        if(!StrUtil.checkPassword(sysUser.getPassWord())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        // 判断用户名是否被使用
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserName(sysUser.getUserName());
        if(querySysUser != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_USERNAME_USED));
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = sysUser.getPassWord();
        sysUser.setPassWord(encoder.encode(rawPassword));
        sysUser.setLastPasswordResetTime(new Date());
        sysUser.setCreateUserId(getUser().getId());
        int count = rpcCommonService.rpcSysService.add(sysUser);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改用户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改用户" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysUser sysUser = getObject(param, SysUser.class);

        if(StringUtils.isBlank(sysUser.getPassWord())) {
            sysUser.setPassWord(null);
        }else {
            String rawPassword = sysUser.getPassWord();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setPassWord(encoder.encode(rawPassword));
            sysUser.setLastPasswordResetTime(new Date());
        }

        // 判断用户名是否被使用
        SysUser querySysUser = rpcCommonService.rpcSysService.findByUserName(sysUser.getUserName());
        if(querySysUser != null && !querySysUser.getUserName().equals(sysUser.getUserName())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_USERNAME_USED));
        }

        int count = rpcCommonService.rpcSysService.update(sysUser);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        SysUser sysUser = getObject(param, SysUser.class);
        int count = rpcCommonService.rpcSysService.count(sysUser);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<SysUser> sysUserList = rpcCommonService.rpcSysService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), sysUser);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(sysUserList, count));
    }

    /**
     * 删除用户信息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除用户" )
    public ResponseEntity<?> delete(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String userIds = getStringRequired(param, "userIds");
        String[] ids = userIds.split(",");
        List<Long> uids = new LinkedList<>();
        for(String userId : ids) {
            if(NumberUtils.isDigits(userId)) uids.add(Long.parseLong(userId));
        }
        rpcCommonService.rpcSysService.batchDelete(uids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查看用户所有角色
     * @param request
     * @return
     */
    @RequestMapping("/user_role_view")
    @ResponseBody
    public ResponseEntity<?> viewPermission(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        // 该用户分配的角色
        List<SysUserRole> sysUserRoleList = rpcCommonService.rpcSysService.selectUserRoleByUserId(userId);
        // 得到所有角色
        List<SysRole> sysRoleList = rpcCommonService.rpcSysService.selectAllRole();
        JSONArray array = new JSONArray();
        for(SysRole sysRole : sysRoleList) {
            JSONObject object = new JSONObject();
            object.put("title", sysRole.getRoleName());
            object.put("value", sysRole.getRoleId());
            object.put("disabled", false);
            object.put("data", new ArrayList<>());
            object.put("checked", false);
            // 设置是否被选中
            for(SysUserRole sysUserRole : sysUserRoleList) {
                if(sysRole.getRoleId().longValue() == sysUserRole.getRoleId().longValue()) {
                    object.put("checked", true);
                    break;
                }
            }
            array.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(array));
    }

    /**
     * 保存用户的角色
     * @param request
     * @return
     */
    @RequestMapping("/user_role_save")
    @ResponseBody
    @MethodLog( remark = "修改用户角色" )
    public ResponseEntity<?> saveUserRole(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long userId = getLongRequired(param, "userId");
        String roleIds = getStringRequired(param, "roleIds");
        String[] ids = roleIds.split(",");
        List<Long> rids = new LinkedList<>();
        for(String roleId : ids) {
            if(NumberUtils.isDigits(roleId)) rids.add(Long.parseLong(roleId));
        }
        rpcCommonService.rpcSysService.saveUserRole(userId, rids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}