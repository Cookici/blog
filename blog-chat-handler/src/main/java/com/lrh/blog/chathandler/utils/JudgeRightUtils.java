package com.lrh.blog.chathandler.utils;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.utils
 * @ClassName: judgeRightUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/23 20:53
 */
@Component
public class JudgeRightUtils {

    @Autowired
    private JwtUtils jwtUtils;


    public  Boolean judgeRight(String userName, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(jwtUtils.getHeader());
        Claims claims = jwtUtils.getClaimsByToken(token);
        String name = claims.getSubject();
        return Objects.equals(name, String.valueOf(userName));
    }


}
