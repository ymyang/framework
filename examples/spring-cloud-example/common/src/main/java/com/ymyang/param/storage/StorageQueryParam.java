package com.ymyang.param.storage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ymyang.framework.beans.PojoDuplicate;


/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:18:11
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "storage.StorageQueryParam", description = "")
public class StorageQueryParam implements PojoDuplicate {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", name = "pageIndex", example = "1")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页数量", name = "pageSize", example = "20")
    private int pageSize = 20;

    /**
     * 
     */
    @ApiModelProperty(value = "", name = "commodityCode")
    private String commodityCode;

    /**
     * 
     */
    @ApiModelProperty(value = "", name = "count")
    private Integer count;

}
