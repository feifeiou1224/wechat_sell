package com.oyf.repository;

import com.oyf.dto.OrderMasterDto;
import com.oyf.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
}
