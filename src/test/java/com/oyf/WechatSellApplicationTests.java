package com.oyf;

import com.google.common.collect.Lists;
import com.oyf.entity.ProductCategory;
import com.oyf.repository.ProductCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class WechatSellApplicationTests {

	@Autowired
	private ProductCategoryRepository repository;

	@Test
	public void contextLoads() {

/*        List<ProductCategory> all = repository.findAll();
        all.stream().forEach(System.out::println);*/

        ArrayList<Integer> ids = Lists.newArrayList(1,2,3);
        List<ProductCategory> byCategoryIdNotIn = repository.findByCategoryIdNotIn(ids);
        byCategoryIdNotIn.stream().forEach(System.out::println);

    }



}
