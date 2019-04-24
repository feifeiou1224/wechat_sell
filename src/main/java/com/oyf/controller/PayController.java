package com.oyf.controller;

import com.google.common.collect.Maps;
import com.lly835.bestpay.model.PayResponse;
import com.oyf.entity.OrderMaster;
import com.oyf.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

/**
 * Create Time: 2019年04月22日 15:29
 * Create Author: 欧阳飞
 **/


@RequestMapping("/pay")
@Controller
@Slf4j
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,@RequestParam("returnUrl")String returnUrl){

        OrderMaster orderMaster = payService.findOneByOrderId(orderId);
        PayResponse response = payService.create(orderMaster);
        Map map = Maps.newHashMap();
        map.put("payResponse",response);
        map.put("returnUrl",returnUrl);

        return new ModelAndView("weixin/pay",map);
    }

    @RequestMapping("/notify")
    public ModelAndView wxNotify(@RequestBody String notifyData){
        //异步回调
        payService.wxNotify(notifyData);

        return new ModelAndView("weixin/success");
    }

    @RequestMapping("/test")
    public void test(){
        log.info("异步回调OK");
    }


}
