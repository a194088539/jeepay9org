package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
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
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.entity.AgentInfo;
import org.jeepay.core.entity.MchAccount;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.merchant.service.MchInfoService;
import org.jeepay.manage.secruity.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_info")
public class MchInfoController extends BaseController {

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询商户信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchInfo));
    }

    /**
     * 新增商户信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增商户" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = getObject(param, MchInfo.class);
        // 设置默认登录密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = MchConstant.MCH_DEFAULT_PASSWORD;
        mchInfo.setPassword(encoder.encode(rawPassword));
        mchInfo.setRole(MchConstant.MCH_ROLE_NORMAL);
        mchInfo.setLastPasswordResetTime(new Date());
        // 设置默认支付密码
        String payPassword = MchConstant.MCH_DEFAULT_PAY_PASSWORD;
        mchInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        // 确认手机不能重复
        if(rpcCommonService.rpcMchInfoService.findByMobile(mchInfo.getMobile()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }
        // 确认邮箱不能重复
        if(rpcCommonService.rpcMchInfoService.findByEmail(mchInfo.getEmail()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }
        // 确认用户名不能重复
        if(rpcCommonService.rpcMchInfoService.findByUserName(mchInfo.getUserName()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_USERNAME_USED));
        }
        // 判断代理商是否正确
        if(mchInfo.getAgentId() != null) {
            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(mchInfo.getAgentId());
            if(agentInfo == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
            }
            if(agentInfo.getStatus() != MchConstant.PUB_YES) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_STATUS_STOP));
            }
            if (agentInfo.getAgentLevel() == 1){
                mchInfo.setAgentId(agentInfo.getAgentId());
                mchInfo.setParentAgentId(0L);
            }else if (agentInfo.getAgentLevel() == 2){
                mchInfo.setAgentId(agentInfo.getAgentId());
                if (agentInfo.getParentAgentId() != null && agentInfo.getParentAgentId() != 0){
                    mchInfo.setParentAgentId(agentInfo.getParentAgentId());
                }
            }

            // 判断充值费率不能小于代理充值费率
            if(mchInfo.getOffRechargeRate() != null) {
                if(agentInfo.getOffRechargeRate() == null) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "请先配置代理商线下充值费率"));
                }
                if(mchInfo.getOffRechargeRate().compareTo(agentInfo.getOffRechargeRate()) == -1) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "商户充值费率不能低于代理商(" + agentInfo.getOffRechargeRate() + "%)"));
                }
            }
        }
        // 确认唯一标记不能重复
        if(null != mchInfo.getTag() && rpcCommonService.rpcMchInfoService.findByTag(mchInfo.getTag()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TAG_USED));
        }
        // 生成商户默认的私钥
        mchInfo.setPrivateKey(RandomStringUtils.randomAlphanumeric(128).toUpperCase());

        int count = rpcCommonService.rpcMchInfoService.add(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
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
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = getObject(param, MchInfo.class);
        MchInfo dbMchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(dbMchInfo == null) {
            ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if(mchInfo.getMobile() != null && mchInfo.getMobile().longValue() != dbMchInfo.getMobile().longValue()) {
            // 确认手机不能重复
            if(rpcCommonService.rpcMchInfoService.findByMobile(mchInfo.getMobile()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
            }
        }

        if(mchInfo.getEmail() != null && !mchInfo.getEmail().equalsIgnoreCase(dbMchInfo.getEmail())) {
            // 确认邮箱不能重复
            if(rpcCommonService.rpcMchInfoService.findByEmail(mchInfo.getEmail()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
            }
        }
        if(mchInfo.getUserName() != null && !mchInfo.getUserName().equalsIgnoreCase(dbMchInfo.getUserName())) {
            // 确认用户名不能重复
            if(rpcCommonService.rpcMchInfoService.findByUserName(mchInfo.getUserName()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_USERNAME_USED));
            }
        }
        // 确认唯一标记不能重复
        if(mchInfo.getTag() != null && !mchInfo.getTag().equalsIgnoreCase(dbMchInfo.getTag())) {
            if(rpcCommonService.rpcMchInfoService.findByTag(mchInfo.getTag()) != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TAG_USED));
            }
        }

        // 商户信息修改
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setMchId(mchId);
        updateMchInfo.setMobile(mchInfo.getMobile());
        updateMchInfo.setEmail(mchInfo.getEmail());
        updateMchInfo.setUserName(mchInfo.getUserName());
        updateMchInfo.setRealName(mchInfo.getRealName());
        updateMchInfo.setQq(mchInfo.getQq());
        updateMchInfo.setIdCard(mchInfo.getIdCard());
        updateMchInfo.setSiteName(mchInfo.getSiteName());
        updateMchInfo.setSiteUrl(mchInfo.getSiteUrl());
        updateMchInfo.setMchRate(mchInfo.getMchRate());
        updateMchInfo.setSettType(mchInfo.getSettType());
        updateMchInfo.setStatus(mchInfo.getStatus());
        updateMchInfo.setAgentId(mchInfo.getAgentId());
        updateMchInfo.setAddress(mchInfo.getAddress());
        updateMchInfo.setTag(mchInfo.getTag() == null ? "" : mchInfo.getTag());
        updateMchInfo.setOffRechargeRate((mchInfo.getOffRechargeRate()));
        updateMchInfo.setBankName(mchInfo.getBankName());
        updateMchInfo.setBankNetName(mchInfo.getBankNetName());
        updateMchInfo.setAccountName(mchInfo.getAccountName());
        updateMchInfo.setAccountNo(mchInfo.getAccountNo());
        updateMchInfo.setProvince(mchInfo.getProvince());
        updateMchInfo.setCity(mchInfo.getCity());
        updateMchInfo.setLoginSecurityType(mchInfo.getLoginSecurityType());
        updateMchInfo.setPaySecurityType(mchInfo.getPaySecurityType());
        updateMchInfo.setLoginWhiteIp(replace(mchInfo.getLoginWhiteIp()));
        updateMchInfo.setLoginBlackIp(replace(mchInfo.getLoginBlackIp()));
        updateMchInfo.setPayWhiteIp(replace(mchInfo.getPayWhiteIp()));
        updateMchInfo.setPayBlackIp(replace(mchInfo.getPayBlackIp()));
        updateMchInfo.setAgentpayWhiteIp(replace(mchInfo.getAgentpayWhiteIp()));
        updateMchInfo.setAgentpayBlackIp(replace(mchInfo.getAgentpayBlackIp()));
        // 如果登录密码不为空,则修改登录密码
        String rawPassword = mchInfo.getPassword();
        if(StringUtils.isNotBlank(rawPassword)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            // 判断新登录密码格式
            if(!StrUtil.checkPassword(rawPassword)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
            }
            updateMchInfo.setPassword(encoder.encode(rawPassword));
            updateMchInfo.setLastPasswordResetTime(new Date());
        }
        // 如果支付密码不为空,则修改支付密码
        String payPassword = mchInfo.getPayPassword();
        if(StringUtils.isNotBlank(payPassword)) {
            // 判断新支付密码格式
            if(!StrUtil.checkPassword(payPassword)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
            }
            updateMchInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        }
        // 判断代理商是否正确
        if(mchInfo.getAgentId() != null && dbMchInfo.getAgentId() != mchInfo.getAgentId()) {
            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(updateMchInfo.getAgentId());
            if(agentInfo == null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
            }
            if(agentInfo.getStatus() != MchConstant.PUB_YES) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_STATUS_STOP));
            }
            if (agentInfo.getAgentLevel() == 1){
                updateMchInfo.setAgentId(agentInfo.getAgentId());
                updateMchInfo.setParentAgentId(0L);
            }else if (agentInfo.getAgentLevel() == 2){
                updateMchInfo.setAgentId(agentInfo.getAgentId());
                if (agentInfo.getParentAgentId() != null){
                    updateMchInfo.setParentAgentId(agentInfo.getParentAgentId());
                }
            }

            // 判断充值费率不能小于代理充值费率
            if(mchInfo.getOffRechargeRate() != null) {
                if(agentInfo.getOffRechargeRate() == null) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "请先配置代理商线下充值费率"));
                }
                if(mchInfo.getOffRechargeRate().compareTo(agentInfo.getOffRechargeRate()) == -1) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "商户充值费率不能低于代理商(" + agentInfo.getOffRechargeRate().floatValue() + "%)"));
                }
            }

        }
        int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改商户结算设置
     * @return
     */
    @RequestMapping("/sett_update")
    @ResponseBody
    @MethodLog( remark = "修改商户结算信息" )
    public ResponseEntity<?> updateSett(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        handleParamAmount(param, "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount", "drawFeeLimit");
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = getObject(param, MchInfo.class);
        // 修改结算设置信息
        MchInfo updateMchInfo = new MchInfo();
        updateMchInfo.setMchId(mchId);
        updateMchInfo.setSettConfigMode(mchInfo.getSettConfigMode());
        if(mchInfo.getSettConfigMode() == 2) {    // 自定义
            updateMchInfo.setDrawFlag(mchInfo.getDrawFlag());                   // 提现开关
            updateMchInfo.setAllowDrawWeekDay(mchInfo.getAllowDrawWeekDay());   // 每周几允许提现
            updateMchInfo.setDrawDayStartTime(mchInfo.getDrawDayStartTime());   // 每日提现开始时间
            updateMchInfo.setDrawDayEndTime(mchInfo.getDrawDayEndTime());       // 每日提现结束时间
            updateMchInfo.setDayDrawTimes(mchInfo.getDayDrawTimes());           // 每日最多提现次数
            checkRequired(param, "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount");
            updateMchInfo.setDrawMaxDayAmount(mchInfo.getDrawMaxDayAmount());   // 每日提现最多金额
            updateMchInfo.setMaxDrawAmount(mchInfo.getMaxDrawAmount());         // 每笔提现最大金额
            updateMchInfo.setMinDrawAmount(mchInfo.getMinDrawAmount());         // 每笔提现最小金额
            updateMchInfo.setFeeType(mchInfo.getFeeType());                     // 结算手续费类型
            if(mchInfo.getFeeType() == 1) {
                // 百分比收费
                updateMchInfo.setFeeRate(mchInfo.getFeeRate());
            }else if(mchInfo.getFeeType() == 2) {
                // 固定收费
                Long feeLevelL = getRequiredAmountL(param, "feeLevel");
                updateMchInfo.setFeeLevel(String.valueOf(feeLevelL));
            }
            updateMchInfo.setDrawFeeLimit(mchInfo.getDrawFeeLimit());           // 单笔手续费上限
            updateMchInfo.setSettType(mchInfo.getSettType());                   // 结算类型
            updateMchInfo.setSettMode(mchInfo.getSettMode());                   // 结算方式
        }
        int count = rpcCommonService.rpcMchInfoService.update(updateMchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = getObject(param, MchInfo.class);
        int count = mchInfoService.countNormal(mchInfo);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchInfo> mchInfoList = mchInfoService.getMchNormalInfoList((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), mchInfo);
        List<JSONObject> objects = new LinkedList<>();
        for(MchInfo info : mchInfoList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("loginMchUrl", buildLoginMchUrl(info));  // 生成登录地址
            MchAccount mchAccount = rpcCommonService.rpcMchAccountService.findByMchId(info.getMchId());
            object.put("balance", mchAccount.getBalance());
            object.put("agentpayBalance", mchAccount.getAgentpayBalance());
            object.put("frozenMoney", mchAccount.getFrozenMoney());
            object.put("securityMoney", mchAccount.getSecurityMoney());
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    /**
     * 登录商户
     * @return
     */
    @RequestMapping("/mch_login")
    @ResponseBody
    @MethodLog( remark = "模拟商户登录" )
    public ResponseEntity<?> loginMch(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        // 将商户ID+商户密码+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String password = mchInfo.getPassword();
        String secret = "Abc%$G&!!!128G";
        String rawToken = mchId + password + secret;
        String token = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginMchUrl = mainConfig.getLoginMchUrl();
        loginMchUrl = String.format(loginMchUrl, mchId, token);
        JSONObject object = new JSONObject();
        object.put("mchId", mchId);
        object.put("token", token);
        object.put("loginMchUrl", loginMchUrl);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    /**
     * 重置商户密码
     * @return
     */
    @RequestMapping("/pwd_reset")
    @ResponseBody
    @MethodLog( remark = "重置商户密码" )
    public ResponseEntity<?> resetPwd(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        String rawPassword = getStringRequired(param, "password");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(mchId);
        mchInfo.setPassword(encoder.encode(rawPassword));
        mchInfo.setLastPasswordResetTime(new Date());
        int count = rpcCommonService.rpcMchInfoService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    /**
     * 解绑谷歌验证
     * @return
     */
    @RequestMapping("/google_untie")
    @ResponseBody
    @MethodLog( remark = "解绑谷歌验证" )
    public ResponseEntity<?> untieGoogleAuth(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(mchId);
        mchInfo.setGoogleAuthStatus(MchConstant.PUB_NO);
        mchInfo.setGoogleAuthSecretKey("");
        int count = rpcCommonService.rpcMchInfoService.update(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(JeePayResponse.buildSuccess());
    }

    private String buildLoginMchUrl(MchInfo mchInfo) {
        // 将商户ID+商户密码+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String password = mchInfo.getPassword();
        String secret = "Abc%$G&!!!128G";
        String rawToken = mchInfo.getMchId() + password + secret;
        String token = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginMchUrl = mainConfig.getLoginMchUrl();
        return String.format(loginMchUrl, mchInfo.getMchId(), token);
    }

    private String replace(String str) {
        if(StringUtils.isBlank(str)) return str;
        str = StrUtil.toSemiangle(str);
        str = str.replaceAll(System.getProperty("line.separator"), "").replaceAll(" ", "");
        return str;
    }
}