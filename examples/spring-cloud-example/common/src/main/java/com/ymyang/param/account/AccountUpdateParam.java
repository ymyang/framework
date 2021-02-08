package com.ymyang.param.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.ymyang.framework.beans.PojoDuplicate;


/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:10:21
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "account.AccountUpdateParam", description = "")
public class AccountUpdateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@NotNull(message = " 不能为空")
	@ApiModelProperty(value = "", name = "id", required = true)
	private Integer id;

	/**
	 * 
	 */
	@ApiModelProperty(value = "", name = "money")
	private Integer money;


}
