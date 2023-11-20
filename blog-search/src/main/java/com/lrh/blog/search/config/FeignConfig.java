package com.lrh.blog.search.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.config
 * @ClassName: FeignConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/18 18:48
 */

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        //添加token
        if (request != null) {
            requestTemplate.header("Authorization", request.getHeader("Authorization"));
        }
    }
}

