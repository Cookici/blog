package com.lrh.blog.identify.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.exception.CaptchaException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.handler
 * @ClassName: LoginFailureHandler
 * @Author: 63283
 * @Description: 登陆失败处理器
 * @Date: 2023/10/22 12:26
 */

@Component
public class LoginFailureHandler implements ServerAuthenticationFailureHandler {


    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange()
                .getResponse()).flatMap(serverHttpResponse -> {
            DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String errorMessage = "用户名或密码错误";
            Result<String> result;
            if (exception instanceof CaptchaException) {
                errorMessage = "验证码错误";
                result = Result.fail(errorMessage);
            } else {
                result = Result.fail(errorMessage);
            }
            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
            return serverHttpResponse.writeWith(Mono.just(dataBuffer));
        }));
    }
}
