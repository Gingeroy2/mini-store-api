package com.gingeroy.ministoreapi.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;

    public String generateToken(String email) {
        final long tokenExpiration = 86400; // 24 hours in seconds
        return Jwts.builder() // 1. 拿一张空白门票
                .subject(email) // 2. 在门票上写上：“这是 a@b.com 的票”
                .issuedAt(new Date()) // 3. 盖上“生效时间”：现在生效
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000)) // 4. 盖上“失效时间”：24小时后作废
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // 5. 盖上防伪印章（设置签名密钥）
                .compact(); // 6. 塑封打包，变成一长串字符串返回

    }

    private Claims getClaims(String token){
        return Jwts.parser() // 1. 拿出验票机，准备开始解析
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // 2. 掏出 .env 里的防伪印章（secret）
                .build() // 3. 组装好验票机
                .parseSignedClaims(token) // 4. 验票！验证签名对不对，有没有过期
                .getPayload(); // 5. 验票通过后，把门票上的“正文”（邮箱、过期时间）取出来

        /*这个“正文”就是一个 Claims 对象。里面装着你生成 Token 时写进去的所有东西：
            subject：主体（也就是 email）
            issuedAt：签发时间
            expiration：过期时间
        所以，claims 就是装着这些信息的一个 Map 集合。*/
    }

    public boolean validateToken(String token){
//        我要验牌！
        try{
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date()); // 过期时间，是不是在现在之后？
        }catch(JwtException ex){
            return false;
        }
    }

    public String getEmailFromToken(String token){
        return getClaims(token).getSubject();
    }
}
