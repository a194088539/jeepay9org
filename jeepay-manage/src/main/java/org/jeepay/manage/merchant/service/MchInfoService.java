package org.jeepay.manage.merchant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.jeepay.core.common.constant.MchConstant;
import org.jeepay.core.entity.MchInfo;
import org.jeepay.manage.common.service.RpcCommonService;

import java.util.List;

/**
 * Created by aragom on 17/5/4.
 */
@Component
public class MchInfoService {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 审核商户信息
     * @param mchInd
     */
    public int audit(Long mchInd, Byte status) {
        if(MchConstant.STATUS_OK == status.byteValue()) {
            return rpcCommonService.rpcMchInfoService.auditOk(mchInd);
        }else if(MchConstant.STATUS_AUDIT_NOT == status.byteValue()) {
            return rpcCommonService.rpcMchInfoService.auditNot(mchInd);
        }
        return 0;
    }

    public int updateMchInfo(MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.update(mchInfo);
    }

    public MchInfo selectMchInfo(Long mchId) {
        return rpcCommonService.rpcMchInfoService.findByMchId(mchId);
    }

    public List<MchInfo> getMchNormalInfoList(int offset, int limit, MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.select4Normal(offset, limit, mchInfo);
    }

    public List<MchInfo> getMchAuditInfoList(int offset, int limit, MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.select4Audit(offset, limit, mchInfo);
    }

    public Integer count(MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.count(mchInfo);
    }

    public Integer countAudit(MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.count4Audit(mchInfo);
    }

    public Integer countNormal(MchInfo mchInfo) {
        return rpcCommonService.rpcMchInfoService.count4Normal(mchInfo);
    }

}
