package org.jeepay.core.service;

import org.jeepay.core.entity.MchAgentpayPassage;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/5/3
 * @description: 商户代付通道
 */
public interface IMchAgentpayPassageService {

    int add(MchAgentpayPassage mchAgentpayPassage);

    int update(MchAgentpayPassage mchAgentpayPassage);

    int updateByMchId(MchAgentpayPassage updateMchAgentpayPassage, Long mchId);

    MchAgentpayPassage findById(Integer id);

    MchAgentpayPassage findByMchIdAndAgentpayPassageId(Long mchId, Integer agentpayPassageId);

    List<MchAgentpayPassage> select(int offset, int limit, MchAgentpayPassage mchAgentpayPassage);

    Integer count(MchAgentpayPassage mchAgentpayPassage);

    List<MchAgentpayPassage> selectAll(MchAgentpayPassage mchAgentpayPassage);

    /**
     * 根据商户ID查询所有商户代付通道列表
     * @param mchId
     * @return
     */
    List<MchAgentpayPassage> selectAllByMchId(Long mchId);

    List<MchAgentpayPassage> selectAvailable(Long mchId);
}
