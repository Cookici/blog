package com.lrh.blog.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.identify.utils
 * @ClassName: DecodeUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/5 13:42
 */

public class DecodeUtils {

    private DecodeUtils() {

    }

    /**
     * 解析多个参数的组合禁止解析内容中含有等号
     *
     * @param message
     * @return
     * @throws UnsupportedEncodingException
     */
    public static List<String> decodeMessage(String message) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(message, String.valueOf(StandardCharsets.UTF_8));
        String string = decode.substring(1, decode.length() - 1);

        Pattern pattern = Pattern.compile("([^&=]+)=([^&]+)");
        Matcher matcher = pattern.matcher(string);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            list.add(value);
            System.out.println("Key:" + key);
            System.out.println("Value: " + value);
        }

        return list;
    }

}
