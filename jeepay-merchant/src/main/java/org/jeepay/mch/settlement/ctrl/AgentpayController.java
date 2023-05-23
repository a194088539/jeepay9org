package org.jeepay.mch.settlement.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.jeepay.core.common.Exception.ServiceException;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.*;
import org.jeepay.core.entity.MchAgentpayRecord;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.mch.common.ctrl.BaseController;
import org.jeepay.mch.common.service.RpcCommonService;
import org.jeepay.mch.common.util.POIUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: aragom
 * @date: 18/04/21
 * @description: 代付操作
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/agentpay")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class AgentpayController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(AgentpayController.class);

    /**
     * 申请单笔代付
     * @return
     */
    @RequestMapping("/apply")
    @ResponseBody
    @MethodLog( remark = "单笔代付" )
    public ResponseEntity<?> apply(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        long mchId = getUser().getId();
        Long agentpayAmountL = getRequiredAmountL(param, "agentpayAmount");

        if(agentpayAmountL <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
        }
        String accountName = StringUtils.deleteWhitespace(getStringRequired(param, "accountName"));   // 账户名
        String accountNo = StringUtils.deleteWhitespace(getStringRequired(param, "accountNo"));       // 账号
        String bankName;         																	  // 银行名称
        try {
        	bankName = StringUtils.deleteWhitespace(getStringRequired(param, "bankName"));
        } catch (Exception e) {
        	bankName = null;
        }
        String bankNetName;   																		  // 开户行
        try {
        	bankNetName = StringUtils.deleteWhitespace(getStringRequired(param, "bankNetName"));
        } catch (Exception e) {
        	bankNetName = null;
        }        
        String province;																			  // 所在省   																		  
        try {
        	province = StringUtils.deleteWhitespace(getStringRequired(param, "province"));
        } catch (Exception e) {
        	province = null;
        }
        String city;       		  																	  // 所在市
        try {
        	city = StringUtils.deleteWhitespace(getStringRequired(param, "city"));
        } catch (Exception e) {
        	city = null;
        }
        String bankNumber;     																		  // 银行编码
        try {
        	bankNumber = StringUtils.deleteWhitespace(getStringRequired(param, "bankNumber"));
        } catch (Exception e) {
        	bankNumber = null;
        }
        String remark = getString(param, "remark");                     							  // 备注
        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
        BizResponse bizResponse = verifyPay(mchInfo, param);
        if(bizResponse != null) return ResponseEntity.ok(bizResponse);
        MchAgentpayRecord mchAgentpayRecord = null;
        // 发起提现申请
        try {
            String agentpayOrderId = MySeq.getAgentpay();
            _log.info("商户后台发起代付,代付ID={}", agentpayOrderId);
            mchAgentpayRecord = new MchAgentpayRecord();
            mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
            mchAgentpayRecord.setMchId(getUser().getId());
            mchAgentpayRecord.setMchType(getUser().getType());
            mchAgentpayRecord.setAccountName(accountName);      // 账户名
            mchAgentpayRecord.setAccountNo(accountNo);          // 账号
            mchAgentpayRecord.setAmount(agentpayAmountL);       // 代付金额
            mchAgentpayRecord.setBankName(bankName);       		// 银行名称
            mchAgentpayRecord.setBankNetName(bankNetName);      // 开户行
            mchAgentpayRecord.setProvince(province);       		// 所在省
            mchAgentpayRecord.setCity(city);       				// 所在市
            mchAgentpayRecord.setBankNumber(bankNumber);        // 银行编码
            mchAgentpayRecord.setAgentpayChannel(MchConstant.AGENTPAY_CHANNEL_PLAT);    // 设置商户后台代付
            mchAgentpayRecord.setDevice("web");
            mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
            int result = rpcCommonService.rpcJeePayAgentpayService.applyAgentpay(mchInfo, mchAgentpayRecord);
            if(result == 1) {
                return ResponseEntity.ok(BizResponse.buildSuccess());
            }else {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
            }
        }catch (ServiceException e) {
            _log.error("商户ID:" + getUser().getId() + "," + e.getRetEnum().getMessage());
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum(), e.getExtraMsg()));
        }catch (Exception e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 批量代付申请
     * @param request
     * @return
     */
    @RequestMapping("/apply_batch")
    @ResponseBody
    @MethodLog( remark = "批量代付" )
    public ResponseEntity<?> batchApply(HttpServletRequest request){
        Long startTime = System.currentTimeMillis();
        JSONObject param = getJsonParam(request);
        String nums = getStringRequired(param, "nums");     // 存储前端提交的对已经行编号
        String values = getStringRequired(param, "values"); // 所有input表单域,根据编号取值,如accountName_0
        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
        BizResponse bizResponse = verifyPay(mchInfo, param);
        if(bizResponse != null) return ResponseEntity.ok(bizResponse);
        List<MchAgentpayRecord> mchAgentpayRecordList = new LinkedList<>();
        try {
            // 代付总笔数
            Integer totalNum = 0;
            // 代付总金额
            Long totalAmount= 0l;
            JSONArray numArray = JSONArray.parseArray(nums);
            JSONObject valueObj = JSONObject.parseObject(values);
            for(int i = 0; i < numArray.size(); i++) {
                String accountName = StringUtils.deleteWhitespace(valueObj.getString("accountName_" + numArray.getString(i)));
                String accountNo = StringUtils.deleteWhitespace(valueObj.getString("accountNo_" + numArray.getString(i)));
                //String accountBank = StringUtils.deleteWhitespace(valueObj.getString("accountBank_" + numArray.getString(i)));
                String agentpayAmount = StringUtils.deleteWhitespace(valueObj.getString("agentpayAmount_" + numArray.getString(i)));
                Long agentpayAmountL = new BigDecimal(agentpayAmount).multiply(new BigDecimal(100)).longValue(); // // 转成分
                if(agentpayAmountL <= 0) {
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
                }
                MchAgentpayRecord mchAgentpayRecord = new MchAgentpayRecord();
                mchAgentpayRecord.setAccountName(accountName);  // 账户名
                mchAgentpayRecord.setAccountNo(accountNo);      // 账号
                //mchAgentpayRecord.setBankName(accountBank);     //银行名称
                mchAgentpayRecord.setAmount(agentpayAmountL);       // 代付金额
                mchAgentpayRecord.setRemark("代付:" + agentpayAmount + "元");
                mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
                mchAgentpayRecord.setDevice("web");
                totalNum++;
                totalAmount += agentpayAmountL;
                mchAgentpayRecordList.add(mchAgentpayRecord);
            }
            // 如果账户记录为空
            if(mchAgentpayRecordList.size() == 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY));
            }
            if((mchAgentpayRecordList.size() > 10)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT, "最多10条"));
            }

            // 查看是否有重复代付申请记录
            MchAgentpayRecord mchAgentpayRecord = isRepeatAgentpay(mchAgentpayRecordList);
            if(mchAgentpayRecord != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元"));
            }

            // 批量提交代付
            JSONObject resObj = rpcCommonService.rpcJeePayAgentpayService.batchApplyAgentpay(mchInfo, totalNum, totalAmount, mchAgentpayRecordList);
            if(resObj == null || resObj.getIntValue("batchInertCount") <= 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "批量代付失败"));
            }

            // 返回数据
            JSONObject retObj = new JSONObject();
            retObj.put("batchInertCount", resObj.getInteger("batchInertCount"));
            retObj.put("totalAmount", resObj.getLongValue("totalAmount")); //
            retObj.put("totalFee", resObj.getLongValue("totalFee"));
            retObj.put("costTime", System.currentTimeMillis() - startTime); // 记录服务端耗时
            return ResponseEntity.ok(JeePayResponse.buildSuccess(retObj));
        } catch (ServiceException e) {
            _log.error("商户ID:" + getUser().getId() + "," + e.getRetEnum().getMessage());
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        } catch (Exception e) {
            _log.error(e, e.getMessage());
            // 清除缓存的key
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }

    /**
     * 批量代付申请
     * @param request
     * @param upFile
     * @return
     */
    @MethodLog( remark = "批量代付（上传文件）" )
    @RequestMapping("/batch_upload")
    public Object uploadFile(HttpServletRequest request, @RequestParam(value = "upFile", required = true) MultipartFile upFile){
        Long startTime = System.currentTimeMillis();
        JSONObject param = getJsonParam(request);
        Long agentpayAmountL = getRequiredAmountL(param, "agentpayAmount");         // 代付总金额,转成分
        Integer agentpayNumber = getIntegerRequired(param, "agentpayNumber");       // 代付笔数
        // 支付安全验证
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(getUser().getId());
        BizResponse bizResponse = verifyPay(mchInfo, param);
        if(bizResponse != null) return ResponseEntity.ok(bizResponse);
        if(upFile.isEmpty()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        List<MchAgentpayRecord> mchAgentpayRecordList = null;
        try {
            String fileName = upFile.getOriginalFilename();
            List<List<String>> list = null;
            if(fileName.endsWith(".csv")) {
                InputStreamReader fr = new InputStreamReader(upFile.getInputStream());
                list = FileUtils.readCSVFile(fr, "GBK");
            }else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                list = POIUtil.readExcel(upFile);
            }
            if(CollectionUtils.isEmpty(list)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
            }

            mchAgentpayRecordList = new LinkedList<>();
            // 记录文件中总金额
            Long totalAmount= 0l;
            Integer totalNum = 0;
            for(int i = 0; i < list.size(); i++) {
                List<String> strList = list.get(i);
                try {
                    if(i > 0 && StringUtils.isNotBlank(strList.get(0))) {
                        MchAgentpayRecord mchAgentpayRecord = new MchAgentpayRecord();
                        mchAgentpayRecord.setAccountName(StringUtils.deleteWhitespace(strList.get(0)));     // 账户名
                        mchAgentpayRecord.setAccountNo(StringUtils.deleteWhitespace(strList.get(1)));       // 账号
                        Long amount = new BigDecimal(StringUtils.deleteWhitespace(strList.get(2))).multiply(new BigDecimal(100)).longValue();
                        if(amount <= 0) {
                            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0"));
                        }
                        mchAgentpayRecord.setAmount(amount);                // 代付金额
                        mchAgentpayRecord.setRemark("代付:" + amount/100 + "元");
                        mchAgentpayRecord.setClientIp(IPUtility.getClientIp(request));
                        mchAgentpayRecord.setDevice("web");
                        totalAmount += amount;
                        totalNum++;
                        mchAgentpayRecordList.add(mchAgentpayRecord);
                    }
                }catch (Exception e) {
                    _log.error(e, "第" + i + "行数据处理异常");
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
                }
            }
            // 如果账户记录为空
            if(mchAgentpayRecordList.size() == 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY));
            }
            if((mchAgentpayRecordList.size() > 1000)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT));
            }
            // 文件中的金额与申请代付金额不符
            if(totalAmount.compareTo(agentpayAmountL) != 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_AMOUNT));
            }
            // 文件中的笔数与申请代付笔数不一致
            if(totalNum.compareTo(agentpayNumber) != 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_NUMBER));
            }
            // 查看是否有重复代付申请记录
            MchAgentpayRecord mchAgentpayRecord = isRepeatAgentpay(mchAgentpayRecordList);
            if(mchAgentpayRecord != null) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元"));
            }
            // 批量提交代付
            JSONObject resObj = rpcCommonService.rpcJeePayAgentpayService.batchApplyAgentpay(mchInfo, totalNum, totalAmount, mchAgentpayRecordList);
            if(resObj == null || resObj.getIntValue("batchInertCount") <= 0) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL, "批量代付失败"));
            }
            // 返回数据
            JSONObject retObj = new JSONObject();
            retObj.put("batchInertCount", resObj.getInteger("batchInertCount"));
            retObj.put("totalAmount", resObj.getLongValue("totalAmount")); //
            retObj.put("totalFee", resObj.getLongValue("totalFee"));
            retObj.put("costTime", System.currentTimeMillis() - startTime); // 记录服务端耗时
            return ResponseEntity.ok(JeePayResponse.buildSuccess(retObj));
        } catch (ServiceException e) {
            _log.error("商户ID:" + getUser().getId() + "," + e.getRetEnum().getMessage());
            return ResponseEntity.ok(BizResponse.build(e.getRetEnum()));
        } catch (IOException e) {
            _log.error(e, "");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }


    /**
     * 判断是否有重复的代付申请(卡号+金额),只要有重复即返回
     * @param mchAgentpayRecordList
     * @return
     */
    MchAgentpayRecord isRepeatAgentpay(List<MchAgentpayRecord> mchAgentpayRecordList) {
        Map<String, MchAgentpayRecord> map = new HashMap<>();
        if(mchAgentpayRecordList == null || mchAgentpayRecordList.size() <= 1) return null;
        for(MchAgentpayRecord record : mchAgentpayRecordList) {
            String key = record.getAccountNo() + record.getAmount();
            if(map.get(key) != null) {
                return map.get(key);
            }
            map.put(key, record);
        }
        return null;
    }

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
        mchAgentpayRecord.setMchId(getUser().getId());
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
        mchAgentpayRecord.setMchId(getUser().getId());
        mchAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
        mchAgentpayRecord = rpcCommonService.rpcMchAgentpayService.find(mchAgentpayRecord);
        if(mchAgentpayRecord != null) {
            // 取银行卡号前四位和后四位,中间星号代替
            String accountNo = StrUtil.str2Star3(mchAgentpayRecord.getAccountNo(), 4, 4, 4);
            mchAgentpayRecord.setAccountNo(accountNo);
        }
        return ResponseEntity.ok(JeePayResponse.buildSuccess(mchAgentpayRecord));
    }

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long mchId = getUser().getId();
        String agentpayOrderId = getString(param, "agentpayOrderId");
        String accountName = getString(param, "accountName");
        Byte status = getByte(param, "status");
        Byte agentpayChannel = getByte(param, "agentpayChannel");
        // 订单起止时间
        String createTimeStartStr = getString(param, "createTimeStart");
        String createTimeEndStr = getString(param, "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchAgentpayService.count4All(mchId, accountName, agentpayOrderId, null, status, agentpayChannel, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalFee", allMap.get("totalFee"));                             // 费用
        obj.put("allTotalSubAmount", allMap.get("totalSubAmount"));                 // 扣减金额
        return ResponseEntity.ok(JeePayResponse.buildSuccess(obj));
    }

    public static void main(String[] args) {
        String str = "a b c ";
        System.out.println(StringUtils.deleteWhitespace(str));
    }

}
