package org.jeepay.agent.user.ctrl;

import com.alibaba.fastjson.JSON;
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
import org.jeepay.agent.common.ctrl.BaseController;
import org.jeepay.agent.common.service.RpcCommonService;
import org.jeepay.agent.user.service.UserService;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.MenuTreeBuilder;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.SysResource;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/agent")
@PreAuthorize("hasRole('"+ MchConstant.AGENT_ROLE_NORMAL+"')")
public class AgentController extends BaseController {

    private final static MyLog _log = MyLog.getLog(AgentController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询代理商信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        AgentInfo agentInfo = userService.findByAgentId(getUser().getId());
        // 一些结算配置信息,有可能是集成了系统默认,需要重构下对象
        agentInfo = rpcCommonService.rpcAgentInfoService.reBuildAgentInfoSettConfig(agentInfo);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentInfo));
    }


    /**
     * 查询下级代理商信息
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentInfo agentInfo = getObject(param, AgentInfo.class);
        agentInfo.setParentAgentId(getUser().getId());
        int count = rpcCommonService.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpcCommonService.rpcAgentInfoService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), agentInfo);
        List<JSONObject> objects = new LinkedList<>();
        for(AgentInfo info : agentInfoList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            AgentAccount account = rpcCommonService.rpcAgentAccountService.findByAgentId(info.getAgentId());
            object.put("agentBalance", account.getBalance());  // 账户余额
            objects.add(object);
        }
        Map<String ,Object> ps = new HashMap<String, Object>();
        AgentAccount accountRecord = new AgentAccount();
        accountRecord.setAgentId(agentInfo.getAgentId());
        ps.put("allAgentBalance",  rpcCommonService.rpcAgentAccountService.sumAgentBalance(accountRecord));

        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, ps, count));
    }

    /**
     * 新增代理商信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代理商" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentInfo agentInfo = getObject(param, AgentInfo.class);
        // 设置默认登录密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = MchConstant.MCH_DEFAULT_PASSWORD;
        agentInfo.setPassword(encoder.encode(rawPassword));
        agentInfo.setLastPasswordResetTime(new Date());
        // 设置默认支付密码
        String payPassword = MchConstant.MCH_DEFAULT_PAY_PASSWORD;
        agentInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        // 确认用户名不能重复
        if(rpcCommonService.rpcAgentInfoService.findByUserName(agentInfo.getUserName()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_USERNAME_USED));
        }
        // 确认手机不能重复
        if(rpcCommonService.rpcAgentInfoService.findByMobile(agentInfo.getMobile()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }
        // 确认邮箱不能重复
        if(rpcCommonService.rpcAgentInfoService.findByEmail(agentInfo.getEmail()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }

        // 验证一级代理ID
        if (agentInfo.getParentAgentId() == null) agentInfo.setParentAgentId(0L);
        if(agentInfo.getParentAgentId() != 0) {
            if (rpcCommonService.rpcAgentInfoService.findByAgentId(agentInfo.getParentAgentId()) == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PARENTAGENTID_NOT_EXIST));
            }
            if(rpcCommonService.rpcAgentInfoService.findByAgentId(agentInfo.getParentAgentId()).getAgentLevel() != 1){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PARENTAGENTID_NOT_EXIST));
            }
            agentInfo.setAgentLevel((byte) 2);
        }else{
            agentInfo.setAgentLevel((byte) 1);
        }

        agentInfo.setMinDrawAmount(JEEPayUtil.MIN_SERVICE_CHARGE.longValue()); //最小提现金额。
        agentInfo.setSettMode(MchConstant.SETT_TYPE_D0);  //结算方式默认D0
        agentInfo.setDayDrawTimes(1000); //每次最大申请次数为1000 （不限制）

        int count = rpcCommonService.rpcAgentInfoService.add(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 查询菜单
     * @return
     */
    @RequestMapping("/menu_get")
    @ResponseBody
    public ResponseEntity<?> getMenu() {
        Byte agentLevel = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getId()).getAgentLevel();
        List<SysResource> sysResourceList = rpcCommonService.rpcSysService.selectAllResource(MchConstant.SYSTEM_AGENT);
        List<MenuTreeBuilder.Node> nodeList = new LinkedList<>();
        for(SysResource sysResource : sysResourceList) {
            // 判断是否显示该菜单,一级代理和二级代理不同
            // 用资源表中的property区分,该值为空都可见.否则对应代理等级,如1 表示一级代理可见, 1,2 表示一级、二级代理都可见
            boolean isShow = true;
            String property = sysResource.getProperty();
            if(StringUtils.isNotBlank(property)) {
                isShow = false;
                String[] propertys = property.split(",");
                for(String str : propertys) {
                    if(agentLevel != null && str.equalsIgnoreCase(agentLevel.toString())) {
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
     * 修改代理商信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代理商信息" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(getUser().getId());
        agentInfo.setRemark(param.getString("remark"));
        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 修改登录密码
     * @return
     */
    @RequestMapping("/pwd_update")
    @ResponseBody
    @MethodLog( remark = "修改密码" )
    public ResponseEntity<?> updatePassword(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 旧密码
        String oldRawPassword = getStringRequired(param, "oldPassword");
        // 新密码
        String rawPassword = getStringRequired(param, "password");
        // 验证旧密码是否正确
        AgentInfo agentInfo = userService.findByAgentId(getUser().getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldRawPassword, agentInfo.getPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        agentInfo = new AgentInfo();
        agentInfo.setAgentId(getUser().getId());
        agentInfo.setPassword(encoder.encode(rawPassword));
        agentInfo.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
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
        AgentInfo agentInfo = userService.findByAgentId(getUser().getId());
        if(!MD5Util.string2MD5(oldPayPassword).equals(agentInfo.getPayPassword())) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_OLDPASSWORD_NOT_MATCH));
        }
        // 判断新支付密码格式
        if(!StrUtil.checkPassword(payPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        agentInfo = new AgentInfo();
        agentInfo.setAgentId(getUser().getId());
        agentInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

}