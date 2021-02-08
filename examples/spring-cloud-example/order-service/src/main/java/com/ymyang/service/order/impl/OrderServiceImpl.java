package com.ymyang.service.order.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ymyang.entity.order.OrderEntity;
import com.ymyang.feign.AccountFeignClient;
import com.ymyang.mapper.order.OrderMapper;
import com.ymyang.param.account.AccountUpdateParam;
import com.ymyang.param.order.OrderCreateParam;
import com.ymyang.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public void order(OrderCreateParam param) {
        AccountUpdateParam params = new AccountUpdateParam();
        params.setId(param.getUserId());
        params.setMoney(param.getMoney());
        String res = accountFeignClient.updateMoney(params);
        log.info("update money return: " + res);

        OrderEntity entity = new OrderEntity();
        param.copyPropertiesTo(entity, false);

        this.save(entity);
    }

}
