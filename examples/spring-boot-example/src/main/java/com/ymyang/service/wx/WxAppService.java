package com.ymyang.service.wx;

import com.ymyang.entity.wx.WxAppEntity;
import com.ymyang.param.wx.WxAppQueryParam;
import com.ymyang.param.wx.WxAppCreateParam;
import com.ymyang.param.wx.WxAppUpdateParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
public interface WxAppService extends IService<WxAppEntity> {

	IPage<WxAppEntity> page(WxAppQueryParam param);

	WxAppEntity create(WxAppCreateParam param);

	WxAppEntity update(WxAppUpdateParam param);
}

