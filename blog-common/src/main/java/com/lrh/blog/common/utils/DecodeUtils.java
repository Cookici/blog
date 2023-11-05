package com.lrh.blog.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.utils
 * @ClassName: DecodeUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/5 13:42
 */

public class DecodeUtils {

    private DecodeUtils(){

    }

    /**
     *  只能解析两个参数的组合
     * @param message
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String[] decodeMessage(String message) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(message, String.valueOf(StandardCharsets.UTF_8));
        String string = decode.substring(1, decode.length() - 1);
        String[] split = string.split("&");
        String one = split[0].substring(split[0].indexOf("=") + 1);
        String two = split[1].substring(split[1].indexOf("=") + 1);
        return new String[]{one,two};
    }

}
