package com.zeyuli.util;

import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Date;


/**
 * Jwt工具类<br>
 * 在application.yaml中token过期时间单位是小时
 *
 * @author 李泽聿
 * @since 2025-10-21 21:10
 */


@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtUtil {

    private long tokenExpiration; // 有效时间,单位毫秒 1000毫秒 == 1秒
    private String tokenSignKey;  // 当前程序 签名秘钥

    // 生成token字符串
    public String createToken(String id, String userName, String password) {
        return Jwts.builder()
                .setSubject(id.toString())  // 将用户ID作为主体
                .claim("userName", userName) // 添加用户名作为独立声明
                // 保留密码哈希校验位（6位短哈希）
                .claim("pwdv", DigestUtils.md5DigestAsHex(password.getBytes()).substring(0, 6))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000L * 60 * 60))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    // 从token字符串获取id,userName,password
    public String[] getUserInfo(String token) {
        if (StringUtils.isEmpty(token))
            return null;
        try {
            Claims claims = Jwts.parser() // 使用旧版本的方式
                    .setSigningKey(tokenSignKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String id = claims.getSubject();
            String username = claims.get("userName", String.class);
            String password = claims.get("pwdv", String.class);
            return new String[]{id, username, password};
        } catch (Exception e) {
            return null;
        }
    }


    // 判断token是否有效
    public boolean isExpiration(String token) {
        try {
            boolean isExpire = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().
                    before(new Date());
            // 没有过期，有效，返回false
            return isExpire;
        } catch (Exception e) {
            // 过期出现异常，返回true
            return true;
        }
    }
}