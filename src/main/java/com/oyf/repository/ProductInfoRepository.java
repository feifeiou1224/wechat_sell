package com.oyf.repository;


import com.oyf.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {

    //根据商品的 状态码 和 类别集合 查询出 每个类型的商品集合
    List<ProductInfo> findByProductStatusAndCategoryTypeIn(Integer status,List<Integer> typeList);

    //根据商品id添加库存
    @Modifying
    @Query(value = "update product_info set product_stock = product_stock + ?2 where product_id = ?1",nativeQuery = true)
    void AddProductStocksByProductId(String productId,Integer quantity);

}
