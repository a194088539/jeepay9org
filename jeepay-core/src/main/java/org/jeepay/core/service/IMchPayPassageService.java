package org.jeepay.core.service;

import org.jeepay.core.entity.MchPayPassage;
import org.jeepay.core.entity.PayPassage;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/5/3
 * @description: 商户支付通道
 */
public interface IMchPayPassageService {

    int add(MchPayPassage mchPayPassage);

    int update(MchPayPassage mchPayPassage);

    int update(MchPayPassage updateMchPayPassage, MchPayPassage queryMchPayPassage);

    MchPayPassage findById(Integer id);

    MchPayPassage findByMchIdAndProductId(Long mchId,  Integer productId);

    List<MchPayPassage> select(int offset, int limit, MchPayPassage mchPayPassage);

    Integer count(MchPayPassage mchPayPassage);

    List<MchPayPassage> selectAll(MchPayPassage mchPayPassage);

    /**
     * 根据商户ID查询所有商户支付通道列表
     * @param mchId
     * @return
     */
    List<MchPayPassage> selectAllByMchId(Long mchId);

}
