package com.fy.shardingjdbcdemo.dao;

import com.fy.shardingjdbcdemo.model.OrdersItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrdersItem record);

    int insertSelective(OrdersItem record);

    OrdersItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrdersItem record);

    int updateByPrimaryKey(OrdersItem record);
}