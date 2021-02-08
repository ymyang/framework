package com.ymyang.config.swagger;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config {

	@Bean
	@Order(1)
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("公共服务")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.kaisagroup.controller.common"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(setHeaderToekn());
	}

	@Bean
	@Order(2)
	public Docket createBackRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("后台服务")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.kaisagroup.controller.back"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(setHeaderToekn());
	}

	@Bean
	@Order(3)
	public Docket createFrontRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.groupName("前端服务")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.kaisagroup.controller.api"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(setHeaderToekn());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("城更拆迁业主服务平台API文档")
				.description("城更拆迁业主服务平台API文档")
				.license("科技集团")
				.contact(new Contact("科技集团", "", ""))
				.version("1.0")
				.build();
	}

	private List<Parameter> setHeaderToekn() {
		ParameterBuilder authorizationBuilder = new ParameterBuilder();
		authorizationBuilder.name("Authorization")
			.description("授权登陆token")
			.modelRef(new ModelRef("string"))
			.parameterType("header")
			.required(true);

		return Lists.newArrayList(authorizationBuilder.build());
	}
}
