package com.lrh.blog.common.result;

import lombok.Getter;

/**
 * @ProjectName: lrh-oa-parent
 * @Package: com.lrh.common.result
 * @ClassName: ResultCodeEnum
 * @Author: 63283
 * @Description: 返回值枚举类
 * @Date: 2023/4/11 14:08
 */

@Getter
public enum ResultCodeEnum {

    /**
     * SUCCESS 请求成功
     * FAIL 请求失败
     * LOGIN_ERROR 登录失败
     */
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    LOGIN_ERROR(208,"登陆失败");


    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
