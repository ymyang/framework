package com.ymyang.config.mybatis;

import java.time.LocalDateTime;

import com.ymyang.framework.web.shiro.JWTUser;
import com.ymyang.framework.web.utils.SpringContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject obj) {
		this.setFieldValByName("createTime", LocalDateTime.now(), obj);
		this.setFieldValByName("modifyTime", LocalDateTime.now(), obj);

		JWTUser jwtUser = SpringContextHolder.getLoginUser();
		if (jwtUser != null) {
//			Object tenantIdVal = this.getFieldValByName("tenantId", obj);
//			if (tenantIdVal == null || tenantIdVal.equals(0)) {
//				this.setFieldValByName("tenantId", jwtUser.getTenantId(), obj);
//			}
			this.setFieldValByName("creatorId", jwtUser.getId(), obj);
			this.setFieldValByName("creatorName", jwtUser.getUsername(), obj);
			this.setFieldValByName("modifierId", jwtUser.getId(), obj);
			this.setFieldValByName("modifierName", jwtUser.getUsername(), obj);
		} else {
			this.setFieldValByName("creatorId", 0L, obj);
			this.setFieldValByName("creatorName", "", obj);
			this.setFieldValByName("modifierId", 0L, obj);
			this.setFieldValByName("modifierName", "", obj);
		}
	}

	@Override
	public void updateFill(MetaObject obj) {
		this.setFieldValByName("modifyTime", LocalDateTime.now(), obj);
		JWTUser jwtUser = SpringContextHolder.getLoginUser();
		if (jwtUser != null) {
			this.setFieldValByName("modifierId", jwtUser.getId(), obj);
			this.setFieldValByName("modifierName", jwtUser.getUsername(), obj);
		} else {
			this.setFieldValByName("modifierId", 0L, obj);
			this.setFieldValByName("modifierName", "", obj);
		}
	}

}
