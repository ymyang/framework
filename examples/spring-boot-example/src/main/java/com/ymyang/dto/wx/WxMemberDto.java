package com.ymyang.dto.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.ymyang.framework.beans.PojoDuplicate;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信用户信息
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Getter
@Setter
@ToString
@ApiModel(value="wx.WxMemberDto", description="微信用户信息")
public class WxMemberDto implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "id", required = true)
	private Long id;

	/**
	 * 用户id
	 */
    @ApiModelProperty(value = "用户id", name = "memberInfoId")
	private Long memberInfoId;

	/**
	 * 微信AppId
	 */
    @ApiModelProperty(value = "微信AppId", name = "appId", required = true)
	private String appId;

	/**
	 * 微信openid
	 */
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

	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间", name = "createTime", required = true, example = "2019-01-01 09:01:01")
	private LocalDateTime createTime;

	/**
	 * 创建者id
	 */
    @ApiModelProperty(value = "创建者id", name = "creatorId")
	private Long creatorId;

	/**
	 * 创建者
	 */
    @ApiModelProperty(value = "创建者", name = "creatorName")
	private String creatorName;

	/**
	 * 修改时间
	 */
    @ApiModelProperty(value = "修改时间", name = "modifyTime", required = true, example = "2019-01-01 09:01:01")
	private LocalDateTime modifyTime;

	/**
	 * 修改者id
	 */
    @ApiModelProperty(value = "修改者id", name = "modifierId")
	private Long modifierId;

	/**
	 * 修改者
	 */
    @ApiModelProperty(value = "修改者", name = "modifierName")
	private String modifierName;

}
