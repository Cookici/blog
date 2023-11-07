package com.lrh.blog.chat.utils;

import com.lrh.blog.chat.domain.User;
import io.netty.util.AttributeKey;

/**
 * @ProjectName: Blog
 * @Package: com.lrh.blog.chat.utils
 * @ClassName: Attributes
 * @Author: 63283
 * @Description:
 * @Date: 2023/11/7 19:32
 */

public interface Attributes {
    AttributeKey<User> STATUS =  AttributeKey.newInstance("status");
}
