package com.lrh.blog.identify.filter;

import cn.hutool.json.JSONUtil;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.result.ResultCodeEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
 * @ClassName: JwtAccessDeniedHandler
 * @Author: 63283
 * @Description: 我们之前放行了匿名请求，但有的接口是需要权限的，当用户权限不足时，会进入AccessDeniedHandler进行处理，我们定义JwtAccessDeniedHandler类来实现该接口，需重写其handle方法
 * 当权限不足时，我们需要设置权限不足状态码403，并将错误信息返回给前端
 * @Date: 2023/10/22 13:35
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream outputStream = response.getOutputStream();

        Result<Object> result = Result.fail().code(ResultCodeEnum.NO_RIGHT.getCode()).message(accessDeniedException.getMessage());
        System.out.println(accessDeniedException.getMessage());

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
