package com.lrh.blog.identify.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ProjectName: lrh-oa-parent
 * @Package: com.lrh.common.jwt
 * @ClassName: JwtHelper
 * @Author: 63283
 * @Description:
 * @Date: 2023/4/21 12:54
 */

@Data
@Component
@Configuration
public class JwtUtils {

    private static final long EXPIRE = 6 * 60;
    private static final String SECRET = "louruihan20030706springsecurity";
    private String header = "Authorization";


    /**
     *     生成JWT
     */
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * EXPIRE);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     *     解析JWT
     */
    public Claims getClaimsByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *     判断JWT是否过期
     */

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}

