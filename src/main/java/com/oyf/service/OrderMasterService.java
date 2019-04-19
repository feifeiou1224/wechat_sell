package com.oyf.service;

import com.oyf.dto.PageParam;
import com.oyf.common.ResultResponse;
import com.oyf.dto.OrderMasterDto;
import com.oyf.entity.OrderMaster;

import java.util.List;

public interface OrderMasterService {

    //dto数据是从前端传过来的
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    //获取分页后的订单列表
    ResultResponse<List<OrderMaster>> getOrderList(PageParam pageParam);

    //获取订单详情
    ResultResponse<OrderMaster> getOrderDetail(String openId,String orderId);

    //取消订单
    ResultResponse cancelOrder(String openId,String orderId);

}
