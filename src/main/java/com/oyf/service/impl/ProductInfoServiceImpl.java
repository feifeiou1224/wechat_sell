package com.oyf.service.impl;

import com.oyf.common.ResultEnums;
import com.oyf.common.ResultResponse;
import com.oyf.dto.ProductCategoryDto;
import com.oyf.dto.ProductInfoDto;
import com.oyf.entity.ProductInfo;
import com.oyf.repository.ProductInfoRepository;
import com.oyf.service.ProductCategoryService;
import com.oyf.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Create Time: 2019年04月16日 16:53
 * Create Author: 欧阳飞
 **/

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Override
    public ResultResponse<List<ProductCategoryDto>> getCategoryProductList() {

        //获取类别的dto集合,目的是将产品信息的dto集合放入类别
        ResultResponse<List<ProductCategoryDto>> category = productCategoryService.findAll();
        List<ProductCategoryDto> categoryDtos = category.getData();
        if (CollectionUtils.isEmpty(categoryDtos)){//如果类别dto集合为空，直接返回
            return category;
        }
        //获取类别编号的集合
        List<Integer> typeList = categoryDtos.stream().map(ProductCategoryDto::getCategoryType).collect(Collectors.toList());
        //通过类别编号集合和状态码查询商品列表
        List<ProductInfo> productList = productInfoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);
        //多线程 将商品列表转换为dto类型并将 商品列表设置进对应类目的字段中
        List<List<ProductCategoryDto>> categoryProductList = categoryDtos.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setProductInfoDtos(productList.stream()
                    .filter(productInfo -> productInfo.getCategoryType() == productCategoryDto.getCategoryType()).map(ProductInfoDto::build)
                    .collect(Collectors.toList()));
            return categoryDtos;
        }).collect(Collectors.toList());

        return ResultResponse.success(categoryProductList);
    }
}
