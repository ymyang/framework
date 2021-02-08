package com.ymyang.test.sms;

import com.ymyang.framework.web.shiro.JWTUtil;
import org.junit.Test;

public class SmsSignTest {

    @Test
    public void sign() {
        String signText = "mobile=" + "13418604106" + "&nonce=" + "0123456789abcdef";
        String sign = JWTUtil.sha256(signText);
        System.out.println(sign);
    }

}
