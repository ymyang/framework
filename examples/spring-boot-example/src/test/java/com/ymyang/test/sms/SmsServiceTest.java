package com.ymyang.test.sms;

import com.ymyang.framework.web.utils.JsonParser;
import com.ymyang.param.sms.SmsSendParam;
import com.ymyang.service.sms.SmsService;
import com.ymyang.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsServiceTest extends BaseTest {

    @Autowired
    private SmsService smsService;

    @Test
    public void sendSms() {
        SmsSendParam param = new SmsSendParam();
        param.setTemplateCode("SMS_205122886");
        param.setMobile("13418604106");
        Map<String, String> params = new HashMap<>();
        params.put("name", "迎春晚会活动");
        params.put("address", "发展中心");
        params.put("time", "2021-01-07 15:30");
        param.setParams(params);
        smsService.send(param);
    }

    @Test
    public void checkSmsCode() {
        String mobile = "18302479158";
        String code = "244444";
        boolean check = smsService.checkSmsCode(mobile, code);
        System.out.println(check);
    }

    @Test
    public void fetchParams() {
//        String tpl = "由${company}发起的电子签约文件《${contractname}》您还未处理，文件有效期仅剩3天，请访问 http://contract.kaisagroup.com:9180/${link} 尽快处理。";
        String tpl = "亲爱的商户，平台有新的订单，请及时登录后台跟进处理！";
        String regex = "[$][{]([^${}]*?)[}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tpl);

        Map<String, String> params = new HashMap<>();
        while (matcher.find()) {
            String paramKey = matcher.group().replaceAll("[${}]", "");
            params.put(paramKey, "");
        }

        System.out.println(JsonParser.toJson(params));
    }

    @Test
    public void replaceParams() {
        String tpl = "由${company}发起的电子签约文件《${contractname}》您还未处理，文件有效期仅剩3天，请访问 http://contract.kaisagroup.com:9180/${link} 尽快处理。【${company}】";

        String content = tpl;
        Map<String, String> params = new HashMap<>();
        params.put("company", "佳兆业");
        params.put("contractname", "采招合同");
        params.put("link", "1234");
        for (String key : params.keySet()) {
            String regex = "\\$\\{" + key + "\\}";
            content = content.replaceAll(regex, params.get(key));
        }

        System.out.println(content);
    }

}
