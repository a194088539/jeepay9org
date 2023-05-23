package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.PayInterface;
import org.jeepay.core.entity.PayInterfaceType;
import org.jeepay.core.entity.PayPassage;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/05/04
 * @description: 支付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_passage")
public class PayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassage payPassage = getObject(param, PayPassage.class);
        int count = rpcCommonService.rpcPayPassageService.count(payPassage);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), payPassage);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // 支付类型Map
        Map payTypeMap = commonConfigService.getPayTypeMap();

        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(PayPassage info : payPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("payTypeName", payTypeMap.get(info.getPayType()));               // 转换支付类型名称
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            objects.add(object);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(payPassage));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassage payPassage = getObject(param, PayPassage.class);
        String ifCode = payPassage.getIfCode();
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
        }
        payPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型

        int count = rpcCommonService.rpcPayPassageService.update(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/risk_update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道风控" )
    public ResponseEntity<?> updateRisk(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassage payPassage = getObject(param, PayPassage.class);
        Long maxDayAmount = payPassage.getMaxDayAmount();
        Long maxEveryAmount = payPassage.getMaxEveryAmount();
        Long minEveryAmount = payPassage.getMinEveryAmount();
        // 元转分
        if(maxDayAmount != null) payPassage.setMaxDayAmount(maxDayAmount * 100);
        if(maxEveryAmount != null) payPassage.setMaxEveryAmount(maxEveryAmount * 100);
        if(minEveryAmount != null) payPassage.setMinEveryAmount(minEveryAmount * 100);
        int count = rpcCommonService.rpcPayPassageService.update(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/rate_update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道费率" )
    public ResponseEntity<?> updateRate(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        String passageRate = getStringRequired(param, "passageRate");
        PayPassage payPassage = new PayPassage();
        payPassage.setId(id);
        payPassage.setPassageRate(new BigDecimal(passageRate));
        int count = rpcCommonService.rpcPayPassageService.updateRate(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付通道" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        PayPassage payPassage = getObject(param, PayPassage.class);
        String ifCode = payPassage.getIfCode();
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface == null) {
            ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
        }
        payPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型
        int count = rpcCommonService.rpcPayPassageService.add(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 根据支付通道ID,获取支付账号配置定义描述
     * @param request
     * @return
     */
    @RequestMapping("/pay_config_get")
    @ResponseBody
    public ResponseEntity<?> getPayConfig(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer payPassageId = getIntegerRequired(param, "payPassageId");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(payPassageId);
        if(payPassage == null) {
            return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        String ifCode = payPassage.getIfCode();
        String ifTypeCode = payPassage.getIfTypeCode();

        // 如果接口配置了定义描述,则使用接口
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface != null && StringUtils.isNotBlank(payInterface.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterface);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterface.getIfTypeCode()));
            return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
        }
        // 使用接口类型配置的定义描述
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        if(payInterfaceType != null && StringUtils.isNotBlank(payInterfaceType.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterfaceType);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterfaceType.getIfTypeCode()));
            return ResponseEntity.ok(JeePayResponse.buildSuccess(object));
        }
        return ResponseEntity.ok(JeePayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
    }

}
