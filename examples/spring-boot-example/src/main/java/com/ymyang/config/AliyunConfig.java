package com.ymyang.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {

    private String accessKeyId;

    private String accessKeySecret;

    private AliyunOssConfig oss;
}
