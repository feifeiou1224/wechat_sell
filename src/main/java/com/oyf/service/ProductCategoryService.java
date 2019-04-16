package com.oyf.service;

import com.oyf.common.ResultResponse;
import com.oyf.dto.ProductCategoryDto;

import java.util.List;

public interface ProductCategoryService {

    ResultResponse<List<ProductCategoryDto>> findAll();

}
