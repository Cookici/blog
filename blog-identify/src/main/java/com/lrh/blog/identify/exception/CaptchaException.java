package com.lrh.blog.identify.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.exception
 * @ClassName: CaptchaException
 * @Author: 63283
 * @Description:
 * @Date: 2023/10/22 12:31
 */

public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
