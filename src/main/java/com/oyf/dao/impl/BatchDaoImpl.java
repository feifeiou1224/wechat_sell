package com.oyf.dao.impl;

import com.oyf.dao.BatchDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Create Time: 2019年04月17日 15:03
 * Create Author: 欧阳飞
 **/

/*
*   用来实现批量插入，想要使用这个方法时可以直接继承该实现类
*
* */

@Repository
public class BatchDaoImpl<T> implements BatchDao<T> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public void batchInsert(List<T> list) {

        //获取批量添加的条数
        long size = list.size();
        for (int i = 0; i < size; i++) {
            em.persist(list.get(i));
            if (i % 100 == 0 || i == size - 1){//每100条或者集合长度进行一次批量插入
                em.flush(); //流的刷新方法
                em.clear(); //流的清理方法
            }
        }
    }

}
