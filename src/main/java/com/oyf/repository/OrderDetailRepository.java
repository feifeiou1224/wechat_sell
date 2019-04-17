package com.oyf.repository;

import com.oyf.dto.OrderDetailDto;
import com.oyf.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {
}
