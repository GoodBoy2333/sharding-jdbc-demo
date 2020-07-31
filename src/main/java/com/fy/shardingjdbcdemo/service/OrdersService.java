package com.fy.shardingjdbcdemo.service;
import java.util.List;

import com.fy.shardingjdbcdemo.model.Orders;
public interface OrdersService{


    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);



	List<Orders> findAll();



	List<Orders> findByUserId(Integer userId);



	List<Orders> findByUserIdOrId(Integer userId,Long id);



	List<Orders> findByUserIdBetween(Integer minUserId,Integer maxUserId);



	Long countByUserId(Integer userId);

    Long countGroupByUserId(Integer userId);





}
