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
 * 微信用户信息
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@TableName("t_wx_member")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WxMemberEntity extends BaseModel<WxMemberEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户id
	 */
	private Long memberInfoId;

	/**
	 * 微信AppId
	 */
	private String appId;

	/**
	 * 微信openid
	 */
	private String openid;

	/**
	 * 微信unionid
	 */
	private String unionid;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 头像
	 */
	private String headImgUrl;

	/**
	 * 性别
	 */
	private Integer sex;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省份
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

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
