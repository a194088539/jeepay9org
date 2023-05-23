package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.entity.ChannelConfig;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/01/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/channel")
public class ChannelConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        ChannelConfig channelConfig = getObject(param, ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.count(channelConfig);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<ChannelConfig> channelConfigList = rpcCommonService.rpcChannelConfigService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), channelConfig);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(channelConfigList, count));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Integer id = getIntegerRequired(param, "id");
        ChannelConfig channelConfig = rpcCommonService.rpcChannelConfigService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(channelConfig));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get/channelId")
    @ResponseBody
    public ResponseEntity<?> getChanenelId(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String channelId = getStringRequired(param, "channelId");
        ChannelConfig channelConfig = rpcCommonService.rpcChannelConfigService.findByChannelId(channelId);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(channelConfig));
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        ChannelConfig channelConfig = getObject(param, ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.update(channelConfig);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        ChannelConfig channelConfig = getObject(param, ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.add(channelConfig);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
