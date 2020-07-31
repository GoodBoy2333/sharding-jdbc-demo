package com.fy.shardingjdbcdemo.service;

import com.fy.shardingjdbcdemo.model.OrdersItem;
public interface OrdersItemService{


    int deleteByPrimaryKey(Long id);

    int insert(OrdersItem record);

    int insertSelective(OrdersItem record);

    OrdersItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrdersItem record);

    int updateByPrimaryKey(OrdersItem record);

}
