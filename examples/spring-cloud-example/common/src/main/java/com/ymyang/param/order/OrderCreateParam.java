package com.ymyang.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import com.ymyang.framework.beans.PojoDuplicate;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;


/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:22:17
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "order.OrderCreateParam", description = "")
public class OrderCreateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

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
