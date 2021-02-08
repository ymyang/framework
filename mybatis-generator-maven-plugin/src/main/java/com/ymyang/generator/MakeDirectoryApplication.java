package com.ymyang.generator;

import com.ymyang.generator.utils.GenerationUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

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
@Mojo(name = "mkdir")
public class MakeDirectoryApplication extends AbstractMojo {

    @Parameter(defaultValue = "${basedir}")
    private File baseDir;


    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private String sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}", readonly = true, required = true)
    private File testSourceDir;

    @Override
    public void execute() {

        File sourceFile = new File(sourceDirectory);
        sourceFile.mkdirs();

        String resourceDirectory = GenerationUtils.resourceDirectoryFromSourceDirectory(sourceDirectory);
        File resourceFile = new File(resourceDirectory);
        resourceFile.mkdirs();

        testSourceDir.mkdirs();
    }

}
