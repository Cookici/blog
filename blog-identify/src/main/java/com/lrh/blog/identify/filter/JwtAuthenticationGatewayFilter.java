package com.lrh.blog.identify.filter;


import com.lrh.blog.identify.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.handler
 * @ClassName: ServerFormLoginAuthenticationConverter
 * @Author: 63283
 * @Description: 全局网关过滤器
 * @Date: 2023/11/17 17:14
 */
@Slf4j
@Component
public class JwtAuthenticationGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String jwt = exchange.getRequest().getHeaders().getFirst(jwtUtils.getHeader());

        log.info("JwtAuthenticationGatewayFilter ---> {},token ---> {}", exchange.getRequest().getURI().getPath(), jwt);

        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token 已过期");
        }

        return chain.filter(exchange);

    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}