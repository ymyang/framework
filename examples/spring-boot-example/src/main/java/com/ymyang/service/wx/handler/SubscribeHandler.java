package com.ymyang.service.wx.handler;

import com.ymyang.framework.web.utils.JsonParser;
import com.ymyang.service.member.WxMemberService;
import com.ymyang.service.wx.WxConfig;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SubscribeHandler implements WxMpMessageHandler {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private WxMemberService wxMemberService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        try {
            WxMpUser user = wxMpService.getUserService().userInfo(wxMpXmlMessage.getFromUser());
            log.info("SubscribeHandler, appId=" + appId + ", user = " + JsonParser.toJson(user));
            wxMemberService.create(appId, user);
        } catch (Exception ex) {
            log.error("SubscribeHandler.handle, appId=" + appId, ex);
        }
        return null;
    }

}
