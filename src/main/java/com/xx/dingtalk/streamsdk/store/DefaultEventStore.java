package com.xx.dingtalk.streamsdk.store;

import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 默认的事件存储器
 *
 * @author xx
 * @date 2024-09-05
 */
public class DefaultEventStore implements EventStore, AutoCloseable {
    private final Map<String, GenericOpenDingTalkEvent> eventMap = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> eventLockMap = new HashMap<>();

    @Override
    public void save(GenericOpenDingTalkEvent event) {
        eventMap.putIfAbsent(event.getEventId(), event);
        eventLockMap.putIfAbsent(event.getEventId(), new ReentrantLock());
    }

    @Override
    public GenericOpenDingTalkEvent get(String eventId) {
        return eventMap.get(eventId);
    }

    @Override
    public void remove(String eventId) {
        eventMap.remove(eventId);
        eventLockMap.remove(eventId);
    }

    @Override
    public boolean exists(String eventId) {
        return eventMap.containsKey(eventId);
    }

    @Override
    public boolean tryLock(String eventId) {
        return eventLockMap.get(eventId).tryLock();
    }

    @Override
    public boolean releaseLock(String eventId) {
        eventLockMap.get(eventId).unlock();
        return true;
    }

    @Override
    public void close() throws Exception {
        eventMap.clear();
        eventLockMap.clear();
    }
}
