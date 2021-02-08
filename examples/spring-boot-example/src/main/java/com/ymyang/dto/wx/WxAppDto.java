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
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Getter
@Setter
@ToString
@ApiModel(value="wx.WxAppDto", description="微信公众号")
public class WxAppDto implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "id", required = true)
	private Long id;

	/**
	 * 名称
	 */
    @ApiModelProperty(value = "名称", name = "name", required = true)
	private String name;

	/**
	 * 微信AppId
	 */
    @ApiModelProperty(value = "微信AppId", name = "appId", required = true)
	private String appId;

	/**
	 * 微信AppSecret
	 */
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
