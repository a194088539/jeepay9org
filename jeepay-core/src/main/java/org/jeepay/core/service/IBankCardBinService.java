package org.jeepay.core.service;

import org.jeepay.core.entity.BankCardBin;

import java.util.List;

/**
 * @author: aragom
 * @date: 18/07/22
 * @description: 银行卡Bin接口
 */
public interface IBankCardBinService {

    int add(BankCardBin bankCardBin);

    int update(BankCardBin bankCardBin);

    BankCardBin findById(Long id);

    BankCardBin findByCardBin(String cardBin);

    BankCardBin findByCardNo(String cardNo);

    BankCardBin findByCardNoAndIfTypeCode(String cardNo, String ifTypeCode);

    List<BankCardBin> select(int offset, int limit, BankCardBin bankCardBin);

    Integer count(BankCardBin bankCardBin);

    int delete(Long id);

    int delete(List<Long> ids);

    int insertBatch(List<BankCardBin> bankCardBinList);

}
