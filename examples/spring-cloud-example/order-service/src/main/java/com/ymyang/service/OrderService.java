package com.ymyang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.OrderEntity;

public interface OrderService extends IService<OrderEntity> {

    void order(OrderEntity param);

}
