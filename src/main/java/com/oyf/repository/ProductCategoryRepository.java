package com.oyf.repository;

import com.oyf.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//JPA接口 泛型第一个为实体类类型，第二个为该实体类的主键类型
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {

    /*测试：根据 id集合 查询 不在表中的数据*/
    List<ProductCategory> findByCategoryIdNotIn(List<Integer> ids);

}
