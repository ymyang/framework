package com.ymyang.test;


import com.ymyang.framework.web.shiro.JWTUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class PwdTest {

    @Test
    public void pwd() {
        String pwd = JWTUtil.sha256("123456");
        System.out.println(pwd);
    }

    @Test
    public void random() {
        System.out.println(RandomStringUtils.randomAlphanumeric(64).toLowerCase());
    }

    @Test
    public void str() {
        String str = "c13ffb77249344e2b88ef4bb158fcde6#/";
        System.out.println(str.substring(0, str.indexOf("#")));
    }



}
