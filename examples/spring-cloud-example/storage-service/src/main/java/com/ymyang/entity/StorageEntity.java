package com.ymyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_storage")
public class StorageEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * commodity code.
     */
    public String commodityCode;

    /**
     * count.
     */
    public int count;

}
