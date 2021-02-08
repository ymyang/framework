package com.ymyang.feign;

import com.ymyang.param.order.OrderCreateParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order-service")//, fallback = TestFallback.class)
public interface OrderFeignClient {

    @PostMapping("/api/order")
    String order(@RequestBody OrderCreateParam body);
}
