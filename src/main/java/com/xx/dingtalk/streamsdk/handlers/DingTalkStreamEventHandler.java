package com.xx.dingtalk.streamsdk.handlers;

import cn.hutool.extra.spring.SpringUtil;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import com.xx.dingtalk.streamsdk.consumers.DingTalkEventConsumer;
import com.xx.dingtalk.streamsdk.store.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import shade.com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 钉钉 stream 全局事件处理器
 *
 * @author xx
 * @date 2024-08-19
 */
@Slf4j
public class DingTalkStreamEventHandler implements InitializingBean, AutoCloseable {

    private final Map<String, DingTalkEventConsumer<?>> consumers = new ConcurrentHashMap<>();


    private final EventStore eventStore;

    public DingTalkStreamEventHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, DingTalkEventConsumer> beansOfType = SpringUtil.getBeansOfType(DingTalkEventConsumer.class);
        beansOfType.forEach((k, v) -> {
            if (StringUtils.hasText(v.getEvent())) {
                log.info("Find DingTalk event [{}], consumer: {}", v.getEvent(), v.getClass().getName());
                consumers.put(v.getEvent(), v);
            } else {
                log.warn("Event name for consumer {} is missing, skipping registration.", v.getClass().getName());
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> EventAckStatus consume(GenericOpenDingTalkEvent event) {
        String eventId = event.getEventId();
        String eventType = event.getEventType();
        Long bornTime = event.getEventBornTime();
        JSONObject bizData = event.getData();
        doStoreIfNecessary(event);
        DingTalkEventConsumer<T> consumer = (DingTalkEventConsumer<T>) consumers.get(eventType);
        if (consumer != null) {
            if (log.isDebugEnabled()) {
                log.debug("[Ding Talk Stream] Received DingTalk event notification, eventId: {}, eventType: {}, timestamp: {} \n jsonData : {}", eventId, eventType, bornTime, bizData);
            }
            try {
                T eventData = convertToEventData(bizData, consumer);
                consumer.consume(eventData);
                return EventAckStatus.SUCCESS;
            } catch (Exception e) {
                log.error("[Ding Talk Stream] Error handling event: eventId={}, eventType={}", eventId, eventType, e);
                return EventAckStatus.LATER;
            }
        } else {
            log.warn("[Ding Talk Stream] No consumer found for eventType: {}, skip the msg...", eventType);
            return EventAckStatus.SUCCESS;
        }
    }

    private void doStoreIfNecessary(GenericOpenDingTalkEvent event) {
        if (eventStore != null) {
            try {
                eventStore.save(event);
            } catch (Exception e) {
                log.error("[Ding Talk Stream] store msg failed.", e);
            }
        }
    }

    private <T> T convertToEventData(JSONObject bizData, DingTalkEventConsumer<T> consumer) {
        try {
            return bizData.toJavaObject(getGenericType(consumer.getClass()));
        } catch (Exception e) {
            log.error("[Ding Talk Stream] Error converting event data: {}", bizData, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getGenericType(Class<?> parentClz) {
        Type genericSuperclass = parentClz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                return (Class<T>) actualTypeArguments[0];
            }
        }
        return (Class<T>) Object.class; // Fallback to Object if generic type not found
    }


    @Override
    public void close() throws Exception {
        consumers.clear();
    }
}
