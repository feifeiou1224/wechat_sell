package com.oyf.service.impl;

import com.oyf.common.ResultResponse;
import com.oyf.dto.ProductCategoryDto;
import com.oyf.entity.ProductCategory;
import com.oyf.repository.ProductCategoryRepository;
import com.oyf.service.ProductCategoryService;
import com.oyf.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Create Time: 2019年04月16日 16:57
 * Create Author: 欧阳飞
 **/

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ResultResponse<List<ProductCategoryDto>> findAll() {

        List<ProductCategory> all = productCategoryRepository.findAll();
        //转换为dto
        List<ProductCategoryDto> collect = all.stream().map(ProductCategoryDto::build).collect(Collectors.toList());

        return ResultResponse.success(collect);
    }
}
