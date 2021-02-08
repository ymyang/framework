package com.ymyang.test;

import com.ymyang.dto.file.FileDto;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.*;

public class VelocityTest {

    public static void main(String[] args) {
        try {
            Properties prop = new Properties();
            prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(prop);
            String tplFile = "template/investment_mail.vm";
            Map<String, Object> map = new HashMap<>();
            map.put("contactName", "联系人姓名");
            FileDto fileDto = new FileDto();
            fileDto.setName("测试文件");
            fileDto.setUrl("");
            List<FileDto> files = new ArrayList<>();
            files.add(fileDto);
            map.put("attachments", files);
            VelocityContext context = new VelocityContext(map);
            Template template = Velocity.getTemplate(tplFile, "UTF-8");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            String str = writer.toString();
            System.out.println(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
