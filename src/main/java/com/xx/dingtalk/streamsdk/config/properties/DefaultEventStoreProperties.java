package com.xx.dingtalk.streamsdk.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 默认存储器配置
 *
 * @author xx
 * @date 2024-09-26
 */
@Data
@ConfigurationProperties(prefix = "dingtalk.stream-client.store.default")
public class DefaultEventStoreProperties {
    private boolean enable = true;

}
