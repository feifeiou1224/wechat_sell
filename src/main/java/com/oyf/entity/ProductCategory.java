package com.oyf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Create Time: 2019年04月16日 15:05
 * Create Author: 欧阳飞
 **/

@Entity //表示该类是实体类
@DynamicUpdate  // 设置为true,表示update对象的时候,生成动态的update语句,如果这个字段的值是null就不会被加入到update语句中
@Data   //相当于get set toString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_category") //在数据库中对应的表名
public class ProductCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //表示自增IDENTITY：mysql SEQUENCE:oracle
    private Integer categoryId;

    private String categoryName;

    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
