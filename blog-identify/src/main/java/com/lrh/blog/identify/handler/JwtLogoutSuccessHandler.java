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
 * @Description: 在用户退出登录时，我们需将原来的JWT置为空返给前端，这样前端会将空字符串覆盖之前的jwt，JWT是无状态化的，销毁JWT是做不到的，JWT生成之后，只有等JWT过期之后，才会失效。因此我们采取置空策略来清除浏览器中保存的JWT。同时我们还要将我们之前置入SecurityContext中的用户信息进行清除，这可以通过创建SecurityContextLogoutHandler对象，调用它的logout方法完成
 * 们定义LogoutSuccessHandler接口的实现类JWTLogoutSuccessHandler，重写其onLogoutSuccess方法
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
