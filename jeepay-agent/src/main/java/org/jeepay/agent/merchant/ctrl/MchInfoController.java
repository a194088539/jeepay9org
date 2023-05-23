package org.jeepay.agent.merchant.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.MD5Util;
import org.jeepay.core.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/mch_info")
public class MchInfoController extends BaseController {

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
        MchInfo mchInfo = new MchInfo();
        mchInfo.setMchId(mchId);
        mchInfo.setAgentId(getUser().getId());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(rpcCommonService.rpcMchInfoService.find(mchInfo)));
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
        mchInfo.setLastPasswordResetTime(new Date());
        // 设置默认支付密码
        String payPassword = MchConstant.MCH_DEFAULT_PAY_PASSWORD;
        mchInfo.setPayPassword(MD5Util.string2MD5(payPassword));
        mchInfo.setRole(MchConstant.MCH_ROLE_NORMAL);
        mchInfo.setAgentId(getUser().getId());              // 设置商户属于的代理商
        mchInfo.setStatus(MchConstant.STATUS_AUDIT_ING);    // 设置代理商增加的商户为待审核状态

        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(getUser().getId());
        if (agentInfo == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
        // 二级代理商增加下级商户
        if (agentInfo.getParentAgentId() != null && agentInfo.getParentAgentId() != 0) {
            mchInfo.setParentAgentId(agentInfo.getParentAgentId());
        }else{
            mchInfo.setParentAgentId(0L);
        }
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
        // 确认唯一标记不能重复
        if(null != mchInfo.getTag() && rpcCommonService.rpcMchInfoService.findByTag(mchInfo.getTag()) != null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TAG_USED));
        }
        int count = rpcCommonService.rpcMchInfoService.register(mchInfo);
        if(count != 1) ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchInfo mchInfo = getObject(param, MchInfo.class);
        mchInfo.setAgentId(getUser().getId());
        int count = rpcCommonService.rpcMchInfoService.count(mchInfo);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchInfo> mchInfoList = rpcCommonService.rpcMchInfoService.select((getPageIndex(param) - 1) * getPageSize(param), getPageSize(param), mchInfo);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchInfoList, count));
    }

    /**
     * 查询账户信息
     * @return
     */
    @RequestMapping("/account_get")
    @ResponseBody
    public ResponseEntity<?> accountGet(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        haveMchPermission(mchId);
        MchAccount mchAccount = rpcCommonService.rpcMchAccountService.findByMchId(mchId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchAccount));
    }

    /**
     * 查看商户的支付通道
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_list")
    @ResponseBody
    public ResponseEntity<?> payPassagelist(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        haveMchPermission(mchId);
        Long agentId = getUser().getId();
        // 得到代理商已经配置的支付通道
        List<AgentPassage> agentPassageList = rpcCommonService.rpcAgentPassageService.selectAllByAgentId(agentId);
        // 支付产品很多时,要考虑内存溢出问题
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll();
        Map<String, PayProduct> payProductMap = new HashMap<>();
        for(PayProduct product : payProductList) {
            payProductMap.put(String.valueOf(product.getId()), product);
        }

        // 得到商户配置的支付通道
        List<MchPayPassage> mchPayPassageList = rpcCommonService.rpcMchPayPassageService.selectAllByMchId(mchId);
        Map<String, MchPayPassage> mchPayPassageMap = new HashMap<>();
        for(MchPayPassage mchPayPassage : mchPayPassageList) {
            mchPayPassageMap.put(String.valueOf(mchPayPassage.getProductId()), mchPayPassage);
        }

        List<JSONObject> objects = new LinkedList<>();
        for(AgentPassage agentPassage : agentPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(agentPassage);
            if(payProductMap.get(String.valueOf(agentPassage.getProductId())) != null) {
                object.put("productName", payProductMap.get(String.valueOf(agentPassage.getProductId())).getProductName());
            }
            if(mchPayPassageMap.get(String.valueOf(agentPassage.getProductId())) != null) {
                object.put("mchRate", mchPayPassageMap.get(String.valueOf(agentPassage.getProductId())).getMchRate());
            }
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

}