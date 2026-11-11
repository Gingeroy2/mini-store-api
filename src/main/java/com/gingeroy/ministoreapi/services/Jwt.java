package com.gingeroy.ministoreapi.services;

import com.gingeroy.ministoreapi.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
public class Jwt {
    // JWT 的载荷部分（包含了 userId、role、过期时间等业务数据）
    private final Claims claims;
    private final SecretKey secretKey;

    public boolean isExpired(){
        // 取出 Token 的过期时间，判断是否在当前时间之前
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId(){
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole(){
        return Role.valueOf(claims.get("role", String.class));
    }

    @Override
    public String toString(){
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey) // 盖上防伪印章（设置签名密钥）
                .compact(); // 塑封打包，变成一长串字符串返回
    }
}
