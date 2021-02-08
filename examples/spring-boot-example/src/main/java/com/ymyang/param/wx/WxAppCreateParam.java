package com.ymyang.param.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import com.ymyang.PojoDuplicate;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import java.time.LocalDateTime;

/**
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:31:21
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "wx.WxAppCreateParam", description = "微信公众号")
public class WxAppCreateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@NotNull(message = "名称 不能为空")
	@Length(min = 1, max = 50, message = "名称 长度需要在1和50之间")
	@ApiModelProperty(value = "名称", name = "name", required = true)
	private String name;

	/**
	 * 微信AppId
	 */
	@NotNull(message = "微信AppId 不能为空")
	@Length(min = 1, max = 30, message = "微信AppId 长度需要在1和30之间")
	@ApiModelProperty(value = "微信AppId", name = "appId", required = true)
	private String appId;

	/**
	 * 微信AppSecret
	 */
	@NotNull(message = "微信AppSecret 不能为空")
	@Length(min = 1, max = 50, message = "微信AppSecret 长度需要在1和50之间")
	@ApiModelProperty(value = "微信AppSecret", name = "appSecret", required = true)
	private String appSecret;

	/**
	 * Token
	 */
	@ApiModelProperty(value = "Token", name = "token")
	private String token;

	/**
	 * EncodingAESKey
	 */
	@ApiModelProperty(value = "EncodingAESKey", name = "aesKey")
	private String aesKey;

	/**
	 * 状态,1:启用,2:禁用
	 */
	@ApiModelProperty(value = "状态,1:启用,2:禁用", name = "status")
	private Integer status;


}
