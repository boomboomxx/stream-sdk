package com.xx.dingtalk.streamsdk.config.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否允许钉钉 stream sdk 启动
 *
 * @author xx
 * @date 2024-09-04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(value = "dingtalk.stream-client.enable")
public @interface ConditionOnEnableDingTalkStreamSdk {
}
