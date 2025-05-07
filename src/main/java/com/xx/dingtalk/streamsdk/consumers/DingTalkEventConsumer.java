package com.xx.dingtalk.streamsdk.consumers;


/**
 * 钉钉 stream 事件处理器, 实现该接口即可接收钉钉 stream 事件
 * 不同的事件对应不同处理器
 *
 * @author xx
 * @date 2024-08-19
 */
public abstract class DingTalkEventConsumer<T> {

    public abstract void consume(T eventData);

    public abstract String getEvent();


}
