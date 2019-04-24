package com.oyf.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.oyf.bean.WeChatBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Create Time: 2019年04月22日 15:07
 * Create Author: 欧阳飞
 **/

@Configuration
public class WXPayConfig {

    @Autowired
    private WeChatBean weChatBean;

    @Bean
    public BestPayService bestPayService(){

        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId(weChatBean.getAppId());
        wxPayH5Config.setAppSecret(weChatBean.getSecret());
        wxPayH5Config.setMchId(weChatBean.getMchId());
        wxPayH5Config.setMchKey(weChatBean.getMchKey());
        wxPayH5Config.setKeyPath(weChatBean.getKeyPath());
        wxPayH5Config.setNotifyUrl(weChatBean.getNotifyUrl());

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config);

        return bestPayService;
    }




}
