package com.ymyang.dto;

import com.ymyang.framework.beans.PojoDuplicate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUserDto implements PojoDuplicate {

    @ApiModelProperty(value = "用户ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "姓名/昵称", name = "name")
    private String name;

    @ApiModelProperty(value = "账号", name = "account")
    private String account;

    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "头像", name = "headImgUrl")
    private String headImgUrl;

}
