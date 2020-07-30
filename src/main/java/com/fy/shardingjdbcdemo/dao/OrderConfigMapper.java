package com.fy.shardingjdbcdemo.dao;

import com.fy.shardingjdbcdemo.model.OrderConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderConfig record);

    int insertSelective(OrderConfig record);

    OrderConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderConfig record);

    int updateByPrimaryKey(OrderConfig record);
}