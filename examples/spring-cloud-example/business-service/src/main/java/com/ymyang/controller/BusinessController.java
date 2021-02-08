package com.ymyang.controller;

import com.ymyang.feign.OrderFeignClient;
import com.ymyang.feign.StorageFeignClient;
import com.ymyang.param.order.OrderCreateParam;
import com.ymyang.param.storage.StorageUpdateParam;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        StorageUpdateParam storage = new StorageUpdateParam();
        storage.setCommodityCode("C00321");
        storage.setCount(2);

        String storageRes = storageFeignClient.updateStorage(storage);
        if (!"ok".equals(storageRes)) {
            throw new RuntimeException();
        }

        OrderCreateParam order = new OrderCreateParam();
        order.setUserId(1);
        order.setCommodityCode("C00321");
        order.setCount(2);
        order.setMoney(2);

        String orderRes = orderFeignClient.order(order);
        if (!"ok".equals(orderRes)) {
            throw new RuntimeException();
        }

        return "ok";
    }

}
