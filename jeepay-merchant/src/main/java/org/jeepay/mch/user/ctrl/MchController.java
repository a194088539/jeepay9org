package org.jeepay.mch.user.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.jeepay.core.common.domain.MenuTreeBuilder;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.core.entity.SysResource;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;
import org.jeepay.mch.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchController extends BaseController {

    private final static MyLog _log = MyLog.getLog(MchController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        MchInfo mchInfo = userService.findByMchId(getUser().getId());
        // 一些结算配置信息,有可能是集成了系统默认,需要重构下对象
        mchInfo = rpcCommonService.rpcMchInfoService.reBuildMchInfoSettConfig(mchInfo);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchInfo));
    }

    /**
     * 查询商户菜单
     * @return
     */
    @RequestMapping("/menu_get")
    @ResponseBody
    public ResponseEntity<?> getMenu() {
        Byte mchType = getUser().getType();
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.SYSTEM_MCH);
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            // 判断是否显示该菜单,平台账户和私有账户显示的菜单可能不同
            // 用资源表中的property区分,该值为空都可见.否则对应商户类型,如1 表示平台账户可见, 1,2 表示平台账户和私有账户都可见
            boolean isShow = true;
            String property = sysResource.getProperty();
            if(StringUtils.isNotBlank(property)) {
                isShow = false;
                String[] propertys = property.split(",");
                for(String str : propertys) {
                    if(str.equalsIgnoreCase(mchType.toString())) {
                        isShow = true;
                        break;
                    }
                }
            }
            if(!isShow) continue;
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
     * 修改商户信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户信息" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getId());
        mchInfo.setRemark(param.getString("remark"));
        int count = userService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 修改登录密码
     * @return
     */
    @RequestMapping("/pwd_update")
    @ResponseBody
    @MethodLog( remark = "修改登录密码" )
    public ResponseEntity<?> updatePassword(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 旧密码
        String oldRawPassword = getStringRequired(param, "oldPassword");
        // 新密码
        String rawPassword = getStringRequired(param, "password");
        // 验证旧密码是否正确
        MchInfo mchInfo = userService.findByMchId(getUser().getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldRawPassword, mchInfo.getPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getId());
        mchInfo.setPassword(encoder.encode(rawPassword));
        mchInfo.setLastPasswordResetTime(new Date());
        int count = userService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 修改支付密码
     * @return
     */
    @RequestMapping("/paypwd_update")
    @ResponseBody
    @MethodLog( remark = "修改支付密码" )
    public ResponseEntity<?> updatePayPassword(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 旧支付密码
        String oldPayPassword = getStringRequired(param, "oldPayPassword");
        // 新支付密码
        String payPassword = getStringRequired(param, "payPassword");
        // 验证旧支付密码
        MchInfo mchInfo = userService.findByMchId(getUser().getId());
        if(!MD5Util.string2MD5(oldPayPassword).equals(mchInfo.getPayPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新支付密码格式
        if(!StrUtil.checkPassword(payPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getId());
        mchInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        int count = userService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 修改私钥(需要验证谷歌验证码)
     * @return
     */
    @RequestMapping("/key_update")
    @ResponseBody
    @MethodLog( remark = "修改商户私钥" )
    public ResponseEntity<?> updateKey(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 私钥
        String privateKey = getStringRequired(param, "privateKey");
        // 获取传的验证码
        Long code = getLongRequired(param, "code");
        MchInfo mchInfo = userService.findByMchId(getUser().getId());
        if(!checkGoogleCode(mchInfo.getGoogleAuthSecretKey(), code)) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_GOOGLECODE_NOT_MATCH));
        /*// 支付密码
        String payPassword = getStringRequired(param, "payPassword");
        // 验证旧密码是否正确
        MchInfo mchInfo = userService.findByMchId(getUser().getId());
        if(!MD5Util.string2MD5(payPassword).equals(mchInfo.getPayPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PAP_PASSWORD_NOT_MATCH));
        }*/
        // 修改密钥
        mchInfo = new MchInfo();
        mchInfo.setMchId(getUser().getId());
        mchInfo.setPrivateKey(privateKey);
        int count = userService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

}