package com.fy.shardingjdbcdemo.algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 自定义实现 范围分片算法（RangeShardingAlgorithm）接口
 * 数据库DB的范围分片
 * @author fangyan
 * @since 2020/7/31 10:39
 */
public class RangeShardingDBAlgorithm implements RangeShardingAlgorithm<Integer> {


    @Override
    public Collection<String> doSharding(final Collection<String> databaseNames,
                                         final RangeShardingValue<Integer> shardingValue) {

        /**
         * 自定义SQL ->
         * SELECT *  FROM orders WHERE user_id Between 6 and 8
         * mall0.orders: 2,4,6,8,10
         * mall1.orders: 1,3,5,7,9
         *
         * 执行后对应的数据库为：
         * 6->mall_0,order_2
         * 7->mall_1,order_3
         * 8->mall_0,order_0
         */
        Set<String> result = new LinkedHashSet<>();
        int lower = shardingValue.getValueRange().lowerEndpoint();
        int upper = shardingValue.getValueRange().upperEndpoint();
        for (int i = lower; i <= upper; i++) {
            for (String each : databaseNames) {
                if (each.endsWith(i % databaseNames.size() + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
