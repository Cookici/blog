package com.lrh.blog.identify.config;

import com.lrh.blog.identify.authentication.JwtAuthenticationEntryPoint;
import com.lrh.blog.identify.authentication.SecurityRepository;
import com.lrh.blog.identify.filter.*;
import com.lrh.blog.identify.handler.*;
import com.lrh.blog.identify.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.LinkedList;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.config
 * @ClassName: SecurityConfig
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 13:44
 */
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Autowired
    private SecurityRepository securityRepository;


    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager() {
        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
        UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailService);
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(passwordEncoder());
        managers.add(userDetailsRepositoryReactiveAuthenticationManager);
        return new DelegatingReactiveAuthenticationManager(managers);
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors().and().csrf().disable()
                // 登录配置
                .formLogin()
                .authenticationSuccessHandler(loginSuccessHandler)
                .authenticationFailureHandler(loginFailureHandler)
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                .and()
                // 配置拦截规则
                .authorizeExchange()
                .pathMatchers(WhiteList.URL_WHITELIST).permitAll()
                .anyExchange().authenticated()
                .and()
                // 异常处理器
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                // 验证码过滤器放在最前
                .addFilterBefore(captchaFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .securityContextRepository(securityRepository)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

