package com.xx.dingtalk.streamsdk.enums;

import lombok.Getter;

/**
 * @author xx
 * @date 2024-08-19
 */
@Getter
public enum InstanceType {
    START("start"),
    FINISH("finish"),
    TERMINATE("terminate"),
    DELETE("delete"),
    ;

    private final String value;

    InstanceType(String value) {
        this.value = value;
    }

    public static InstanceType fromValue(String value) {
        for (InstanceType type : InstanceType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
