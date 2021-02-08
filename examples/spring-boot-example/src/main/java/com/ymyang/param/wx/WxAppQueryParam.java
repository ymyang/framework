package com.ymyang.param.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ymyang.PojoDuplicate;

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
@ApiModel(value = "wx.WxAppQueryParam", description = "微信公众号")
public class WxAppQueryParam implements PojoDuplicate {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", name = "pageIndex", example = "1")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页数量", name = "pageSize", example = "20")
    private int pageSize = 20;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    /**
     * 微信AppId
     */
    @ApiModelProperty(value = "微信AppId", name = "appId")
    private String appId;

    /**
     * 微信AppSecret
     */
    @ApiModelProperty(value = "微信AppSecret", name = "appSecret")
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
