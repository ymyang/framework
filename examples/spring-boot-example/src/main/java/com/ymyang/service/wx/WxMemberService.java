package com.ymyang.service.wx;

import com.ymyang.entity.wx.WxMemberEntity;
import com.ymyang.param.wx.WxMemberQueryParam;
import com.ymyang.param.wx.WxMemberCreateParam;
import com.ymyang.param.wx.WxMemberUpdateParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 微信用户信息
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
public interface WxMemberService extends IService<WxMemberEntity> {

	IPage<WxMemberEntity> page(WxMemberQueryParam param);

	WxMemberEntity create(WxMemberCreateParam param);

	WxMemberEntity update(WxMemberUpdateParam param);
}

