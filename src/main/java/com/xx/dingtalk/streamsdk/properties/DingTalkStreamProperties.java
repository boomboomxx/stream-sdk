package com.xx.dingtalk.streamsdk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉stream事件参数
 *
 * @author xx
 * @date 2024-08-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dingtalk-stream")
public class DingTalkStreamProperties {
    private Boolean enable = false;
    /**
     * 应用id, 也叫 accessKey
     */
    private String appId;
    /**
     * 应用密钥, 也叫 secret_key
     */
    private String appSecret;
    /**
     * http 回调 aesKey
     */
    private String aesKey;
    /**
     * http 回调 token
     */
    private String token;
}
