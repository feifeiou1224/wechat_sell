package com.oyf.service;

import com.oyf.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    //批量插入
    void batchInsert(List<OrderDetail> orderDetailList);

}
