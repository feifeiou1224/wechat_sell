package com.oyf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oyf.entity.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create Time: 2019年04月16日 16:18
 * Create Author: 欧阳飞
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto implements Serializable {

    @JsonProperty("id")
    private String productId;

    /** 名字. */
    @JsonProperty("name")
    private String productName;

    /** 单价. */
    @JsonProperty("price")
    private BigDecimal productPrice;


    /** 描述. */
    @JsonProperty("description")
    private String productDescription;

    /** 小图. */
    @JsonProperty("icon")
    private String productIcon;

    //一般情况都是根据数据库查询到productInfo来构建这个类
    public static ProductInfoDto build(ProductInfo productInfo){
        ProductInfoDto productInfoDto = new ProductInfoDto();
        //复制属性
        BeanUtils.copyProperties(productInfo,productInfoDto);
        return productInfoDto;

    }



}
