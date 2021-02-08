package com.ymyang.param.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ymyang.framework.beans.PojoDuplicate;

import java.time.LocalDateTime;

/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:41:15
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "file.OssFileQueryParam", description = "")
public class OssFileQueryParam implements PojoDuplicate {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", name = "pageIndex", example = "1")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页数量", name = "pageSize", example = "20")
    private int pageSize = 20;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称", name = "name")
    private String name;

    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址", name = "url")
    private String url;

}
