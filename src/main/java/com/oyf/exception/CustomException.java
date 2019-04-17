package com.oyf.exception;

import com.oyf.bean.ResultEnums;

/**
 * Create Time: 2019年04月17日 14:45
 * Create Author: 欧阳飞
 **/

public class CustomException extends RuntimeException {

    //错误状态码
    private int code;

    //自己传递错误信息和错误的状态码
    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }

    //调用本类的构造方法，默认错误状态码，自定义错误信息
    public CustomException(String message){
        this(message,ResultEnums.FAIL.getCode());
    }
}
