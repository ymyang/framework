package com.ymyang.service.wx.impl;

import com.ymyang.framework.beans.exception.BusinessException;
import com.ymyang.service.wx.WxAppService;
import com.ymyang.entity.wx.WxAppEntity;
import com.ymyang.param.wx.WxAppQueryParam;
import com.ymyang.param.wx.WxAppCreateParam;
import com.ymyang.param.wx.WxAppUpdateParam;
import com.ymyang.mapper.wx.WxAppMapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 微信公众号
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Service("wx.wxAppService")
public class WxAppServiceImpl extends ServiceImpl<WxAppMapper, WxAppEntity> implements WxAppService {

	@Override
	public IPage<WxAppEntity> page(WxAppQueryParam param) {
		IPage<WxAppEntity> page = new Page<>(param.getPageIndex(), param.getPageSize());
		WxAppEntity entity = new WxAppEntity();
		param.copyPropertiesTo(entity, false);
		QueryWrapper<WxAppEntity> wrapper = Wrappers.query(entity);

		return page(page, wrapper);
	}


	@Override
	public WxAppEntity create(WxAppCreateParam param) {
		WxAppEntity entity = new WxAppEntity();
		param.copyPropertiesTo(entity, false);

		boolean f = save(entity);

		if (!f) {
			throw new BusinessException("创建失败");
		}

		return entity;
	}

	@Override
	public WxAppEntity update(WxAppUpdateParam param) {
		WxAppEntity entity = getById(param.getId());
		if (entity == null) {
			throw new BusinessException("未查询到此对象");
		}

		param.copyPropertiesTo(entity, false);
		boolean f = updateById(entity);

		if (!f) {
			throw new BusinessException("修改失败");
		}

		return entity;
	}
}
