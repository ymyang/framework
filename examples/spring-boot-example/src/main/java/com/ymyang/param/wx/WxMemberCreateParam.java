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
 * 微信用户信息
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
@ApiModel(value = "wx.WxMemberCreateParam", description = "微信用户信息")
public class WxMemberCreateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id", name = "memberInfoId")
	private Long memberInfoId;

	/**
	 * 微信AppId
	 */
	@NotNull(message = "微信AppId 不能为空")
	@Length(min = 1, max = 30, message = "微信AppId 长度需要在1和30之间")
	@ApiModelProperty(value = "微信AppId", name = "appId", required = true)
	private String appId;

	/**
	 * 微信openid
	 */
	@NotNull(message = "微信openid 不能为空")
	@Length(min = 1, max = 50, message = "微信openid 长度需要在1和50之间")
	@ApiModelProperty(value = "微信openid", name = "openid", required = true)
	private String openid;

	/**
	 * 微信unionid
	 */
	@ApiModelProperty(value = "微信unionid", name = "unionid")
	private String unionid;

	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称", name = "nickname")
	private String nickname;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像", name = "headImgUrl")
	private String headImgUrl;

	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别", name = "sex")
	private Integer sex;

	/**
	 * 国家
	 */
	@ApiModelProperty(value = "国家", name = "country")
	private String country;

	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份", name = "province")
	private String province;

	/**
	 * 城市
	 */
	@ApiModelProperty(value = "城市", name = "city")
	private String city;


}
