package com.xx.dingtalk.streamsdk.enums;

import lombok.Getter;

/**
 * 钉钉事件
 *
 * @author xx
 * @date 2024-08-21
 */
@Getter
public enum DingTalkEvent {

    PROCESS_INSTANCE_CHANGE("bpms_instance_change", "流程实例变更事件"),

    PROCESS_TASK_CHANGE("bpms_task_change", "流程任务变更事件"),
    ;

    private final String name;

    private final String desc;

    DingTalkEvent(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

}
