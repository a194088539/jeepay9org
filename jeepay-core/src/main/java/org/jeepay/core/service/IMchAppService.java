package org.jeepay.core.service;

import org.jeepay.core.entity.MchApp;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/13
 * @description:
 */
public interface IMchAppService {

    List<MchApp> select(int pageIndex, int pageSize, MchApp mchApp);

    int count(MchApp mchApp);

    MchApp findById(String appId);

    MchApp findByMchIdAndAppId(Long mchId, String appId);

    int add(MchApp mchApp);

    int update(MchApp mchApp);

    int updateByMchIdAndAppId(Long mchId, String appId, MchApp mchApp);

    int delete(String  appId);

}
