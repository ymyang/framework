package com.ymyang.param.wx;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ymyang.PojoDuplicate;

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
@ApiModel(value = "wx.WxMemberQueryParam", description = "微信用户信息")
public class WxMemberQueryParam implements PojoDuplicate {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", name = "pageIndex", example = "1")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页数量", name = "pageSize", example = "20")
    private int pageSize = 20;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", name = "memberInfoId")
    private Long memberInfoId;

    /**
     * 微信AppId
     */
    @ApiModelProperty(value = "微信AppId", name = "appId")
    private String appId;

    /**
     * 微信openid
     */
    @ApiModelProperty(value = "微信openid", name = "openid")
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
