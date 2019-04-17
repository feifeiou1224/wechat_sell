package com.oyf.service.impl;

import com.oyf.dao.BatchDao;
import com.oyf.entity.OrderDetail;
import com.oyf.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create Time: 2019年04月17日 15:21
 * Create Author: 欧阳飞
 **/

@Service
public class OrderDetailServiceImpl implements OrderDetailService  {

    @Autowired
    private BatchDao<OrderDetail> batchDao;

    @Override
    @Transactional
    public void batchInsert(List<OrderDetail> orderDetailList) {
        batchDao.batchInsert(orderDetailList);
    }

}
