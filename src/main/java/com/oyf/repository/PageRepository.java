package com.oyf.repository;

import com.oyf.entity.OrderMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PageRepository extends PagingAndSortingRepository<OrderMaster,String> {

    List<OrderMaster> findByBuyerOpenid(String openId, Pageable pageable);

}
