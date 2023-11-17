package com.lrh.blog.identify.filter;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.filter
 * @ClassName: WhiteList
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/17 10:53
 */

public interface WhiteList {

    String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/blog/identify/register",
            "/favicon.ico",
    };

}
