package com.lrh.blog.identify.authentication;

import com.lrh.blog.identify.filter.WhiteList;
import com.lrh.blog.identify.service.UserDetailService;
import com.lrh.blog.identify.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.handler
 * @ClassName: SecurityRepository
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/18 14:43
 */
@Slf4j
@Component
public class SecurityRepository implements ServerSecurityContextRepository {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String jwt = exchange.getRequest().getHeaders().getFirst(jwtUtils.getHeader());
        log.info("JwtAuthenticationFilter -----> {}", exchange.getRequest().getURI().getPath());
        boolean judge = Arrays.asList(WhiteList.URL_WHITELIST).contains(exchange.getRequest().getURI().getPath());
        if (judge) {
            log.info("白名单url");
            return Mono.empty();
        }
        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            log.error("token 异常");
            return Mono.empty();
        }
        if (jwtUtils.isTokenExpired(claim)) {
            log.error("token 已过期");
            return Mono.empty();
        }
        String username = claim.getSubject();

        Mono<UserDetails> userDetailsMono = userDetailService.findByUsername(username);

        return userDetailsMono.flatMap(userDetails -> {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            log.info("usernamePasswordAuthenticationToken ---> {}", authentication);
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(authentication);
            log.info("securityContext ---> {}", emptyContext);
            return Mono.just(emptyContext);
        });
    }
}
