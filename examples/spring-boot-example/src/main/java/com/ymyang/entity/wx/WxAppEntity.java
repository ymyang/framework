package com.ymyang.entity.wx;

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
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@TableName("t_wx_app")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WxAppEntity extends BaseModel<WxAppEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 微信AppId
	 */
	private String appId;

	/**
	 * 微信AppSecret
	 */
	private String appSecret;

	/**
	 * Token
	 */
	private String token;

	/**
	 * EncodingAESKey
	 */
	private String aesKey;

	/**
	 * 状态,1:启用,2:禁用
	 */
	private Integer status;

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
