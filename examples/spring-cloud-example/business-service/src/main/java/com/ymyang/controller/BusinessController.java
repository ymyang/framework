package com.ymyang.controller;

import com.ymyang.feign.OrderFeignClient;
import com.ymyang.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class BusinessController {

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @GlobalTransactional(timeoutMills = 300000, name = "spring-cloud-demo-tx")
    @PostMapping("/order")
    public String order() {
        Map<String, Object> storage = new HashMap<>();
        storage.put("commodityCode", "C00321");
        storage.put("count", 2);

        String storageRes = storageFeignClient.updateStorage(storage);
        if (!"ok".equals(storageRes)) {
            throw new RuntimeException();
        }

        Map<String, Object> order = new HashMap<>();
        order.put("userId", 1);
        order.put("commodityCode", "C00321");
        order.put("count", 2);
        order.put("money", 2);

        String orderRes = orderFeignClient.order(order);
        if (!"ok".equals(orderRes)) {
            throw new RuntimeException();
        }

        return "ok";
    }

}
