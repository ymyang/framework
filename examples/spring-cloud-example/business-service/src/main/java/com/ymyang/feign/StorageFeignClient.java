package com.ymyang.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "storage-service")//, fallback = TestFallback.class)
public interface StorageFeignClient {

    @PostMapping("/api/storage")
    String updateStorage(@RequestBody Map<String, Object> body);
}
