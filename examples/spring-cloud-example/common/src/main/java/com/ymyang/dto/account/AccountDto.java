package com.ymyang.dto.account;

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
 * @date 2021-02-08 16:08:52
 */
@Getter
@Setter
@ToString
@ApiModel(value="account.AccountDto", description="")
public class AccountDto implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "id", required = true)
	private Integer id;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "money")
	private Integer money;

}
