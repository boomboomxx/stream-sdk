package com.xx.dingtalk.streamsdk.config.annotations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xx
 * @date 2024-09-26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ConditionalOnProperty(value = "dingtalk.stream-client.store.default.enable", matchIfMissing = true)
public @interface ConditionOnDefaultStoreEnabled {
}
