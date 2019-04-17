package com.oyf.bean;

import lombok.Getter;

/**
 * Create Time: 2019年04月16日 15:34
 * Create Author: 欧阳飞
 **/

@Getter
public enum  ResultEnums {

    SUCCESS(0,"成功"),
    FAIL(1,"失败"),
    PRODUCT_UP(0,"正常"),
    PRODUCT_DOWN(1,"商品下架"),
    PARAM_ERROR(1,"参数错误"),
    NOT_EXIST(1,"商品不存在");

    private Integer code;

    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
