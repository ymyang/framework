package com.ymyang.framework.web.shiro;


import com.ymyang.framework.web.utils.JsonParser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description JWT 工具类
 * @Date 2018-04-07
 * @Time 22:48
 */
public class JWTUtil {

    // 过期时间 7 天
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    // 密钥
    private static final String SALT = "SHIRO + JWT";
    //
    private static final String claimName = "payload";

    /**
     * 生成 token
     *
     * @param jwtUser
     * @return
     */
    public static String createToken(JWTUser jwtUser) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SALT);
            // 附带JWTUser信息
            return JWT.create()
                    .withClaim(claimName, JsonParser.toJson(jwtUser))
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     *
     * @param token
     * @param jwtUser
     * @return
     */
    public static boolean verify(String token, JWTUser jwtUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SALT);
            //在token中附带了JWTUser信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(claimName, JsonParser.toJson(jwtUser))
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static JWTUser getJWTUser(String token) {

        try {
            DecodedJWT jwt = JWT.decode(token);
            return JsonParser.fromJson(jwt.getClaim(claimName).asString(), JWTUser.class);
        } catch (JWTDecodeException e) {
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * md5混淆加密
     * @param original
     * @return
     */
    public static String encrypt(String original) {
        original = "S#32SDEsER2343S" + original;
        return DigestUtils.md2Hex(original);
    }

    /**
     * MD5加密
     */
    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }

    /**
     * SHA256加密
     */
    public static String sha256(String text) {
        return DigestUtils.sha256Hex(text);
    }
}