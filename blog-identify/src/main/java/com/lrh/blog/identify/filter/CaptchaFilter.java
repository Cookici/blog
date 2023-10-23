package com.lrh.blog.identify.filter;

import com.alibaba.fastjson.JSONObject;
import com.lrh.blog.identify.exception.CaptchaException;
import com.lrh.blog.identify.handler.LoginFailureHandler;
import com.lrh.blog.identify.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: CaptchFilter
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 13:16
 */

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI();
        if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
            // 校验验证码
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {

                /*
                  交给认证失败处理器
                 */
                try {
                    loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                } catch (IOException | ServletException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 校验验证码逻辑
     */
    private void validate(HttpServletRequest httpServletRequest) throws IOException {

        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("userKey");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtils.hget("captcha", key))) {
            throw new CaptchaException("验证码错误");
        }

        /*
           若验证码正确，执行以下语句
           一次性使用
         */

        redisUtils.hdel("captcha", key);
    }


}

