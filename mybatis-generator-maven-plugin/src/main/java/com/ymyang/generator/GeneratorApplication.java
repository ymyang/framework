package com.ymyang.generator;

import com.ymyang.generator.config.GeneratorConfig;
import com.ymyang.generator.service.CustomGeneratorService;
import com.ymyang.generator.utils.GenerationUtils;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileUrlResource;

import java.io.File;
import java.util.List;

/**
 * liyug@kaisagroup.com
 *
 * 使用说明：
 * 1、pom.xml 增加插件
 * <build>
 *         <plugins>
 *             <plugin>
 *                 <groupId>cc.gigahome.generator</groupId>
 *                 <artifactId>mybatis-plus-maven-plugin</artifactId>
 *                 <version>1.0.0</version>
 *             </plugin>
 *         </plugins>
 *  </build>
 *  2、src/main/resources创建配置文件generator.yaml
 *  3、执行：mvn mybatis-plus:generate [-Dconfig=generator.yaml]
 */
@SpringBootApplication
@Mojo(name = "generate")
public class GeneratorApplication extends AbstractMojo {

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.resources}", readonly = true, required = true)
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private String sourceDirectory;

    @Parameter(defaultValue = "${project.build.testResources}", readonly = true, required = true)
    private List<Resource> testResources;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
    private File testSourceDir;

    @Parameter(property = "config", defaultValue = "generator.yaml")
    private String config;

    // equals(this.config)
    private static String configFile;

    // equals(this.sourceDirectory)
    private static String sourceDir;


    @Override
    public void execute() {

        // 为运行spring boot而初始化的变量
        String[] args = {};
        configFile = this.config;
        sourceDir = this.sourceDirectory;

        // 初始化spring boot
        SpringApplication springApplication = new SpringApplication(GeneratorApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext context = springApplication.run(args);
        CustomGeneratorService generatorService = context.getBean("customGeneratorService", CustomGeneratorService.class);
        GeneratorConfig generatorConfig = (GeneratorConfig) context.getBean("generatorConfig");
        // 执行生成代码
        try {
            generatorService.generatorCode(generatorConfig);
            System.out.println("代码生成成功！");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            context.close();
        }


    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        FileUrlResource[] configs = GenerationUtils.getGeneratorConfig(configFile, sourceDir);
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(configs);
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}
