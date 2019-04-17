package com.oyf.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oyf.bean.OrderEnums;
import com.oyf.bean.PayEnums;
import com.oyf.bean.ProductEnums;
import com.oyf.bean.ResultEnums;
import com.oyf.common.BigDecimalUtil;
import com.oyf.common.IDUtils;
import com.oyf.common.ResultResponse;
import com.oyf.dto.OrderDetailDto;
import com.oyf.dto.OrderMasterDto;
import com.oyf.entity.OrderDetail;
import com.oyf.entity.OrderMaster;
import com.oyf.entity.ProductInfo;
import com.oyf.exception.CustomException;
import com.oyf.repository.OrderMasterRepository;
import com.oyf.service.OrderDetailService;
import com.oyf.service.OrderMasterService;
import com.oyf.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create Time: 2019年04月17日 15:22
 * Create Author: 欧阳飞
 **/

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    //事务需要遇到异常才回滚
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        //参数校验在controller层进行
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建订单项集合
        List<OrderDetail> detailList = Lists.newArrayList();
        //生成订单id
        String orderId = IDUtils.createIdbyUUID();
        //初始化订单总金额 bigDecimal用于高精度计算
        BigDecimal totalPrice = new BigDecimal("0");
        //遍历dto集合，查询每个dto是否有对应的商品
        for (OrderDetailDto item : items){
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(item.getProductId());
            if (resultResponse.getCode() == ResultEnums.FAIL.getCode()){
                throw new CustomException(resultResponse.getMsg());
            }
            //获得通过id查询的商品
            ProductInfo productInfo = resultResponse.getData();
            //比较 库存与购买数量
            if (productInfo.getProductStock() < item.getProductQuantity()){//说明库存不足
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //封装商品和dto数据为OrderDetail类型，放入集合
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID())
                    .orderId(orderId)
                    .productId(item.getProductId())
                    .productIcon(productInfo.getProductIcon())
                    .productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice())
                    .productQuantity(item.getProductQuantity()).build();
            detailList.add(orderDetail);
            //修改商品的库存数量
            productInfo.setProductStock(productInfo.getProductStock() - item.getProductQuantity());
            productInfoService.updateProduct(productInfo);
            //计算商品总金额
            totalPrice = BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),item.getProductQuantity()));
        }
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder().orderId(orderId)
                .orderStatus(OrderEnums.NEW.getCode())
                .orderAmount(totalPrice)
                .buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName())
                .buyerPhone(orderMasterDto.getPhone())
                .buyerOpenid(orderMasterDto.getOpenid())
                .payStatus(PayEnums.WAIT.getCode()).build();
        //批量插入订单项到数据库
        orderDetailService.batchInsert(detailList);
        //插入订单到数据库
        orderMasterRepository.save(orderMaster);
        //根据接口的数据格式返回数据
        HashMap<String, String> map = Maps.newHashMap();
        map.put("orderId",orderId);
        return ResultResponse.success(map);

    }
}
