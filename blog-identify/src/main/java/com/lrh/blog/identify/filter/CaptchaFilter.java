package com.lrh.blog.identify.filter;

import com.lrh.blog.identify.exception.CaptchaException;
import com.lrh.blog.identify.handler.LoginFailureHandler;
import com.lrh.blog.identify.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: CaptchFilter
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 13:16
 */
@Slf4j
@Component
public class CaptchaFilter implements WebFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        log.info("CaptchaFilter ----> {}", url);

        if ("/login".equals(url) && exchange.getRequest().getMethod() == HttpMethod.POST) {
            // Validate captcha
            try {
                log.info("validate ---> 开始");
                validate(exchange.getRequest());
            } catch (CaptchaException e) {
                // Handle authentication failure
                return loginFailureHandler.onAuthenticationFailure(new WebFilterExchange(exchange, chain), e);
            }
        }

        return chain.filter(exchange);
    }

    /**
     * Validate captcha logic
     */
    private void validate(ServerHttpRequest request) {
        String code = request.getQueryParams().getFirst("code");
        String key = request.getQueryParams().getFirst("userKey");
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        log.info("queryParams --> {}", queryParams);
        log.info("code --> {}", code);
        log.info("key --> {}", key);
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtils.hget("captcha", key))) {
            throw new CaptchaException("验证码错误");
        }

        // If captcha is correct, perform one-time actions
        redisUtils.hdel("captcha", key);
    }
}

