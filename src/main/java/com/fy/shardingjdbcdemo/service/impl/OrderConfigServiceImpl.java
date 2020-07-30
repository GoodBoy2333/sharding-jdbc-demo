package com.fy.shardingjdbcdemo.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.fy.shardingjdbcdemo.dao.OrderConfigMapper;
import com.fy.shardingjdbcdemo.model.OrderConfig;
import com.fy.shardingjdbcdemo.service.OrderConfigService;
@Service
public class OrderConfigServiceImpl implements OrderConfigService{

    @Resource
    private OrderConfigMapper orderConfigMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return orderConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OrderConfig record) {
        return orderConfigMapper.insert(record);
    }

    @Override
    public int insertSelective(OrderConfig record) {
        return orderConfigMapper.insertSelective(record);
    }

    @Override
    public OrderConfig selectByPrimaryKey(Integer id) {
        return orderConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OrderConfig record) {
        return orderConfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OrderConfig record) {
        return orderConfigMapper.updateByPrimaryKey(record);
    }

}
