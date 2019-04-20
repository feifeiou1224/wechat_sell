package com.oyf.config;

import com.oyf.bean.WeChatBean;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Create Time: 2019年04月19日 17:29
 * Create Author: 欧阳飞
 **/

@Configuration
public class WeChatConfig {

    @Autowired
    private WeChatBean weChatBean;

    @Bean
    public WxMpService wxMpService(){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(weChatBean.getAppId());
        wxMpInMemoryConfigStorage.setSecret(weChatBean.getSecret());
        System.err.println(weChatBean.getAppId()+":"+weChatBean.getSecret());
        return wxMpInMemoryConfigStorage;
    }


}
