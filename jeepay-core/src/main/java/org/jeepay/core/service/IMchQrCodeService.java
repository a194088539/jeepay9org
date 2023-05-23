package org.jeepay.core.service;

import org.jeepay.core.entity.MchQrCode;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/21
 * @description:
 */
public interface IMchQrCodeService {

    List<MchQrCode> select(int pageIndex, int pageSize, MchQrCode mchQrCode);

    int count(MchQrCode mchQrCode);

    MchQrCode findById(Long id);

    MchQrCode find(MchQrCode mchQrCode);

    MchQrCode findByMchIdAndAppId(Long mchId, String appId);

    int add(MchQrCode mchQrCode);

    int update(MchQrCode mchQrCode);

    int delete(Long id);

}
