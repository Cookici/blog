package com.lrh.blog.identify.handler;

import cn.hutool.json.JSONUtil;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.identify.exception.CaptchaException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

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
 * @Description:
 * @Date: 2023/10/22 12:26
 */

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        String errorMessage = "用户名或密码错误";
        Result<String> result;
        if (exception instanceof CaptchaException) {
            errorMessage = "验证码错误";
            result = Result.fail(errorMessage);
        } else {
            result = Result.fail(errorMessage);
        }
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
