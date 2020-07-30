package com.fy.shardingjdbcdemo.model;
import java.util.List;
import com.fy.shardingjdbcdemo.model.Orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 订单表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    /**
    * 订单编号
    */
    private Long id;

    /**
    * 用户编号
    */
    private Integer userId;
}