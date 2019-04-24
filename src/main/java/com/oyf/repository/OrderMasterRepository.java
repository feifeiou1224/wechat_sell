package com.oyf.repository;

import com.oyf.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    //根据openId和orderId查询出唯一的订单
    OrderMaster findByBuyerOpenidAndOrderId(String openId,String orderId);


}
