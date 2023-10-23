package com.lrh.blog.common.result;
import lombok.Data;

/**
 * @ProjectName: lrh-oa-parent
 * @Package: com.lrh.common.result
 * @ClassName: Result
 * @Author: 63283
 * @Description: 统一返回值
 * @Date: 2023/4/11 14:12
 */

@Data
public class Result<T> {


    private Integer code;
    private String message;
    private T data;



    private Result(){}//构造私有化

    //封装返回是数据
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        //封装数据
        if(body != null) {
            result.setData(body);
        }
        //状态码
        result.setCode(resultCodeEnum.getCode());
        //返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //成功
    public static<T> Result<T> ok() {
        return build(null,ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> ok(T data) {
        return build(data,ResultCodeEnum.SUCCESS);
    }

    //失败
    public static<T> Result<T> fail() {
        return build(null,ResultCodeEnum.FAIL);
    }

    public static<T> Result<T> fail(T data) {
        return build(data,ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

}
