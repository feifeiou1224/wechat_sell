package com.oyf.service;

import com.oyf.common.ResultResponse;
import com.oyf.dto.ProductCategoryDto;
import com.oyf.entity.ProductInfo;

import java.util.List;

public interface ProductInfoService {


        ResultResponse<List<ProductCategoryDto>> getCategoryProductList();

        //通过Id查询商品
        ResultResponse<ProductInfo> queryById(String productId);

        //修改商品信息
        void updateProduct(ProductInfo productInfo);

}
