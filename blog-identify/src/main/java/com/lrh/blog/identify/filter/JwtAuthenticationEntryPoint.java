package com.lrh.blog.identify.filter;

import cn.hutool.json.JSONUtil;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.result.ResultCodeEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: JwtAuthenticationEntryPoint
 * @Author: 63283
 * @Description: 当BasicAuthenticationFilter认证失败的时候会进入AuthenticationEntryPoint，
 * 我们定义JWT认证失败处理器JwtAuthenticationEntryPoint，使其实现AuthenticationEntryPoint接口，
 * 该接口只有一个commence方法，表示认证失败的处理，我们重写该方法，向前端返回错误信息，
 * 不论是什么原因，JWT认证失败，我们就要求重新登录，所以返回的错误信息为请先登录
 * @Date: 2023/10/22 13:32
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        Result<Object> result = Result.fail().code(ResultCodeEnum.NO_LOGIN.getCode()).message(ResultCodeEnum.NO_LOGIN.getMessage() + "," + e.getMessage());
        ;

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

}
