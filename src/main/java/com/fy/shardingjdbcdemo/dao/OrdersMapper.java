package com.fy.shardingjdbcdemo.dao;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.fy.shardingjdbcdemo.model.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> findAll();

    List<Orders> findByUserId(@Param("userId")Integer userId);




}