package com.oyf.service;

import com.oyf.common.ResultResponse;
import com.oyf.dto.OrderMasterDto;

public interface OrderMasterService {

    //dto数据是从前端传过来的
    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

}
