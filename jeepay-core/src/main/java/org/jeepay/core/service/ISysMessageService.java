package org.jeepay.core.service;

import org.jeepay.core.entity.SysMessage;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
public interface ISysMessageService {

    List<SysMessage> select(int pageIndex, int pageSize, SysMessage sysMessage);

    int count(SysMessage sysMessage);

    SysMessage findById(Long id);

    SysMessage find(SysMessage sysMessage);

    int add(SysMessage sysMessage);

    int update(SysMessage sysMessage);

    int delete(Long id);

    Integer batchDelete(List<Long> ids);

}
