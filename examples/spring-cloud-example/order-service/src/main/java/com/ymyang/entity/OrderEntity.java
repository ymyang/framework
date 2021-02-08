package com.ymyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order")
public class OrderEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * user id.
     */
    public Integer userId;

    /**
     * commodity code.
     */
    public String commodityCode;

    /**
     * count.
     */
    public int count;

    /**
     * money.
     */
    public int money;

}
