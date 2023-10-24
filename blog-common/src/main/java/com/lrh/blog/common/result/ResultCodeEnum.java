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
     * NO_RIGHT 没有权限
     */
    SUCCESS(200,"请求成功"),
    FAIL(400, "请求失败"),
    NO_LOGIN(401,"请先登录"),
    NO_RIGHT(403,"没有权限");


    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
