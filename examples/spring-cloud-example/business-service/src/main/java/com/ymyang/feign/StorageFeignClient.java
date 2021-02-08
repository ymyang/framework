package com.ymyang.feign;

import com.ymyang.param.storage.StorageUpdateParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "storage-service")//, fallback = TestFallback.class)
public interface StorageFeignClient {

    @PostMapping("/api/storage")
    String updateStorage(@RequestBody StorageUpdateParam body);
}
