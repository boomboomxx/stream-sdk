package com.xx.dingtalk.streamsdk.enums;

import lombok.Getter;

/**
 * @author xx
 * @date 2024-08-19
 */
@Getter
public enum InstanceResultType {
    AGREE("agree"),
    REFUSE("refuse");
    private String value;

    InstanceResultType(String value) {
        this.value = value;
    }

    public static InstanceResultType fromValue(String value) {
        for (InstanceResultType type : InstanceResultType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
