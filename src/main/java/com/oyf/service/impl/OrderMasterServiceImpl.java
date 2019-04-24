package com.oyf.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oyf.bean.*;
import com.oyf.common.BigDecimalUtil;
import com.oyf.common.IDUtils;
import com.oyf.common.ResultResponse;
import com.oyf.dto.OrderDetailDto;
import com.oyf.dto.OrderMasterDetailDto;
import com.oyf.dto.OrderMasterDto;
import com.oyf.dto.PageParam;
import com.oyf.entity.OrderDetail;
import com.oyf.entity.OrderMaster;
import com.oyf.entity.ProductInfo;
import com.oyf.exception.CustomException;
import com.oyf.repository.OrderDetailRepository;
import com.oyf.repository.OrderMasterRepository;
import com.oyf.repository.PageRepository;
import com.oyf.repository.ProductInfoRepository;
import com.oyf.service.OrderDetailService;
import com.oyf.service.OrderMasterService;
import com.oyf.service.PayService;
import com.oyf.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create Time: 2019年04月17日 15:22
 * Create Author: 欧阳飞
 **/

@Service
@Slf4j
public class OrderMasterServiceImpl implements OrderMasterService {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private PayService payService;


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

    @Override
    public ResultResponse<List<OrderMaster>> getOrderList(PageParam pageParam) {

        /*分页类，设置开始页数和每页条数*/
        Pageable pageable = PageRequest.of(pageParam.getPage(),pageParam.getSize());

        /*条件+分页查询*/
        List<OrderMaster> orderMasterList = pageRepository.findByBuyerOpenid(pageParam.getOpenId(), pageable);

        //转换成dto类型
        List<OrderMasterDetailDto> collect = orderMasterList.stream().map(orderMaster -> OrderMasterDetailDto.adapter(orderMaster)).collect(Collectors.toList());

        return ResultResponse.success(collect);

    }

    @Override
    public ResultResponse<OrderMaster> getOrderDetail(String openId, String orderId) {

        if (StringUtils.isBlank(openId) || StringUtils.isBlank(orderId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg());
        }

        /*通过openId和orderId先查询出OrderMaster，然后通过orderId查出订单详情，set进OrderMaster*/
        OrderMaster orderMaster = orderMasterRepository.findByBuyerOpenidAndOrderId(openId, orderId);
        //需要转换成dto类型，因为多了一条字段
        OrderMasterDetailDto adapter = OrderMasterDetailDto.adapter(orderMaster);
        List<OrderDetail> detailList = orderDetailRepository.findByOrderId(orderId);
        adapter.setOrderDetailList(detailList);

        return ResultResponse.success(adapter);
    }

    @Override
    @Transactional
    public ResultResponse cancelOrder(String openId, String orderId) {

        if (StringUtils.isBlank(openId) || StringUtils.isBlank(orderId)){
            throw new CustomException(ResultEnums.PARAM_ERROR.getMsg());
        }
        OrderMaster orderMaster = orderMasterRepository.findByBuyerOpenidAndOrderId(openId, orderId);
        if (orderMaster == null){
            throw new CustomException("订单不存在！");
        }
        //判断订单的状态,取消订单如果是待支付的定单，不退钱，只把数量归还给库存
        if (orderMaster.getPayStatus() == PayEnums.WAIT.getCode()){
            //修改订单状态为：已取消
            orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
            //修改支付状态为：支付失败
            orderMaster.setPayStatus(PayEnums.FAIL.getCode());
            //修改库存
            returnStocks(orderId);
            //更新订单信息
            orderMasterRepository.save(orderMaster);
        }

        //判断订单的状态，退款订单应该是已经完成支付的订单，退钱并归还库存
        if (orderMaster.getPayStatus() == PayEnums.FINISH.getCode()){
            //修改库存
            returnStocks(orderId);
            //修改订单状态为：已取消
            orderMaster.setOrderStatus(OrderEnums.CANCEL.getCode());
            //更新订单信息
            orderMasterRepository.save(orderMaster);
            log.info("发起退款");
            payService.refund(orderMaster);
        }
        return ResultResponse.success();
    }

    /** 修改商品库存的方法 */
    @Transactional
    public void returnStocks(String orderId){
        //查询出订单项集合:订单项有商品id和购买的数量
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        for ( OrderDetail orderDetail : orderDetailList){
            String productId = orderDetail.getProductId();
            Integer productQuantity = orderDetail.getProductQuantity();
            try {
                productInfoRepository.AddProductStocksByProductId(productId,productQuantity);
            }catch (Exception e){
                throw new CustomException("商品库存添加失败！");
            }
        }
    }


}
