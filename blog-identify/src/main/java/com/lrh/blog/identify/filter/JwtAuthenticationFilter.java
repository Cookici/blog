package com.lrh.blog.identify.filter;


import com.lrh.blog.identify.service.UserDetailService;
import com.lrh.blog.identify.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;;
import java.util.Arrays;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: JwtAuthenticationFilter
 * @Author: 63283
 * @Description: 在首次登录成功后，LoginSuccessHandler将生成JWT，并返回给前端。在之后的所有请求中（包括再次登录请求），都会携带此JWT信息。我们需要写一个JWT过滤器JwtAuthenticationFilter，当前端发来的请求有JWT信息时，该过滤器将检验JWT是否正确以及是否过期，若检验成功，则获取JWT中的用户名信息，检索数据库获得用户实体类，并将用户信息告知Spring Security，后续我们就能调用security的接口获取到当前登录的用户信息。
 * 若前端发的请求不含JWT，我们也不能拦截该请求，因为一般的项目都是允许匿名访问的，有的接口允许不登录就能访问，没有JWT也放行是安全的，因为我们可以通过Spring Security进行权限管理，设置一些接口需要权限才能访问，不允许匿名访问
 * @Date: 2023/10/22 13:20
 */

@Slf4j
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter  {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/blog/identify/register",
            "/favicon.ico",
    };


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        log.error("jwtAuthenticationFilter---->{}",request.getRequestURI());
        boolean judge =  Arrays.asList(URL_WHITELIST).contains(request.getRequestURI());
        if (judge) {
            chain.doFilter(request,response);
            return;
        }


        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token 已过期");
        }

        String username = claim.getSubject();

        // 获取用户的权限等信息
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request, response);

    }
}
