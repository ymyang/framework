package com.ymyang.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "order-service")//, fallback = TestFallback.class)
public interface OrderFeignClient {

    @PostMapping("/api/order")
    String order(@RequestBody Map<String, Object> body);
}
