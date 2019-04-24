package com.oyf.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Create Time: 2019年04月19日 17:35
 * Create Author: 欧阳飞
 **/

@Component
@EnableConfigurationProperties(WeChatBean.class)
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "wechat")
@Data
public class WeChatBean {

    private String appId;

    private String secret;

    private String mchId;

    private String mchKey;

    private String keyPath;

    private String notifyUrl;

}
