package org.jeepay.core.service;

import org.jeepay.core.entity.ChannelConfig;

import java.util.List;

/**
 * @author: aragom
 * @date: 17/12/14
 * @description:
 */
public interface IChannelConfigService {

    int add(ChannelConfig channelConfig);

    int update(ChannelConfig channelConfig);

    List<ChannelConfig> select(int pageIndex, int pageSize, ChannelConfig channelConfig);

    Integer count(ChannelConfig channelConfig);

    ChannelConfig findById(int id);

    ChannelConfig findByChannelId(String channelId);

}
