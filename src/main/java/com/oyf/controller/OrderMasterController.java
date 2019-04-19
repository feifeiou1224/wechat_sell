package com.oyf.controller;

import com.google.common.collect.Maps;
import com.oyf.dto.PageParam;
import com.oyf.common.JsonUtil;
import com.oyf.common.ResultResponse;
import com.oyf.dto.OrderMasterDto;
import com.oyf.service.OrderMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Create Time: 2019年04月17日 19:03
 * Create Author: 欧阳飞
 **/

@RestController
@RequestMapping("/buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {

    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("/create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response = ResultResponse.class)
    public ResultResponse create(@Valid @ApiParam(name="订单对象",value = "传入json格式",required = true)
                                             OrderMasterDto orderMasterDto, BindingResult bindingResult){
        Map<String,String> map = Maps.newHashMap();
        //判断是否有参数校验问题
        if(bindingResult.hasErrors()){
            List<String> errList = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return  ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);

    }

    @GetMapping("/list")
    @ApiOperation(value = "查询订单列表")
    public ResultResponse list(@Valid @ApiParam PageParam pageParam){

        return orderMasterService.getOrderList(pageParam);

    }

    @GetMapping("/detail")
    @ApiOperation(value = "查询订单详情",httpMethod = "GET",response = ResultResponse.class)
    public ResultResponse detail(String openId,String orderId){

        return orderMasterService.getOrderDetail(openId, orderId);

    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消订单",httpMethod = "POST",response = ResultResponse.class)
    public ResultResponse cancel(String openId,String orderId){

        return orderMasterService.cancelOrder(openId, orderId);

    }

}
