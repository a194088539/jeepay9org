package org.jeepay.manage.settlement.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.PayConstant;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.StrUtil;
import org.jeepay.core.common.util.JEEPayUtil;
import org.jeepay.core.entity.AgentpayPassageAccount;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 17/12/6
 * @description: 代付
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agentpay")
public class AgentpayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 代付列表查询
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        MchAgentpayRecord mchAgentpayRecord = getObject(param, MchAgentpayRecord.class);
        if(mchAgentpayRecord == null) mchAgentpayRecord = new MchAgentpayRecord();
        int count = rpcCommonService.rpcMchAgentpayService.count(mchAgentpayRecord, getQueryObj(param));
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<MchAgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcMchAgentpayService.select((getPageIndex(param)-1) * getPageSize(param), getPageSize(param), mchAgentpayRecord, getQueryObj(param));
        for(MchAgentpayRecord mchAgentpayRecord1 : mchAgentpayRecordList) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord1.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord1.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(mchAgentpayRecordList, count));
    }

    /**
     * 代付记录查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String agentpayOrderId = getStringRequired(param, "agentpayOrderId");
        MchAgentpayRecord mchAgentpayRecord = new MchAgentpayRecord();
        mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
        mchAgentpayRecord = rpcCommonService.rpcMchAgentpayService.find(mchAgentpayRecord);
        if(mchAgentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchAgentpayRecord));
    }

    @RequestMapping("/trans_query")
    @ResponseBody
    public ResponseEntity<?> queryTrans(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        String transOrderId = getStringRequired(param, "transOrderId");
        JSONObject resObj = rpcCommonService.rpcJeePayTransService.queryTrans(transOrderId);
        if(JEEPayUtil.isSuccess(resObj)) {
            JSONObject jsonObject = resObj.getJSONObject("channelObj");
            return ResponseEntity.ok(JeePayResponse.buildSuccess(jsonObject == null ? "转账接口没有返回channelObj" : jsonObject.toJSONString()));
        }else {
            return ResponseEntity.ok(JeePayResponse.buildSuccess("查询通道异常【" + resObj.getString(PayConstant.RESULT_PARAM_ERRDES) + "】"));
        }
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getLong(param, "mchId");
        String agentpayOrderId = getString(param, "agentpayOrderId");
        String transOrderId = getString(param, "transOrderId");
        String accountName = getString(param, "accountName");
        Byte status = getByte(param, "status");
        Byte agentpayChannel = getByte(param, "agentpayChannel");

        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchAgentpayService.count4All(mchId, accountName, agentpayOrderId, transOrderId, status, agentpayChannel, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        obj.put("allTotalSubAmount", allMap.get("totalSubAmount"));                 // 扣减金额
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

}
