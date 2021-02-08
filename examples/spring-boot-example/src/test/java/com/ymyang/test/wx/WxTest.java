package com.ymyang.test.wx;

import com.ymyang.framework.web.utils.JsonParser;
import com.ymyang.service.wx.WxConfig;
import com.ymyang.test.BaseTest;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WxTest extends BaseTest {

    @Autowired
    private WxConfig wxConfig;

    @Test
    public void getUserInfo() {
        try {
            String appId = "wx49ed3a32afa961ae";
            String openid = "oECcvwuvV7MI04gSimt07pPtClmE";

            WxMpUser user = wxConfig.getWxMpService(appId).getUserService().userInfo(openid);
            System.out.println(JsonParser.toJson(user));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
