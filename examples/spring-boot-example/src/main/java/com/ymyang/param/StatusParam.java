package com.ymyang.param;

import com.ymyang.framework.beans.PojoDuplicate;
import com.ymyang.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value = "sys.StatusParam", description = "状态参数")
@Data
public class StatusParam implements PojoDuplicate {

    @NotNull(message = "id 不能为空")
    @ApiModelProperty(value = "id", name = "id", required = true)
    private Long id;

    @NotNull(message = "状态 不能为空")
    @ApiModelProperty(value = "状态，1：启用，2：禁用", name = "status", required = true)
    private Status status;

}
