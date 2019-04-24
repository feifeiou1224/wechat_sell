package com.oyf.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.oyf.entity.OrderMaster;

public interface PayService {

    OrderMaster findOneByOrderId(String orderId);

    PayResponse create(OrderMaster orderMaster);

    void wxNotify(String notifyData);

    RefundResponse refund(OrderMaster orderMaster);

}
