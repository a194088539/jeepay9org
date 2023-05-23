package org.jeepay.manage.agent.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.AgentAccount;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_info")
public class AgentInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询代理商信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(agentInfo));
    }

    /**
     * 查询一级、二级代理商ID
     * @return
     */
    @RequestMapping("/getParentAgentId")
    @ResponseBody
    public ResponseEntity<?> getParentAgentId(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Byte agentLevel = getByte(param, "agentLevel");
        AgentInfo agentInfo = new AgentInfo();
        if (agentLevel != null) {
            agentInfo.setAgentLevel(agentLevel);
        }
        agentInfo.setStatus(MchConstant.PUB_YES);
        List<AgentInfo> list = rpcCommonService.rpcAgentInfoService.selectAll(agentInfo);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(list));
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

        int count = rpcCommonService.rpcAgentInfoService.add(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改代理商信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代理商" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        AgentInfo agentInfo = getObject(param, AgentInfo.class);
        AgentInfo dbAgentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        if(dbAgentInfo == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if(agentInfo.getMobile() != null && agentInfo.getMobile().longValue() != dbAgentInfo.getMobile().longValue()) {
            // 确认手机不能重复
            if(rpcCommonService.rpcAgentInfoService.findByMobile(agentInfo.getMobile()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
            }
        }

        if(agentInfo.getEmail() != null && !agentInfo.getEmail().equalsIgnoreCase(dbAgentInfo.getEmail())) {
            // 确认邮箱不能重复
            if(rpcCommonService.rpcAgentInfoService.findByEmail(agentInfo.getEmail()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
            }
        }

        //判断上级代理商
        if (agentInfo.getParentAgentId() != null && agentInfo.getParentAgentId() != 0){
            AgentInfo parentAgentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentInfo.getParentAgentId());
            if (parentAgentInfo == null) return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
        }

        // 商户信息只允许修改手机号,邮箱,费率,结算方式,状态,其他不允许修改
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(agentId);
        updateAgentInfo.setParentAgentId(agentInfo.getParentAgentId());
        updateAgentInfo.setAgentLevel(agentInfo.getAgentLevel());
        updateAgentInfo.setAgentName(agentInfo.getAgentName());
        updateAgentInfo.setMobile(agentInfo.getMobile());
        updateAgentInfo.setEmail(agentInfo.getEmail());
        updateAgentInfo.setSettType(agentInfo.getSettType());
        updateAgentInfo.setStatus(agentInfo.getStatus());
        updateAgentInfo.setRealName(agentInfo.getRealName());
        updateAgentInfo.setIdCard(agentInfo.getIdCard());
        updateAgentInfo.setTel(agentInfo.getTel());
        updateAgentInfo.setQq(agentInfo.getQq());
        updateAgentInfo.setAddress(agentInfo.getAddress());
        updateAgentInfo.setOffRechargeRate(agentInfo.getOffRechargeRate());
        updateAgentInfo.setBankName(agentInfo.getBankName());
        updateAgentInfo.setBankNetName(agentInfo.getBankNetName());
        updateAgentInfo.setAccountName(agentInfo.getAccountName());
        updateAgentInfo.setAccountNo(agentInfo.getAccountNo());
        updateAgentInfo.setProvince(agentInfo.getProvince());
        updateAgentInfo.setCity(agentInfo.getCity());
        updateAgentInfo.setLoginSecurityType(agentInfo.getLoginSecurityType());
        updateAgentInfo.setPaySecurityType(agentInfo.getPaySecurityType());
        // 如果登录密码不为空,则修改登录密码
        String rawPassword = agentInfo.getPassword();
        if(StringUtils.isNotBlank(rawPassword)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // 判断新登录密码格式
            if(!StrUtil.checkPassword(rawPassword)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
            }
            updateAgentInfo.setPassword(encoder.encode(rawPassword));
            updateAgentInfo.setLastPasswordResetTime(new Date());
        }
        // 如果支付密码不为空,则修改支付密码
        String payPassword = agentInfo.getPayPassword();
        if(StringUtils.isNotBlank(payPassword)) {
            // 判断新支付密码格式
            if(!StrUtil.checkPassword(payPassword)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
            }
            updateAgentInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        }
        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改结算设置
     * @return
     */
    @RequestMapping("/sett_update")
    @ResponseBody
    @MethodLog( remark = "修改代理商结算信息" )
    public ResponseEntity<?> updateSett(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount", "drawFeeLimit");
        Long agentId = getLongRequired(param, "agentId");
        AgentInfo agentInfo = getObject(param, AgentInfo.class);
        // 修改结算设置信息
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(agentId);

        updateAgentInfo.setSettConfigMode(agentInfo.getSettConfigMode());
        if(agentInfo.getSettConfigMode() == 2) {    // 自定义
            updateAgentInfo.setDrawFlag(agentInfo.getDrawFlag());                   // 提现开关
            updateAgentInfo.setAllowDrawWeekDay(agentInfo.getAllowDrawWeekDay());   // 每周几允许提现
            updateAgentInfo.setDrawDayStartTime(agentInfo.getDrawDayStartTime());   // 每日提现开始时间
            updateAgentInfo.setDrawDayEndTime(agentInfo.getDrawDayEndTime());       // 每日提现结束时间
            updateAgentInfo.setDayDrawTimes(agentInfo.getDayDrawTimes());           // 每日最多提现次数
            checkRequired(param, "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount");
            updateAgentInfo.setDrawMaxDayAmount(agentInfo.getDrawMaxDayAmount());   // 每日提现最多金额
            updateAgentInfo.setMaxDrawAmount(agentInfo.getMaxDrawAmount());         // 每笔提现最大金额
            updateAgentInfo.setMinDrawAmount(agentInfo.getMinDrawAmount());         // 每笔提现最小金额
            updateAgentInfo.setFeeType(agentInfo.getFeeType());                     // 结算手续费类型
            if(agentInfo.getFeeType() == 1) {
                // 百分比收费
                updateAgentInfo.setFeeRate(agentInfo.getFeeRate());
            }else if(agentInfo.getFeeType() == 2) {
                // 固定收费
                Long feeLevelL = getRequiredAmountL(param, "feeLevel");
                updateAgentInfo.setFeeLevel(String.valueOf(feeLevelL));
            }
            updateAgentInfo.setDrawFeeLimit(agentInfo.getDrawFeeLimit());           // 单笔手续费上限
            updateAgentInfo.setSettType(agentInfo.getSettType());                   // 结算类型
            updateAgentInfo.setSettMode(agentInfo.getSettMode());                   // 结算方式
        }
        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }    

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        AgentInfo agentInfo = getObject(param, AgentInfo.class);
        int count = rpcCommonService.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpcCommonService.rpcAgentInfoService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), agentInfo);
        List<JSONObject> objects = new LinkedList<>();
        for(AgentInfo info : agentInfoList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("loginAgentUrl", buildLoginAgentUrl(info));  // 生成登录地址
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
     * 重置商户密码
     * @return
     */
    @RequestMapping("/pwd_reset")
    @ResponseBody
    @MethodLog( remark = "重置代理商密码" )
    public ResponseEntity<?> resetPwd(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        String rawPassword = getStringRequired(param, "password");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(agentId);
        agentInfo.setPassword(encoder.encode(rawPassword));
        agentInfo.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 解绑谷歌验证
     * @return
     */
    @RequestMapping("/google_untie")
    @ResponseBody
    @MethodLog( remark = "解绑代理商谷歌验证" )
    public ResponseEntity<?> untieGoogleAuth(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long agentId = getLongRequired(param, "agentId");
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(agentId);
        agentInfo.setGoogleAuthStatus(MchConstant.PUB_NO);
        agentInfo.setGoogleAuthSecretKey("");
        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    private String buildLoginAgentUrl(AgentInfo agentInfo) {
        // 将商户ID+商户密码+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String password = agentInfo.getPassword();
        String secret = "Abc%$G&!!!128G";
        String rawToken = agentInfo.getAgentId() + password + secret;
        String token = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginAgentUrl = mainConfig.getLoginAgentUrl();
        return String.format(loginAgentUrl, agentInfo.getAgentId(), token);
    }


}