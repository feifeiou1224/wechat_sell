package com.oyf.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.oyf.bean.Constant;
import com.oyf.bean.OrderEnums;
import com.oyf.bean.PayEnums;
import com.oyf.common.BigDecimalUtil;
import com.oyf.common.JsonUtil;
import com.oyf.entity.OrderMaster;
import com.oyf.exception.CustomException;
import com.oyf.repository.OrderMasterRepository;
import com.oyf.service.OrderMasterService;
import com.oyf.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Create Time: 2019年04月22日 15:50
 * Create Author: 欧阳飞
 **/
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private BestPayService bestPayService;


    @Override
    public OrderMaster findOneByOrderId(String orderId) {
            //相当于增强了通过订单号查找的方法：避免了orderId和查询出对象为空的问题
            if (StringUtils.isBlank(orderId)){
                throw new CustomException("订单号为空！");
            }
            Optional<OrderMaster> opt = orderMasterRepository.findById(orderId);
            if (!opt.isPresent()){
                throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
            }
            return opt.get();
    }

    @Override
    public PayResponse create(OrderMaster orderMaster) {
        //工具类中支付请求类
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderMaster.getBuyerOpenid());
        payRequest.setOrderId(orderMaster.getOrderId());
        payRequest.setOrderName(Constant.ORDER_NAME);
        payRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信支付的请求json:{}", JsonUtil.object2string(payRequest));
        PayResponse response = bestPayService.pay(payRequest);
        log.info("微信支付的返回结果json:",JsonUtil.object2string(response));
        return response;
    }

    @Override
    public void wxNotify(String notifyData) {
        //调用API 会自动完成支付状态签名等验证 async是异步，sync是同步
        PayResponse response = bestPayService.asyncNotify(notifyData);
        OrderMaster orderMaster = findOneByOrderId(response.getOrderId());
        //比较支付金额和订单金额是否一致
        if (!BigDecimalUtil.equals2(orderMaster.getOrderAmount(),new BigDecimal(String.valueOf(response.getOrderAmount())))){
            log.error("微信支付回调，订单金额不一致.微信:{},数据库:{}",response.getOrderAmount(),orderMaster.getOrderAmount());
            throw new CustomException(PayEnums.STATUS_ERROR.getMsg());
        }
        //判断订单的支付状态是否为等待支付WAIT
        if(!(orderMaster.getPayStatus()== PayEnums.WAIT.getCode())){
            log.error("微信回调,订单状态异常：{}",orderMaster.getPayStatus());
            throw new CustomException(PayEnums.STATUS_ERROR.getMsg());
        }
        //支付成功后修改支付状态为已支付,订单状态暂时不修改，还有其它流程
        orderMaster.setPayStatus(PayEnums.FINISH.getCode());
        /*orderMaster.setOrderStatus(OrderEnums.FINSH.getCode());*/
        //修改数据库
        orderMasterRepository.save(orderMaster);
        log.info("异步回调，支付状态修改完成！");
    }

    @Override
    public RefundResponse refund(OrderMaster orderMaster) {
        //微信退款对象
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMaster.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信退款请求:{}",refundRequest);
        //执行退款请求
        RefundResponse refund = bestPayService.refund(refundRequest);
        log.info("微信退款请求响应:{}",refund);
        return refund;
    }
}
