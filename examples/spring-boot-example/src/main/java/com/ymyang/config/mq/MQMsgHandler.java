package com.ymyang.config.mq;

import com.ymyang.dto.file.FileDto;
import com.ymyang.entity.investment.InvestmentContactEntity;
import com.ymyang.entity.member.WxMemberEntity;
import com.ymyang.mapper.investment.InvestmentContactMapper;
import com.ymyang.mapper.member.WxMemberMapper;
import com.ymyang.param.investment.CooperationProjectCreateParam;
import com.ymyang.service.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class MQMsgHandler {

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private FileService fileService;

    @Autowired
    private WxMemberMapper wxMemberMapper;

    @Autowired
    private InvestmentContactMapper investmentContactMapper;

    @Autowired
    private JavaMailSender mailSender;

    public void uploadWxHeadImgUrl(WxMemberEntity entity) {
        File file = fileService.downloadImg(entity.getHeadImgUrl());
        if (file != null) {
            FileDto fileDto = fileService.upload(file);
            if (fileDto != null && StringUtils.isNotBlank(fileDto.getUrl())) {
                WxMemberEntity updateEntity = new WxMemberEntity();
                updateEntity.setId(entity.getId());
                updateEntity.setHeadImgUrl(fileDto.getUrl());
                wxMemberMapper.updateById(updateEntity);
            }
        }
    }

    public void sendMail(CooperationProjectCreateParam param) {
        try {
            List<InvestmentContactEntity> contacts = investmentContactMapper.selectList(null);
            String[] mailTo = new String[contacts.size()];
            for (int i = 0; i < contacts.size(); i++) {
                mailTo[i] = contacts.get(i).getEmail();
            }

            log.info("mailTo: " + mailTo);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(this.username);

            helper.setTo(mailTo);
            helper.setSubject("投资合作项目信息填报通知");

            Properties prop = new Properties();
            prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(prop);

            String tplFile = "template/investment_mail.vm";
            Map<String, Object> map = new HashMap<>();
            map.put("name", param.getName());
            map.put("address", param.getAddress());
            map.put("description", param.getDescription());
            map.put("contactName", param.getContactName());
            map.put("contactOrg", param.getContactOrg());
            map.put("contactTel", param.getContactTel());
            map.put("contactCard", param.getContactCard());
            map.put("attachments", param.getAttachments());
            VelocityContext context = new VelocityContext(map);
            Template template = Velocity.getTemplate(tplFile, "UTF-8");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            helper.setText(writer.toString(), true);
            mailSender.send(message);
        } catch (Exception ex) {
            log.error("send mail: " + param.getName(), ex);
        }
    }

}
