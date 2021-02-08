package com.ymyang.framework.web.shiro;

import com.ymyang.framework.beans.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTFilter extends BasicHttpAuthenticationFilter {


    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return pathsMatch(getLoginUrl(), request) || executeLogin(request, response);
    }


    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws ShiroException {
        String authorization = getAuthzHeader(request);
        if (authorization == null) {
            return true;
        }
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request, response).login(token);
        } catch (AuthenticationException e) {
            responseError401(response, e.getMessage());
            return false;
        } catch (Exception e) {
            responseError403(response, e.getMessage());
            return false;
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
//        response403(request, response);
        return false;
    }


    /**
     * @param response
     * @param message
     */
    private void responseError401(ServletResponse response, String message) {
        responseError(response, message, 401);
    }

    /**
     * @param response
     * @param message
     */
    private void responseError403(ServletResponse response, String message) {
        responseError(response, message, 403);
    }


    /**
     * @param response
     * @param message
     */
    private void responseError(ServletResponse response, String message, int statusCode) {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.getWriter().println(objectMapper.writeValueAsString(ResponseEntity.error(message)));
            ((HttpServletResponse) response).setStatus(statusCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        super.postHandle(request, response);
    }
}
