package com.xx.dingtalk.streamsdk.enums;

import lombok.Getter;

/**
 * 审核结果枚举
 *
 * @author xx
 * @date 2024-08-19
 */
@Getter
public enum ApprovalResult {
    AGREE("agree"),
    REFUSE("refuse"),
    REDIRECT("redirect"),
    AUDIT("audit");

    private final String value;

    ApprovalResult(String value) {
        this.value = value;
    }


    public static ApprovalResult fromValue(String value) {
        for (ApprovalResult result : ApprovalResult.values()) {
            if (result.getValue().equals(value)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
