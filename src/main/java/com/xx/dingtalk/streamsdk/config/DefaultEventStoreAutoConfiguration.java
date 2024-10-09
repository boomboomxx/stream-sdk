package com.xx.dingtalk.streamsdk.config;

import com.xx.dingtalk.streamsdk.config.annotations.ConditionOnEnableDingTalkStreamSdk;
import com.xx.dingtalk.streamsdk.handlers.DingTalkStreamEventHandler;
import com.xx.dingtalk.streamsdk.store.DefaultEventStore;
import com.xx.dingtalk.streamsdk.store.EventStore;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 事件存储器配置
 *
 * @author xx
 * @date 2024-09-26
 */
@Configuration
@ConditionalOnBean(DingTalkStreamEventHandler.class)
@AutoConfigureAfter({DingTalkStreamAutoConfiguration.class})
@ConditionOnEnableDingTalkStreamSdk
public class DefaultEventStoreAutoConfiguration {
    @Bean
    @Order
    public EventStore defaultEventStore() {
        return new DefaultEventStore();
    }
}
