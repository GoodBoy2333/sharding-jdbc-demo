package com.fy.shardingjdbcdemo.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.fy.shardingjdbcdemo.dao.OrdersItemMapper;
import com.fy.shardingjdbcdemo.model.OrdersItem;
import com.fy.shardingjdbcdemo.service.OrdersItemService;
@Service
public class OrdersItemServiceImpl implements OrdersItemService{

    @Resource
    private OrdersItemMapper ordersItemMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ordersItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OrdersItem record) {
        return ordersItemMapper.insert(record);
    }

    @Override
    public int insertSelective(OrdersItem record) {
        return ordersItemMapper.insertSelective(record);
    }

    @Override
    public OrdersItem selectByPrimaryKey(Long id) {
        return ordersItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OrdersItem record) {
        return ordersItemMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OrdersItem record) {
        return ordersItemMapper.updateByPrimaryKey(record);
    }

}
