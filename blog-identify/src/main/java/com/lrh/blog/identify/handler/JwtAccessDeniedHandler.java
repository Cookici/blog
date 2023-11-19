package com.lrh.blog.identify.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.result.ResultCodeEnum;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
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
 * @ClassName: JwtAccessDeniedHandler
 * @Author: 63283
 * @Description: 没有权限会进入该方法
 * @Date: 2023/10/22 13:35
 */
@Component
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    String result = JSONObject.toJSONString(Result.fail().code(ResultCodeEnum.NO_RIGHT.getCode()).message(denied.getMessage()));
                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(
                            StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer));
                });
    }
}
