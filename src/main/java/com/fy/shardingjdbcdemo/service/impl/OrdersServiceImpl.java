package com.fy.shardingjdbcdemo.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.fy.shardingjdbcdemo.dao.OrdersMapper;
import com.fy.shardingjdbcdemo.model.Orders;
import com.fy.shardingjdbcdemo.service.OrdersService;

import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService{

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ordersMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Orders record) {
        return ordersMapper.insert(record);
    }

    @Override
    public int insertSelective(Orders record) {
        return ordersMapper.insertSelective(record);
    }

    @Override
    public Orders selectByPrimaryKey(Long id) {
        return ordersMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Orders record) {
        return ordersMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Orders record) {
        return ordersMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Orders> findAll() {
        return ordersMapper.findAll();
    }

	@Override
	public List<Orders> findByUserId(Integer userId){
		 return ordersMapper.findByUserId(userId);
	}

	@Override
	public List<Orders> findByUserIdOrId(Integer userId,Long id){
		 return ordersMapper.findByUserIdOrId(userId,id);
	}

	@Override
	public List<Orders> findByUserIdBetween(Integer minUserId,Integer maxUserId){
		 return ordersMapper.findByUserIdBetween(minUserId,maxUserId);
	}

	@Override
	public Long countByUserId(Integer userId){
		 return ordersMapper.countByUserId(userId);
	}


    @Override
    public Long countGroupByUserId(Integer userId) {
        return ordersMapper.countGroupByUserId(userId);
    }
}
