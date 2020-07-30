package com.fy.shardingjdbcdemo;

import com.fy.shardingjdbcdemo.model.OrderConfig;
import com.fy.shardingjdbcdemo.model.Orders;
import com.fy.shardingjdbcdemo.service.OrderConfigService;
import com.fy.shardingjdbcdemo.service.OrdersService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.apache.ibatis.ognl.DynamicSubscript.all;

@SpringBootTest
class ShardingJdbcDemoApplicationTests {

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderConfigService orderConfigService;

    @Test
    void contextLoads_01() {
        OrderConfig orderConfig = orderConfigService.selectByPrimaryKey(null);
        System.out.println(orderConfig);
    }

    @Test
    void contextLoads_02() {
        List<Orders> all = ordersService.findAll();
        System.out.println(all);
    }

    @Test
    void contextLoads_03() {
//        Orders o = Orders.builder().userId(9).build();
//        Orders o = Orders.builder().userId(8).build();
        Orders o = Orders.builder().userId(7).build();
        int insert = ordersService.insert(o);
        System.out.println(insert);
    }

    @Test
    void contextLoads_04() {
//        Orders orders = ordersService.selectByPrimaryKey(1L);
        List<Orders> byUserId = ordersService.findByUserId(9);
        System.out.println(byUserId);
    }

    @Test
    void contextLoads_05() {
        List<Orders> byUserIdOrId = ordersService.findByUserIdOrId(null, null);
        System.out.println(byUserIdOrId);
    }

}
