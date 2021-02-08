package com.ymyang.framework.web.exceptionhandler;

import com.ymyang.framework.beans.ResponseEntity;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorController implements ErrorController {

    private ErrorAttributes errorAttributes;

    private ServerProperties serverProperties;

    private ErrorProperties errorProperties;

    public DefaultErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        this.errorAttributes = errorAttributes;
        this.serverProperties = serverProperties;
        this.errorProperties = serverProperties.getError();
    }

    @RequestMapping
    public ResponseEntity error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request, false);
        if (body.get("status").equals(401)) {
            response.setStatus(Integer.valueOf(body.get("status").toString()));
            return ResponseEntity.error("请先登录。");
        } else if (body.get("status").equals(404)) {
            response.setStatus(Integer.valueOf(body.get("status").toString()));
            return ResponseEntity.error("url不存在");
        }
        response.setStatus(200);
        return ResponseEntity.error(body.get("message").toString());
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                     boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }
}
