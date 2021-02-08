package com.ymyang.dto.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.ymyang.framework.beans.PojoDuplicate;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:41:15
 */
@Getter
@Setter
@ToString
@ApiModel(value="file.OssFileDto", description="")
public class OssFileDto implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
    @ApiModelProperty(value = "", name = "id", required = true)
	private Long id;

	/**
	 * 文件名称
	 */
    @ApiModelProperty(value = "文件名称", name = "name", required = true)
	private String name;

	/**
	 * 文件地址
	 */
    @ApiModelProperty(value = "文件地址", name = "url", required = true)
	private String url;

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
