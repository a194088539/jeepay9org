package org.jeepay.core.service;

import org.jeepay.core.entity.PayProduct;

import java.util.Map;

/**
 * @author: aragom
 * @date: 18/9/11
 * @description: 公共服务接口,一般用于查询配置,字典等
 */
public interface ICommonService {

    Map getPayProdcutMap(PayProduct payProduct);

}
