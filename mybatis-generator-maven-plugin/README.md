# mysql逆向生成mybatis

## 一、在portal模块的`pom.xml`中增加下面配置
```xml
<build>
    <plugins>
        <plugin>
            <groupId>cc.gigahome.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>2.2.0</version>
        </plugin>
    </plugins>
</build>
``` 
## 二、在portal模块的resources配置目录中增加`src/main/resources/generator.yml`
示例：
```yaml
# mysql
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db_name?useUnicode=true&characterEncoding=UTF-8&useSSL=false
    username: root
    password: root
  jackson:
    time-zone: GMT+8
    date-format: yyyy-MM-dd HH:mm:ss
  resources:
    static-locations: classpath:/static/,classpath:/views/

# Mybatis配置
mybatis:
  mapperLocations: classpath:mapper/**/*.xml

## mysql逆向生成配置
generator:

  # 作者，会标注接口文档的负责人
  author:

  # 邮箱
  email:

  # 当前应用的微服务应用名，会标注在Feign Client上
  applicationName:

  # 是否文件覆盖
  overwrite: false

  # 控制器增加shiro权限注解
  shiroAnnotation: true

  # 控制器所属modules绝对路径，若path和packageName为空时，则不生成，最终目录：${controllerPath}/src/main/java/${controllerPackageName}/${modulesName}
  controllerPath:
  controllerPackageName:

  # 控制器中的查询参数对象，若path和packageName为空时，则不生成，最终目录：${queryParamPath}/src/main/java/${queryParamPackageName}/${modulesName}
  queryParamPath:
  queryParamPackageName:

  # 控制器中的新建参数对象，若path和packageName为空时，则不生成，最终目录：${createParamPath}/src/main/java/${createParamPackageName}/${modulesName}
  createParamPath:
  createParamPackageName:

  # 控制器中的更新参数对象，若path和packageName为空时，则不生成，最终目录：${updateParamPath}/src/main/java/${updateParamPackageName}/${modulesName}
  updateParamPath:
  updateParamPackageName:

  # 控制器中的返回值对象，若path和packageName为空时，则不生成，最终目录：${dtoPath}/src/main/java/${dtoPackageName}/${modulesName}
  dtoPath:
  dtoPackageName:

  # service所属modules绝对路径，若path和packageName为空时，则不生成，最终目录：${servicePath}/src/main/java/${servicePackageName}/${modulesName}
  servicePath:
  servicePackageName:

  # 实体类，若path和packageName为空时，则不生成，最终目录：${entityPath}/src/main/java/${entityPackageName}/${modulesName}
  entityPath:
  entityPackageName:

  # mybatis mapper类，若path和packageName为空时，则不生成，最终目录：${mapperPath}/src/main/java/${mapperPackageName}/${modulesName}
  mapperPath:
  mapperPackageName:

  # feign客户端，若path和packageName为空时，则不生成，最终目录：${feignClientPath}/src/main/java/${feignClientPackageName}/${modulesName}
  feignClientPath:
  feignClientPackageName:

  # 菜单sql 最终目录：${feignClientPath}/src/main/resources/sql/${modulesName}
  menuSqlPath:

  # 表前缀
  tablePrefix: sys_

  modules:
    # 模块名
    - name: sys
      # 需要生成的表名列表
      tables:
        - sys_user
        - sys_menu

```

## 三、路径配置建议

### 1、建议模块


- <应用名>-dto

只存在各种DTO： `dtoPath`，`updateParamPath`，`createParamPath`，`queryParamPath`，

此模块所需依赖
```xml
<dependencies>
    <dependency>
        <groupId>com.ymyang.framework</groupId>
        <artifactId>beans</artifactId>
        <version>2.2.0</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```
- 控制器对外提供模块：<应用名>-portal

`controllerPath` 所在模块

此模块需依赖
```xml
<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>应用名-dto</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>应用名-service</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.ymyang.framework</groupId>
        <artifactId>web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-actuator</artifactId>
    </dependency>
</dependencies>

```

- feign接口模块：<应用名>-feign-client
`feignClientPath` 所在目录
微服务feign接口 所在模块
此模块所需依赖
```xml
<dependencies>
    <dependency>
        <groupId>com.ymyang.framework</groupId>
        <artifactId>beans</artifactId>
        <version>2.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>应用名-dto</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-extension</artifactId>
    </dependency>
</dependencies>
```

- <应用名>-service

此模块存放的模块：`servicePath`，`menuSqlPath`，`mapperPath`，`entityPath`

此模块所需依赖
```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>com.ymyang.framework</groupId>
        <artifactId>mybatis-plus</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
    </dependency>
</dependencies>
```