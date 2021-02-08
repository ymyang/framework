package com.ymyang.service.wx.impl;

import com.ymyang.framework.beans.exception.BusinessException;
import com.ymyang.service.wx.WxMemberService;
import com.ymyang.entity.wx.WxMemberEntity;
import com.ymyang.param.wx.WxMemberQueryParam;
import com.ymyang.param.wx.WxMemberCreateParam;
import com.ymyang.param.wx.WxMemberUpdateParam;
import com.ymyang.mapper.wx.WxMemberMapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 微信用户信息
 *
 * @author ymyang
 * @email ymyang@163.com
 * @date 2021-02-08 15:38:51
 */
@Service("wx.wxMemberService")
public class WxMemberServiceImpl extends ServiceImpl<WxMemberMapper, WxMemberEntity> implements WxMemberService {

	@Override
	public IPage<WxMemberEntity> page(WxMemberQueryParam param) {
		IPage<WxMemberEntity> page = new Page<>(param.getPageIndex(), param.getPageSize());
		WxMemberEntity entity = new WxMemberEntity();
		param.copyPropertiesTo(entity, false);
		QueryWrapper<WxMemberEntity> wrapper = Wrappers.query(entity);

		return page(page, wrapper);
	}


	@Override
	public WxMemberEntity create(WxMemberCreateParam param) {
		WxMemberEntity entity = new WxMemberEntity();
		param.copyPropertiesTo(entity, false);

		boolean f = save(entity);

		if (!f) {
			throw new BusinessException("创建失败");
		}

		return entity;
	}

	@Override
	public WxMemberEntity update(WxMemberUpdateParam param) {
		WxMemberEntity entity = getById(param.getId());
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
