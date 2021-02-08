package com.ymyang.config;

import lombok.Data;

@Data
public class AliyunOssConfig {

    private String endpoint;

    private String bucketName;

    private String fileHost;

    private String url;

}
