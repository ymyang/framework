package com.ymyang.dto.file;

import com.ymyang.framework.beans.PojoDuplicate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FileDto", description = "文件")
public class FileDto implements PojoDuplicate {

    @ApiModelProperty(value = "名称", name = "name")
    private String name;

    @ApiModelProperty(value = "文件地址", name = "url")
    private String url;
}
