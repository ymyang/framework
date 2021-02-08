package com.ymyang.framework.web.shiro;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JWTUser {

    private Long id = 0L;

    /**
     * 账号，唯一
     */
    private String account = "";

    /**
     * 用户名
     */
    private String username = "";

    private String extra = "";

}
