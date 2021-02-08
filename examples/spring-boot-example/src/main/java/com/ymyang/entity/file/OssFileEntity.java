package com.ymyang.entity.file;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import lombok.*;

import com.ymyang.framework.mybatisplus.BaseModel;

/**
 * 
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:41:15
 */
@TableName("t_oss_file")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OssFileEntity extends BaseModel<OssFileEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 文件名称
	 */
	private String name;

	/**
	 * 文件地址
	 */
	private String url;

	/**
	 * 删除标识,0:有效,1:删除
	 */
	@TableLogic
	private Integer deleted;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 创建者id
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long creatorId;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String creatorName;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime modifyTime;

	/**
	 * 修改者id
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long modifierId;

	/**
	 * 修改者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String modifierName;

}
