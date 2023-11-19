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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
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
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: JWTLogoutSuccessHandler
 * @Author: 63283
 * @Description: 在用户退出登录时，覆盖JWT并清除SecurityContextHolder
 * @Date: 2023/10/22 13:37
 */

@Component
public class JwtLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(exchange.getExchange().getResponse()).flatMap(serverHttpResponse -> {
            if (authentication != null) {
                SecurityContextHolder.clearContext();
            }
            DataBufferFactory dataBufferFactory = serverHttpResponse.bufferFactory();
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            serverHttpResponse.getHeaders().set(jwtUtils.getHeader(), "");
            Result<String> result = Result.ok("登出成功");
            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8));
            return serverHttpResponse.writeWith(Mono.just(dataBuffer));
        }));
    }
}
