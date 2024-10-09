package com.xx.dingtalk.streamsdk.config;

import cn.hutool.extra.spring.SpringUtil;
import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.xx.dingtalk.streamsdk.config.annotations.ConditionOnEnableDingTalkStreamSdk;
import com.xx.dingtalk.streamsdk.config.properties.DingTalkStreamProperties;
import com.xx.dingtalk.streamsdk.handlers.DingTalkStreamEventHandler;
import com.xx.dingtalk.streamsdk.store.EventStore;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * 钉钉 stream 配置
 *
 * @author xx
 * @date 2024-08-15
 */
@Slf4j
@Configuration
@ConditionOnEnableDingTalkStreamSdk
public class DingTalkStreamAutoConfiguration {
    private final static List<String> effectiveKeys = Arrays.asList("dingtalk.stream-client.enable", "dingtalk.stream-client.appId", "dingtalk.stream-client.appSecret");
    private final Object lock = new Object();

    @EventListener
    public void handleDingTalkPropertiesChange(EnvironmentChangeEvent event) {
        Set<String> changedKeys = event.getKeys();
        boolean keyChanged = changedKeys.stream().anyMatch(effectiveKeys::contains);
        if (keyChanged) {
            synchronized (lock) {
                String beanName = "openDingTalkClient";
                if (Boolean.parseBoolean(SpringUtil.getProperty("dingtalk.stream-client.enable"))) {
                    OpenDingTalkClient bean;
                    try {
                        bean = SpringUtil.getBean(beanName, OpenDingTalkClient.class);
                        bean.stop();
                    } catch (Exception e) {
                        log.info("[钉钉 stream] 客户端未注册, 开始注册");
                    } finally {
                        SpringUtil.unregisterBean(beanName);
                    }
                    DingTalkStreamProperties properties = dingTalkStreamProperties();
                    DingTalkStreamEventHandler handler = SpringUtil.getBean(DingTalkStreamEventHandler.class);
                    SpringUtil.registerBean(beanName, openDingTalkClient(properties, handler));
                    bean = SpringUtil.getBean(beanName, OpenDingTalkClient.class);
                    try {
                        bean.start();
                    } catch (Exception e) {
                        log.error("无法注册 钉钉stream客户端, 错误信息:\n", e);
                    }
                } else {
                    SpringUtil.unregisterBean(beanName);
                }
            }

        }
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkStreamProperties dingTalkStreamProperties() {
        return new DingTalkStreamProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public DingTalkStreamEventHandler streamEventHandler(@Nullable EventStore eventStore) {
        return new DingTalkStreamEventHandler(eventStore);
    }

    public OpenDingTalkClient openDingTalkClient(DingTalkStreamProperties properties, DingTalkStreamEventHandler handler) {
        return OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential(properties.getAppId(), properties.getAppSecret()))
                //注册事件监听
                .registerAllEventListener(new GenericEventListener() {
                    public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                        try {
                            //处理事件
                            return handler.consume(event);
                        } catch (Exception e) {
                            //消费失败
                            return EventAckStatus.LATER;
                        }
                    }
                })
                .build();
    }
}
