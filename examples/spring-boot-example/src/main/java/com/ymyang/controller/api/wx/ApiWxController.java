package com.ymyang.controller.api.wx;

import com.ymyang.entity.wx.WxMemberEntity;
import com.ymyang.framework.web.annotations.Anonymous;
import com.ymyang.config.redis.CacheService;
import com.ymyang.service.wx.WxConfig;
import com.ymyang.service.wx.WxMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Anonymous
@Slf4j
@Api(value = "wx.ApiWxController", tags = {"微信授权回调"})
@RestController()
@RequestMapping("/api/front/wx/{appId}")
public class ApiWxController {

    @Value("${app.host}")
    private String appHost;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private WxMemberService wxMemberService;

    /**
     * 微信授权跳转，用于公众号菜单地址，state=1时调用登录接口，state=2时调用获取用户信息接口
     * @param appId 微信AppId
     * @param url 回调地址
     * @param scope snsapi_userinfo / snsapi_base
     */
    @ApiOperation(value = "微信授权跳转", notes = "scope: snsapi_userinfo / snsapi_base")
    @GetMapping("/redirect")
    public ModelAndView redirect(@PathVariable("appId") String appId,
                                 @RequestParam("url") String url,
                                 @RequestParam(value = "scope", required = false) String scope) throws Exception {
        WxMpConfigStorage config = wxConfig.getAppConfig(appId);

        String state = "";

        if (WxConsts.OAuth2Scope.SNSAPI_BASE.equals(scope)) {
            state = "1";
        } else {
            scope = WxConsts.OAuth2Scope.SNSAPI_USERINFO;
            state = "2";
        }

        // 回调地址
        String callbackUrl = appHost + "/api/front/wx/" + appId + "/callback?url=" + URLEncoder.encode(url, "utf8");

        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize"
                + "?appid=" + config.getAppId()
                + "&redirect_uri=" + URLEncoder.encode(callbackUrl, "utf8")
                + "&response_type=code"
                + "&scope=" + scope
                + "&state=" + state
                + "#wechat_redirect";

        return new ModelAndView(new RedirectView(redirectUrl));
    }

    /**
     * 微信授权回调
     * @param appId 微信AppId
     * @param code 微信授权code
     * @param state state=1时调用登录接口，state=2时调用获取用户信息接口
     * @param url 回调地址
     * @return
     */
    @ApiOperation(value = "微信授权回调")
    @GetMapping("/callback")
    public ModelAndView callback(@PathVariable("appId") String appId,
                                 @RequestParam("code") String code,
                                 @RequestParam("state") String state,
                                 @RequestParam("url") String url) {
        String key = UUID.randomUUID().toString().replace("-", "");
        try {
//            LoginResultD resultDto = null;
//            if ("2".equals(state)) {
//                resultDto = this.getUserInfo(appId, code);
//            } else {
//                resultDto = this.login(appId, code);
//            }
//
//            cacheService.set(key, resultDto, 5, TimeUnit.MINUTES);
//            log.info("callback key="+key+"dto="+ resultDto.getToken());
        } catch (Exception ex) {
            log.error("callback, appId=" + appId, ex);
        }

        String redirectUrl = url + "?_s=" + key;
        return new ModelAndView(new RedirectView(redirectUrl));
    }

    /**
     * 微信授权登录 微信回调state=1时调用登录接口
     */
//    private LoginResultDto login(String appId, String code) throws Exception {
//        String key = "wx-login-result:" + appId + "-" + code;
//        LoginResultDto resultDto = cacheService.get(key);
//        if (resultDto != null) {
//            return resultDto;
//        }
//
//        WxMpService wxMpService = wxConfig.getWxMpService(appId);
//        WxMpOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
//
//        log.info("login callback, appId=" + appId+ "###openid="+accessToken.getOpenId());
//        resultDto = wxMemberService.login(appId, accessToken);
//
//        cacheService.set(key, resultDto, 5, TimeUnit.MINUTES);
//
//        return resultDto;
//    }

    /**
     * 授权获取微信用户信息 微信回调state=2时调用获取用户信息接口
     */
//    private LoginResultDto getUserInfo(String appId, String code) throws Exception {
//        String key = "wx-user-result:" + appId + "-" + code;
//        LoginResultDto resultDto = cacheService.get(key);
//        if (resultDto != null) {
//            return resultDto;
//        }
//
//        WxMpService wxMpService = wxConfig.getWxMpService(appId);
//        WxMpOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
//
//        WxMpUser user = wxMpService.getUserService().userInfo(accessToken.getOpenId());
//        WxMemberEntity wxMember = wxMemberService.create(appId, user);
//        resultDto = wxMemberService.login(wxMember);
//
//        cacheService.set(key, resultDto, 5, TimeUnit.MINUTES);
//
//        return resultDto;
//    }


}
