package com.ymyang.service.wx;

import java.util.HashMap;
import java.util.Map;

import com.ymyang.framework.beans.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ymyang.entity.wx.WxAppEntity;
import com.ymyang.enums.Status;
import com.ymyang.mapper.wx.WxAppMapper;
import com.ymyang.service.wx.handler.MsgHandler;
import com.ymyang.service.wx.handler.SubscribeHandler;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.common.api.WxConsts.EventType;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {

    private static final Map<String, WxMpDefaultConfigImpl> wxMpConfigMap = new HashMap<>();

    private static final Map<String, WxMpService> wxMpServiceMap = new HashMap<>();

    private static final Map<String, WxMpMessageRouter> wxMpMsgRouterMap = new HashMap<>();

    @Autowired
    private WxAppMapper appMapper;

    @Autowired
    private SubscribeHandler subscribeHandler;

    @Autowired
    private MsgHandler msgHandler;

    public void updateWxConfig(String appId) {
        wxMpConfigMap.remove(appId);
        wxMpServiceMap.remove(appId);
        wxMpMsgRouterMap.remove(appId);
    }

    public WxMpService getWxMpService(String appId) {
        WxMpService service = wxMpServiceMap.get(appId);
        if (service != null) {
            return service;
        }

        WxMpConfigStorage config = this.getAppConfig(appId);
        service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(config);

        wxMpServiceMap.put(appId, service);

        return service;
    }

    public WxMpMessageRouter getWxMpMessageRouter(String appId) {
        WxMpMessageRouter router = wxMpMsgRouterMap.get(appId);
        if (router != null) {
            return router;
        }

        router = new WxMpMessageRouter(this.getWxMpService(appId));

        // 记录所有事件的日志 （异步执行）
//        router.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_CREATE_SESSION).handler(this.kfSessionHandler).end();
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_CLOSE_SESSION).handler(this.kfSessionHandler).end();
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(CustomerService.KF_SWITCH_SESSION).handler(this.kfSessionHandler).end();

        // 门店审核事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();
        // 自定义菜单事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.CLICK).handler(this.menuHandler).end();
        // 点击菜单连接事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.VIEW).handler(this.nullHandler).end();

        // 关注事件
        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
        // 上报地理位置事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();
        // 接收地理位置消息
//        router.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();
        // 扫码事件
//        router.rule().async(false).msgType(XmlMsgType.EVENT).event(EventType.SCAN).handler(this.scanHandler).end();
        // 默认
        router.rule().async(false).handler(this.msgHandler).end();

        wxMpMsgRouterMap.put(appId, router);

        return router;
    }

    public WxMpConfigStorage getAppConfig(String appId) {
        WxMpDefaultConfigImpl config = wxMpConfigMap.get(appId);
        if (config != null) {
            return config;
        }

        QueryWrapper<WxAppEntity> wrapper = Wrappers.query();
        wrapper.eq("app_id", appId);
        wrapper.eq("status", Status.Enable);
        wrapper.last("LIMIT 1");
        WxAppEntity app = appMapper.selectOne(wrapper);
        if (app == null) {
            throw new BusinessException("appId错误，未找到相应配置");
        }

        config = new WxMpDefaultConfigImpl();
        config.setAppId(app.getAppId());
        config.setSecret(app.getAppSecret());
        config.setToken(app.getToken());
        config.setAesKey(app.getAesKey());

        wxMpConfigMap.put(appId, config);

        return config;
    }

}
