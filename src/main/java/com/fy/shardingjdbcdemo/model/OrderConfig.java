package com.fy.shardingjdbcdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 订单配置表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfig {
    /**
    * 编号
    */
    private Integer id;

    /**
    * 支付超时时间;单位：分钟
    */
    private Integer payTimeout;
}