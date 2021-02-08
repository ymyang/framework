package com.ymyang.param.storage;

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
 * @date 2021-02-08 16:18:11
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "storage.StorageCreateParam", description = "")
public class StorageCreateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

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


}
