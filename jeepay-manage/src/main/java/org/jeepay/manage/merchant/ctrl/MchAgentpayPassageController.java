package org.jeepay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.AgentAgentpayPassage;
import org.jeepay.core.entity.AgentpayPassage;
import org.jeepay.core.entity.MchAgentpayPassage;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/05
 * @description: 商户代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_agentpay_passage")
public class MchAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        // 得到代理商已经配置的代付通道
        List<AgentAgentpayPassage> agentAgentpayPassageList = new LinkedList<>();
        if(mchInfo.getAgentId() != null) {
            agentAgentpayPassageList = rpcCommonService.rpcAgentAgentpayPassageService.selectAllByAgentId(mchInfo.getAgentId());
        }
        Map<String, AgentAgentpayPassage> agentAgentpayPassageMap = new HashMap<>();
        for(AgentAgentpayPassage agentAgentpayPassage : agentAgentpayPassageList) {
            agentAgentpayPassageMap.put(String.valueOf(agentAgentpayPassage.getAgentpayPassageId()), agentAgentpayPassage);
        }

        // 得到商户已经配置的代付通道
        List<MchAgentpayPassage> mchAgentpayPassageList = rpcCommonService.rpcMchAgentpayPassageService.selectAllByMchId(mchId);
        Map<String, MchAgentpayPassage> mchAgentpayPassageMap = new HashMap<>();
        for(MchAgentpayPassage mchAgentpayPassage : mchAgentpayPassageList) {
            mchAgentpayPassageMap.put(String.valueOf(mchAgentpayPassage.getAgentpayPassageId()), mchAgentpayPassage);
        }
        // 得到所有代付通道
        AgentpayPassage queryAgentpayPassage = new AgentpayPassage();
        queryAgentpayPassage.setStatus(MchConstant.PUB_YES);
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAll(queryAgentpayPassage);
        Map<String, AgentpayPassage> agentpayPassageMap = new HashMap<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            agentpayPassageMap.put(String.valueOf(agentpayPassage.getId()), agentpayPassage);
        }
        // 合并后返回的商户代付通道
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            MchAgentpayPassage mchAgentpayPassage = mchAgentpayPassageMap.get(String.valueOf(agentpayPassage.getId()));
            if(mchAgentpayPassage == null) {
                mchAgentpayPassage = new MchAgentpayPassage();
                mchAgentpayPassage.setMchId(mchId);
                mchAgentpayPassage.setAgentpayPassageId(agentpayPassage.getId());
                mchAgentpayPassage.setStatus(MchConstant.PUB_NO);
                mchAgentpayPassage.setIsDefault(MchConstant.PUB_NO);
            }
            JSONObject object = (JSONObject) JSON.toJSON(mchAgentpayPassage);
            object.put("passageName", agentpayPassage.getPassageName());
            AgentAgentpayPassage agentAgentpayPassage = agentAgentpayPassageMap.get(String.valueOf(agentpayPassage.getId()));
            if(agentAgentpayPassage != null) {
                object.put("agentFeeEvery", agentAgentpayPassage.getFeeEvery());
            }
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(objects));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLongRequired(param, "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }
        Integer agentpayPassageId = getIntegerRequired(param, "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) return ResponseEntity.ok(JeePayResponse.buildSuccess(null));

        AgentAgentpayPassage agentAgentpayPassage = null;
        if(mchInfo.getAgentId() != null) {
            agentAgentpayPassage = rpcCommonService.rpcAgentAgentpayPassageService.findByAgentIdAndPassageId(mchInfo.getAgentId(), agentpayPassageId);
        }
        MchAgentpayPassage mchAgentpayPassage = rpcCommonService.rpcMchAgentpayPassageService.findByMchIdAndAgentpayPassageId(mchId, agentpayPassageId);
        if(mchAgentpayPassage == null) {
            mchAgentpayPassage = new MchAgentpayPassage();
            mchAgentpayPassage.setMchId(mchId);
            mchAgentpayPassage.setAgentpayPassageId(agentpayPassageId);
            mchAgentpayPassage.setIsDefault(MchConstant.PUB_NO);
            mchAgentpayPassage.setStatus(MchConstant.PUB_NO);
        }
        JSONObject object = (JSONObject) JSON.toJSON(mchAgentpayPassage);
        object.put("passageName", agentpayPassage.getPassageName());
        if(agentAgentpayPassage != null) {
            object.put("agentFeeEvery", agentAgentpayPassage.getFeeEvery());
        }
        object.put("agentId", mchInfo.getAgentId() == null ? "" : mchInfo.getAgentId());
        return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "配置商户代付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        // 将参数有字符元转为长整型分
        handleParamAmount(param, "maxEveryAmount", "mchFeeEvery", "inFeeEvery", "onBtbInMaxEveryAmount", "onBtbInMinEveryAmount", "onBtcInMaxEveryAmount", "onBtcInMinEveryAmount");
        MchAgentpayPassage mchAgentpayPassage = getObject(param, MchAgentpayPassage.class);
        // 修改
        int count;
        if(mchAgentpayPassage.getId() != null) {
            // 修改
            mchAgentpayPassage.setMchFeeType((byte) 2);
            checkRequired2(param, "mchFeeEvery");
            if(mchAgentpayPassage.getAgentpayPassageAccountId() == null) mchAgentpayPassage.setAgentpayPassageAccountId(0);
            if(mchAgentpayPassage.getIsDefault() == 1) {
                // 修改该商户其他代付通道为非默认
                MchAgentpayPassage updateMchAgentpayPassage1 = new MchAgentpayPassage();
                updateMchAgentpayPassage1.setIsDefault(MchConstant.PUB_NO);
                rpcCommonService.rpcMchAgentpayPassageService.updateByMchId(updateMchAgentpayPassage1, mchAgentpayPassage.getMchId());
            }
            count = rpcCommonService.rpcMchAgentpayPassageService.update(mchAgentpayPassage);
        }else {
            // 新增
            mchAgentpayPassage.setMchFeeType((byte) 2);
            checkRequired2(param, "mchFeeEvery");
            if(mchAgentpayPassage.getIsDefault() == 1) {
                // 修改该商户其他代付通道为非默认
                MchAgentpayPassage updateMchAgentpayPassage1 = new MchAgentpayPassage();
                updateMchAgentpayPassage1.setIsDefault(MchConstant.PUB_NO);
                rpcCommonService.rpcMchAgentpayPassageService.updateByMchId(updateMchAgentpayPassage1, mchAgentpayPassage.getMchId());
            }
            count = rpcCommonService.rpcMchAgentpayPassageService.add(mchAgentpayPassage);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
