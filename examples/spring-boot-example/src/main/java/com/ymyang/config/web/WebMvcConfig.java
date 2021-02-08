package com.ymyang.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private AnonymousInterceptor anonymousInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(anonymousInterceptor)
			.excludePathPatterns("/swagger-resources", "/v2/api-docs", "/v2/api-docs-ext")
			.addPathPatterns("/**");
	}

}
