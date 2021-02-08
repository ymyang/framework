package com.ymyang.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class MQConfig {

    private String accessChannel;
    private String accessKey;
    private String secretKey;

    private String nameSrvAddr;
    private String topic;
    private String group;

}
