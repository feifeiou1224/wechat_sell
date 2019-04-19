package com.oyf.dto;

import com.oyf.entity.OrderDetail;
import com.oyf.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Create Time: 2019年04月18日 10:26
 * Create Author: 欧阳飞
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterDetailDto extends OrderMaster {

    private List<OrderDetail> orderDetailList;

    public static OrderMasterDetailDto adapter(OrderMaster orderMaster){

        OrderMasterDetailDto orderMasterDetailDto = new OrderMasterDetailDto();

        BeanUtils.copyProperties(orderMaster,orderMasterDetailDto);

        return orderMasterDetailDto;
    }


}
