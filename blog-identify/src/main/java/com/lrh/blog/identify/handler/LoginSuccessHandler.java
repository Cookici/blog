package com.lrh.blog.identify.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
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
 * @ClassName: LoginSuccessHandler
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 12:23
 */

@Component
public class LoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange().getResponse()).flatMap(serverHttpResponse -> {
            DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            String token = jwtUtils.generateToken(authentication.getName());
            serverHttpResponse.getHeaders().set(jwtUtils.getHeader(), token);
            Result<String> result = Result.ok("登录成功");
            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
            return serverHttpResponse.writeWith(Mono.just(dataBuffer));
        }));

    }
}
