package org.jeepay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.jeepay.core.common.annotation.MethodLog;
import org.jeepay.core.common.constant.Constant;
import org.jeepay.core.common.constant.RetEnum;
import org.jeepay.core.common.domain.BizResponse;
import org.jeepay.core.common.domain.JeePayPageRes;
import org.jeepay.core.common.domain.JeePayResponse;
import org.jeepay.core.common.util.FileUtils;
import org.jeepay.core.common.util.MyLog;
import org.jeepay.core.entity.BankCardBin;
import org.jeepay.manage.common.ctrl.BaseController;
import org.jeepay.manage.common.service.RpcCommonService;
import org.jeepay.manage.common.util.POIUtil;
import sun.swing.BakedArrayList;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/07/22
 * @description: 银行卡bin
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/card_bin")
public class BankCardBinController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(BankCardBinController.class);

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        BankCardBin bankCardBin = getObject(param, BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.count(bankCardBin);
        if(count == 0) return ResponseEntity.ok(JeePayPageRes.buildSuccess());
        List<BankCardBin> bankCardBinList = rpcCommonService.rpcBankCardBinService.select((getPageIndex(param) -1) * getPageSize(param), getPageSize(param), bankCardBin);
        return ResponseEntity.ok(JeePayPageRes.buildSuccess(bankCardBinList, count));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        Long id = getLongRequired(param, "id");
        BankCardBin bankCardBin = rpcCommonService.rpcBankCardBinService.findById(id);
        return ResponseEntity.ok(JeePayResponse.buildSuccess(bankCardBin));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改卡BIN信息" )
    public ResponseEntity<?> update(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        BankCardBin bankCardBin = getObject(param, BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.update(bankCardBin);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增卡BIN信息" )
    public ResponseEntity<?> add(HttpServletRequest request) {
        JSONObject param = getJsonParam(request);
        BankCardBin bankCardBin = getObject(param, BankCardBin.class);
        int count = rpcCommonService.rpcBankCardBinService.add(bankCardBin);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 批量导入卡bin
     * @param request
     * @param upFile
     * @return
     */
    @RequestMapping("/import")
    public Object importCardBin(HttpServletRequest request, @RequestParam(value = "upFile", required = true) MultipartFile upFile){
        Long startTime = System.currentTimeMillis();
        if(upFile.isEmpty()) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        try {
            String fileName = upFile.getOriginalFilename();
            List<List<String>> list = new ArrayList<>();
            if(fileName.endsWith(".csv")) {
                InputStreamReader fr = new InputStreamReader(upFile.getInputStream());
                list = FileUtils.readCSVFile(fr, "GBK");
            }else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                list = POIUtil.readExcel(upFile);
            }
            if(CollectionUtils.isEmpty(list)) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
            }

            List<BankCardBin> bankCardBinList = new LinkedList<>();
            int num = 0;
            // 记录文件中总金额
            for(int i = 0; i < list.size(); i++) {
                List<String> strList = list.get(i);
                try {
                    if(i > 3) {
                        BankCardBin bankCardBin = new BankCardBin();
                        String str0 = strList.get(0).trim();    // 发卡行名称及机构代码,如:中国银联支付标记(00010030)
                        String bankName = str0.substring(0, str0.indexOf("("));
                        String bankCode = str0.substring(str0.indexOf("(")+1, str0.indexOf(")"));
                        bankCardBin.setBankName(bankName);                  // 银行名称
                        bankCardBin.setBankCode(bankCode);                  // 银行编码
                        bankCardBin.setCardName(strList.get(1).trim());     // 卡名
                        bankCardBin.setCardType(strList.get(15).trim());    // 银行卡类型
                        bankCardBin.setCardLength(Integer.parseInt(strList.get(8)));    // 卡长度
                        bankCardBin.setCardBin(strList.get(13).trim());     // 卡bin
                        bankCardBinList.add(bankCardBin);
                    }
                }catch (Exception e) {
                    _log.error(e, "第" + i + "行数据处理异常");
                    return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_SETT_BATCH_APPLY_FORMAT_ERROR));
                }
                // 100个提交批量插入
                if(bankCardBinList.size() > 100) {
                    rpcCommonService.rpcBankCardBinService.insertBatch(bankCardBinList);
                    num+= bankCardBinList.size();
                    bankCardBinList = new LinkedList<>();
                }
            }
            if(bankCardBinList.size() > 0) {
                rpcCommonService.rpcBankCardBinService.insertBatch(bankCardBinList);
                num+= bankCardBinList.size();
            }
            // 返回数据
            JSONObject retObj = new JSONObject();
            retObj.put("batchInertCount", num);
            retObj.put("costTime", System.currentTimeMillis() - startTime); // 记录服务端耗时
            return ResponseEntity.ok(JeePayResponse.buildSuccess(retObj));
        } catch (IOException e) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_UNKNOWN_ERROR));
        }
    }


}
