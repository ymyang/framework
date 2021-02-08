package com.ymyang.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.ymyang.framework.beans.PojoDuplicate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:22:17
 */
@Getter
@Setter
@ToString
@ApiModel(value="order.OrderDto", description="")
public class OrderDto implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "id", required = true)
	private Integer id;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "userId")
	private Integer userId;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "commodityCode")
	private String commodityCode;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "count")
	private Integer count;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "money")
	private Integer money;

}
