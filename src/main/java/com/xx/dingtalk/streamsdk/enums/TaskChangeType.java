package com.xx.dingtalk.streamsdk.enums;

import lombok.Getter;

/**
 * 任务改变事件类型
 *
 * @author xx
 * @date 2024-08-19
 */
@Getter
public enum TaskChangeType {
    START("start"),
    FINISH("finish"),
    CANCEL("cancel"),
    COMMENT("comment");

    private final String value;

    TaskChangeType(String value) {
        this.value = value;
    }

    public static TaskChangeType fromValue(String value) {
        for (TaskChangeType type : TaskChangeType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
