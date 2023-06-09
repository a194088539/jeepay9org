package org.jeepay.core.service;

import org.jeepay.core.entity.CheckBatch;
import org.jeepay.core.entity.CheckMistake;
import org.jeepay.core.entity.CheckMistakePool;
import org.jeepay.core.entity.MchInfo;

import java.util.Date;
import java.util.List;

/**
 * @author: aragom
 * @date: 18/1/21
 * @description:
 */
public interface ICheckService {

    List<CheckMistakePool> selectAllMistakePool();

    void saveData(CheckBatch batch, List<CheckMistake> mistakeList, List<CheckMistakePool> insertScreatchRecordList, List<CheckMistakePool> removeScreatchRecordList);

    List<CheckMistakePool> selectScratchPoolRecord(Date date);

    void removeDateFromPool(List<CheckMistakePool> list, List<CheckMistake> mistakeList);

    List<CheckBatch> selectCheckBatch(CheckBatch batch);

    List<CheckBatch> selectCheckBatch(int offset, int limit, CheckBatch batch);

    List<CheckMistake> selectCheckMistake(int offset, int limit, CheckMistake checkMistake);

    List<CheckMistakePool> selectCheckMistakePool(int offset, int limit, CheckMistakePool checkMistakePool);

    Integer countCheckBatch(CheckBatch batch);

    Integer countCheckMistake(CheckMistake checkMistake);

    Integer countCheckMistakePool(CheckMistakePool checkMistakePool);

    Integer handleCheckMistake(Long id, String handleType, String handleRemark);

    CheckBatch findByBatchId(long batchId);

    CheckMistake findByMistakeId(Long mistakeId);

    CheckMistakePool findByMistakePoolId(Long mistakePoolId);

}
