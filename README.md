# 钉钉 Stream SDK 集成支持
该项目为 钉钉 stream sdk 自动注册管理 sdk
用户只需关心对应的事件, 无需关系其内部的实现

## 项目依赖
1. [dingtalk-stream](https://open.dingtalk.com/document/resourcedownload/introduction-to-stream-mode#)
2. [hutool-all](https://hutool.cn/)
3. [spring-cloud-alibaba](https://sca.aliyun.com/)
4. JDK 17+

## 使用
1. 引入本项目依赖
2. 实现接口 [DingTalkEventConsumer.java](src%2Fmain%2Fjava%2Fcom%2Fxx%2Fdingtalk%2Fstreamsdk%2Fconsumers%2FDingTalkEventConsumer.java)
3. 配置 application.yml 配置文件, 对应的配置类为 [DingTalkStreamProperties.java](src%2Fmain%2Fjava%2Fcom%2Fxx%2Fdingtalk%2Fstreamsdk%2Fconfig%2Fproperties%2FDingTalkStreamProperties.java)
4. 启动你的项目