package com.oyf.controller;

import com.oyf.common.ResultResponse;
import com.oyf.service.ProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create Time: 2019年04月16日 16:44
 * Create Author: 欧阳飞
 **/

@RestController
@RequestMapping("/buyer/product")
@Api(description = "商品信息接口") //在swagger2中对接口进行描述
public class ProductInfoController {

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/list")
    @ApiOperation(value = "查询商品列表")
    public ResultResponse list(){
        return productInfoService.getCategoryProductList();
    }

}
