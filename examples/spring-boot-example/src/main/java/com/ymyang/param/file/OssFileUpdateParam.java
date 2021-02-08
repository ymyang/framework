package com.ymyang.param.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
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
@ApiModel(value = "file.OssFileUpdateParam", description = "")
public class OssFileUpdateParam implements PojoDuplicate {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@NotNull(message = " 不能为空")
	@ApiModelProperty(value = "", name = "id", required = true)
	private Long id;

	/**
	 * 文件名称
	 */
	@NotNull(message = "文件名称 不能为空")
	@Length(min = 1, max = 255, message = "文件名称 长度需要在1和255之间")
	@ApiModelProperty(value = "文件名称", name = "name", required = true)
	private String name;

	/**
	 * 文件地址
	 */
	@NotNull(message = "文件地址 不能为空")
	@Length(min = 1, max = 255, message = "文件地址 长度需要在1和255之间")
	@ApiModelProperty(value = "文件地址", name = "url", required = true)
	private String url;


}
