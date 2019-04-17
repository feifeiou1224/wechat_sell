package com.oyf.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oyf.bean.ResultEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Create Time: 2019年04月16日 15:33
 * Create Author: 欧阳飞
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {

    private Integer code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL) //返回json时忽略为null的属性
    private T data;

    public ResultResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //失败：不携带数据 不携带信息
    public static ResultResponse fail(){
        return new ResultResponse<>(ResultEnums.FAIL.getCode(),ResultEnums.FAIL.getMsg());
    }

    //失败: 不携带数据 携带信息的
    public static ResultResponse fail(String msg){
        return  new ResultResponse<>(ResultEnums.FAIL.getCode(),msg);
    }

    //失败: 携带数据 携带信息
    public static <T> ResultResponse  fail(String msg,T t){
        return  new ResultResponse<>(ResultEnums.FAIL.getCode(),msg,t);
    }

    //失败: 携带数据 不携带信息
    public static <T> ResultResponse  fail(T t){
        return  new ResultResponse<>(ResultEnums.FAIL.getCode(),ResultEnums.FAIL.getMsg(),t);
    }

    //成功: 携带数据
    public static <T> ResultResponse success(T t){
        return new ResultResponse<>(ResultEnums.SUCCESS.getCode(),ResultEnums.SUCCESS.getMsg(),t);
    }

    //成功：不携带数据
    public static ResultResponse success(){
        return new ResultResponse<>(ResultEnums.SUCCESS.getCode(),ResultEnums.SUCCESS.getMsg());
    }
}
