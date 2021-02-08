package com.ymyang.framework.web.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShiroConfiguration {

    @Bean
    public CredentialsMatcher credentialMatcher() {
        return new CustomCredentialMatcher();
    }

    @Bean
    @ConditionalOnMissingBean(AuthorizingRealm.class)
    public AuthRealm authRealm(CredentialsMatcher credentialsMatcher) {
        System.err.println("common authRealm");
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
        authRealm.setCredentialsMatcher(credentialsMatcher);
        return authRealm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(AuthorizingRealm authRealm) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 使用自己的realm
        securityManager.setRealm(authRealm);

        // todo 暂时用内存缓存
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

    @Bean("shiroFilter")
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        //设置我们自定义的JWT过滤器
        filterMap.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/jwt/login");

        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        filterRuleMap.put("/404", "anon");
        filterRuleMap.put("/500", "anon");
        filterRuleMap.put("/error", "anon");
//        filterRuleMap.put("/jwt/login", "anon");

        // swagger url
//        filterRuleMap.put("/swagger-resources/**", "anon");
//        filterRuleMap.put("/webjars/**", "anon");
//        filterRuleMap.put("/v2/api-docs", "anon");
//        filterRuleMap.put("/v2/api-docs-ext", "anon");

        // todo 其他anon的接口直接用@RequiresGuest注解即可

        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


}
