package com.ymyang.controller;

import com.alibaba.fastjson.JSON;
import com.ymyang.entity.OrderEntity;
import com.ymyang.service.OrderService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService accountService;

    @PostMapping
    public String order(@RequestBody OrderEntity param) {
        log.info("POST /api/order, xid: " + RootContext.getXID() + ", param: " + JSON.toJSONString(param));

        accountService.order(param);

        return "ok";
    }

}
