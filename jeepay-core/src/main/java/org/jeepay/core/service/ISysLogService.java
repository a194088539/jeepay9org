package org.jeepay.core.service;

import org.jeepay.core.entity.SysLog;

import java.util.List;

public interface ISysLogService {

    int add(SysLog record);

    List<SysLog> select(int offset, int limit, SysLog record);

    Integer count(SysLog record);

}
