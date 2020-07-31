package com.fy.shardingjdbcdemo;

import com.fy.shardingjdbcdemo.model.OrderConfig;
import com.fy.shardingjdbcdemo.model.Orders;
import com.fy.shardingjdbcdemo.model.OrdersItem;
import com.fy.shardingjdbcdemo.service.OrderConfigService;
import com.fy.shardingjdbcdemo.service.OrdersItemService;
import com.fy.shardingjdbcdemo.service.OrdersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShardingJdbcDemoApplicationTests {

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderConfigService orderConfigService;

    @Autowired
    OrdersItemService ordersItemService;

    @Test
    void contextLoads_01() {
        OrderConfig orderConfig = orderConfigService.selectByPrimaryKey(1);
        System.out.println(orderConfig);
    }

    @Test
    void contextLoads_02() {
        Orders o = Orders.builder().userId(4).build();
        int insert = ordersService.insert(o);
        System.out.println(insert);
    }

    @Test
    void contextLoads_04() {
        Orders orders = ordersService.selectByPrimaryKey(1L);
        System.out.println(orders);
    }

    @Test
    void contextLoads_05() {
        List<Orders> byUserId = ordersService.findByUserId(3);
        System.out.println(byUserId);
    }

    @Test
    void contextLoads_06() {
        List<Orders> byUserId = ordersService.findByUserIdOrId(7, 2L);
        System.out.println(byUserId);
    }

    @Test
    void contextLoads_07() {
        List<Orders> byUserId = ordersService.findByUserIdBetween(6, 8);
        System.out.println(byUserId);
    }

    @Test
    void contextLoads_08() {
        Long aLong = ordersService.countGroupByUserId(2);
        System.out.println(aLong);
    }
}
