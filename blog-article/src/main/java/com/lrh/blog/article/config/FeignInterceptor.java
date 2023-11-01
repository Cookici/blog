package com.lrh.blog.article.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.config
 * @ClassName: FeignConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/1 17:38
 */

@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null){
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String authorization = request.getHeader("Authorization");
            template.header("Authorization", authorization);

        }
    }
}

