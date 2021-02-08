package com.ymyang.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymyang.entity.order.OrderEntity;
import com.ymyang.param.order.OrderCreateParam;

public interface OrderService extends IService<OrderEntity> {

    void order(OrderCreateParam param);

}
