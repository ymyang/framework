package com.ymyang.generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 配置
 */
@Configuration
@ConfigurationProperties(prefix = "generator")
public class GeneratorConfig {

    // 作者
    private String author;

    // Email
    private String email;

    private String applicationName;

    // 是否文件覆盖
    private Boolean overwrite = false;

    // 控制器增加shiro权限注解
    private Boolean shiroAnnotation = true;

    // 控制器所属modules绝对路径
    private String controllerPath;
    private String controllerPackageName;

    // service所属modules绝对路径
    private String servicePath;
    private String servicePackageName;

    // 实体类
    private String entityPath;
    private String entityPackageName;

    // mybatis mapper类
    private String mapperPath;
    private String mapperPackageName;

    // 控制器中的查询参数对象
    private String queryParamPath;
    private String queryParamPackageName;

    // 控制器中的创建参数对象
    private String createParamPath;
    private String createParamPackageName;

    // 控制器中的编辑参数对象
    private String updateParamPath;
    private String updateParamPackageName;

    // 控制器中的返回值对象
    private String dtoPath;
    private String dtoPackageName;

    // feign客户端
    private String feignClientPath;
    private String feignClientPackageName;

    // 菜单sql
    private String menuSqlPath;

    // 表前缀
    private String tablePrefix;

    private List<Modules> modules;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public Boolean getShiroAnnotation() {
        return shiroAnnotation;
    }

    public void setShiroAnnotation(Boolean shiroAnnotation) {
        this.shiroAnnotation = shiroAnnotation;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public String getControllerPackageName() {
        return controllerPackageName;
    }

    public void setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getQueryParamPath() {
        return queryParamPath;
    }

    public void setQueryParamPath(String queryParamPath) {
        this.queryParamPath = queryParamPath;
    }

    public String getQueryParamPackageName() {
        return queryParamPackageName;
    }

    public void setQueryParamPackageName(String queryParamPackageName) {
        this.queryParamPackageName = queryParamPackageName;
    }

    public String getCreateParamPath() {
        return createParamPath;
    }

    public void setCreateParamPath(String createParamPath) {
        this.createParamPath = createParamPath;
    }

    public String getCreateParamPackageName() {
        return createParamPackageName;
    }

    public void setCreateParamPackageName(String createParamPackageName) {
        this.createParamPackageName = createParamPackageName;
    }

    public String getUpdateParamPath() {
        return updateParamPath;
    }

    public void setUpdateParamPath(String updateParamPath) {
        this.updateParamPath = updateParamPath;
    }

    public String getUpdateParamPackageName() {
        return updateParamPackageName;
    }

    public void setUpdateParamPackageName(String updateParamPackageName) {
        this.updateParamPackageName = updateParamPackageName;
    }

    public String getDtoPath() {
        return dtoPath;
    }

    public void setDtoPath(String dtoPath) {
        this.dtoPath = dtoPath;
    }

    public String getDtoPackageName() {
        return dtoPackageName;
    }

    public void setDtoPackageName(String dtoPackageName) {
        this.dtoPackageName = dtoPackageName;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getMenuSqlPath() {
        return menuSqlPath;
    }

    public void setMenuSqlPath(String menuSqlPath) {
        this.menuSqlPath = menuSqlPath;
    }

    public List<Modules> getModules() {
        return modules;
    }

    public void setModules(List<Modules> modules) {
        this.modules = modules;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getMapperPackageName() {
        return mapperPackageName;
    }

    public void setMapperPackageName(String mapperPackageName) {
        this.mapperPackageName = mapperPackageName;
    }

    public String getFeignClientPath() {
        return feignClientPath;
    }

    public void setFeignClientPath(String feignClientPath) {
        this.feignClientPath = feignClientPath;
    }

    public String getFeignClientPackageName() {
        return feignClientPackageName;
    }

    public void setFeignClientPackageName(String feignClientPackageName) {
        this.feignClientPackageName = feignClientPackageName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
