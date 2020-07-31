package com.fy.shardingjdbcdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 订单详情表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersItem {
    /**
    * 订单编号
    */
    private Long id;

    /**
    * 订单编号
    */
    private Long orderId;

    private Integer userId;
}