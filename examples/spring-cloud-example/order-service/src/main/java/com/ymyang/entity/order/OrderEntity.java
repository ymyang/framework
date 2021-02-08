package com.ymyang.entity.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

import com.ymyang.framework.mybatisplus.BaseModel;

/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:22:17
 */
@TableName("t_order")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends BaseModel<OrderEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 
	 */
	private Integer userId;

	/**
	 * 
	 */
	private String commodityCode;

	/**
	 * 
	 */
	private Integer count;

	/**
	 * 
	 */
	private Integer money;

}
