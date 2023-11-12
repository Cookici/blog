package com.lrh.blog.common.utils;

import com.lrh.blog.common.domin.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.common.utils
 * @ClassName: RedisPrefixUtils
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/12 21:48
 */

public class RedisPrefixUtils {

    private RedisPrefixUtils(){

    }

    public static StringBuilder getStringBuilder(String fromId,String toId, String type) {
        StringBuilder stringBuffer = new StringBuilder(type);
        List<Long> list = new ArrayList<>();
        Collections.addAll(list, Long.valueOf(fromId), Long.valueOf(toId));
        list = list.stream().sorted().collect(Collectors.toList());
        for (Long id : list) {
            stringBuffer.append("-").append(id);
        }
        return stringBuffer;
    }

}
