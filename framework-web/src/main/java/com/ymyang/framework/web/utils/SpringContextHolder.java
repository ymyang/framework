package com.ymyang.framework.web.utils;

import com.ymyang.framework.web.shiro.JWTUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static final String JWT_USER = "K_CLOUD_REQ_JWT_USER";

    private static ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;

    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return springContext;
    }
    public static void setLoginUser(JWTUser jwtUser) {
        setSessionAttribute(JWT_USER, jwtUser);
    }

    public static JWTUser getLoginUser() {
        try {
            return getSessionAttribute(JWT_USER);
        } catch (Exception ex) {
            return null;
        }
    }

    public static ApplicationContext getSpringContext() {
        return SpringContextHolder.springContext;
    }

    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 返回访问资源的真实物理路径
     */
    public static String getRealPath(String path) {
        return getServletRequestAttributes().getRequest().getSession().getServletContext().getRealPath(path);
    }

    public static String getWebRootPath() {
        HttpServletRequest request = getRequest();
        String webRootPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        return webRootPath;
    }

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    /**
     * 获取request属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String name) {
        return (T) getServletRequestAttributes().getRequest().getAttribute(name);
    }

    /**
     * 设置request属性值
     */
    public static <T> void setAttribute(String name, T value) {
        getServletRequestAttributes().getRequest().setAttribute(name, value);
    }

    /**
     * 获取session属性值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionAttribute(String name) {
        return (T) getServletRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * 设置session属性值
     */
    public static void setSessionAttribute(String name, Object value) {
        getServletRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

}
