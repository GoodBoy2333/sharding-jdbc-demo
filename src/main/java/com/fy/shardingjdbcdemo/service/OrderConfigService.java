package com.fy.shardingjdbcdemo.service;

import com.fy.shardingjdbcdemo.model.OrderConfig;
public interface OrderConfigService{


    int deleteByPrimaryKey(Integer id);

    int insert(OrderConfig record);

    int insertSelective(OrderConfig record);

    OrderConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderConfig record);

    int updateByPrimaryKey(OrderConfig record);

}
