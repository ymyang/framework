package com.ymyang.framework.web.exceptionhandler;

import com.ymyang.framework.beans.ResponseEntity;
import com.ymyang.framework.beans.exception.BusinessException;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(value = JsonParseException.class)
    public ResponseEntity handlerJsonParseException(JsonParseException exception) {
        return ResponseEntity.error("请求的Json数据格式错误：" + exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handlerException(Exception e, HttpServletResponse response) {
        if (BusinessException.class.isAssignableFrom(e.getClass())) {
            return ResponseEntity.error(((BusinessException) e).getCode(), e.getMessage());
        }
        log.warn("{}", e);
        return ResponseEntity.error(e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseEntity handlerAuthorizationException(AuthorizationException e, HttpServletResponse response) {
        response.setStatus(403);
        return ResponseEntity.error("没权限访问！");

    }

}
