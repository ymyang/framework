package com.ymyang.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.ymyang.framework.beans.PojoDuplicate;


/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 16:22:17
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "order.OrderQueryParam", description = "")
public class OrderQueryParam implements PojoDuplicate {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前页码", name = "pageIndex", example = "1")
    private int pageIndex = 1;

    @ApiModelProperty(value = "每页数量", name = "pageSize", example = "20")
    private int pageSize = 20;

    /**
     * 
     */
    @ApiModelProperty(value = "", name = "userId")
    private Integer userId;

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

    /**
     * 
     */
    @ApiModelProperty(value = "", name = "money")
    private Integer money;

}
