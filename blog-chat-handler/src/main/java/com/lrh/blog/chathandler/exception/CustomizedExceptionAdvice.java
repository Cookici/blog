package com.lrh.blog.chathandler.exception;


import cn.hutool.json.JSONUtil;
import com.lrh.blog.common.result.Result;
import com.lrh.blog.common.result.ResultCodeEnum;
import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.article.exception
 * @ClassName: CustomizedExceptionAdvice
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/1 19:35
 */

@RestControllerAdvice
public class CustomizedExceptionAdvice {

    @ExceptionHandler(FeignException.class)
    public void handlerFeignClientException(FeignException exception, HttpServletResponse response) throws IOException {
        if (exception instanceof FeignException.FeignClientException) {
            if (exception.status() == 401) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setStatus(401);
                ServletOutputStream outputStream = response.getOutputStream();
                Result<Object> result = Result.fail().code(ResultCodeEnum.NO_LOGIN.getCode()).message(ResultCodeEnum.NO_LOGIN.getMessage() + "," + exception.getMessage());
                outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();
            }
        }

    }


}
