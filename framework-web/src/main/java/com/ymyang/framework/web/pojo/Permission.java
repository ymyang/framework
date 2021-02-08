package com.ymyang.framework.web.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Permission {

    @ApiModelProperty(value = "id", name = "id", required = true)
    private String id;

    @ApiModelProperty(value = "name", name = "name", required = true)
    private String name;

    @ApiModelProperty(value = "parentId", name = "parentId", required = true)
    private String parentId;

    @ApiModelProperty(value = "isNavMenu", name = "isNavMenu", required = true)
    private Boolean isNavMenu;

    @ApiModelProperty(value = "level", name = "level", required = true)
    private Integer level;

    @ApiModelProperty(value = "path", name = "path", required = true)
    private String path;

    @ApiModelProperty(value = "route", name = "route")
    private String route;

    @ApiModelProperty(value = "icon", name = "icon")
    private String icon;
}
