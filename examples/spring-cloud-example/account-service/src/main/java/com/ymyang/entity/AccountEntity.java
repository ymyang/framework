package com.ymyang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_account")
public class AccountEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer money;

}
