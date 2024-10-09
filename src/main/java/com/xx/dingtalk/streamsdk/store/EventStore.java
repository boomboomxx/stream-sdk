package com.xx.dingtalk.streamsdk.store;

import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import org.springframework.core.Ordered;

/**
 * 事件存储器
 * <p>
 * 默认实现为 {@link DefaultEventStore}
 * <p>
 * 如果需要自定义存储实现, 实现该类, 并且配置 {@code @AutoConfigureBefore({DefaultEventStoreAutoConfiguration.class})}
 *
 *
 *
 * @author xx
 * @date 2024-09-05
 */
public interface EventStore extends Ordered {
    int DEFAULT_ORDER = 0;

    void save(GenericOpenDingTalkEvent event);

    GenericOpenDingTalkEvent get(String eventId);

    void remove(String eventId);

    boolean exists(String eventId);

    boolean tryLock(String eventId);

    boolean releaseLock(String eventId);

    default int getOrder() {
        return DEFAULT_ORDER;
    }

}
