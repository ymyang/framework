package com.ymyang.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ymyang.framework.beans.exception.BusinessException;
import com.ymyang.framework.web.annotations.Anonymous;
import com.ymyang.framework.web.shiro.JWTUser;
import com.ymyang.framework.web.shiro.JWTUtil;
import com.ymyang.framework.web.utils.SpringContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AnonymousInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String token = request.getHeader("Authorization");
		JWTUser jwtUser = null;
		if (StringUtils.isNotBlank(token)) {
			jwtUser = JWTUtil.getJWTUser(token);
		}
		if (jwtUser != null) {
			SpringContextHolder.setLoginUser(jwtUser);
		}

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Anonymous controllerAnonymous = handlerMethod.getBeanType().getAnnotation(Anonymous.class);
            if (controllerAnonymous != null) {
                return true;
            }
            Anonymous methodAnnotation = handlerMethod.getMethodAnnotation(Anonymous.class);
            if (methodAnnotation != null) {
                return true;
            }
        } else {
            return true;
        }
		
		if (StringUtils.isBlank(token) || jwtUser == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			throw new BusinessException("用户未登录");
		}
		
		if (!JWTUtil.verify(token, jwtUser)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			throw new BusinessException("用户登录状态过期");
		}

		return true;
	}

}
