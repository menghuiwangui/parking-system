package com.example.campus_parking_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-hours}")
    private Integer expireHours;

    private SecretKey getKey() {
        // 密钥必须 >= 32 字节
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成 token，载荷里放用户ID和角色
    public String generateToken(Integer userId,Integer role) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        int ttl = expireHours * 3600 * 1000;
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ttl))
                .signWith(getKey())
                .compact();
    }

    // 解析 token，失败会抛异常
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
