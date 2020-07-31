package com.fy.shardingjdbcdemo.algorithm;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * 范围分表
 * </p >
 *
 * @author fangyan
 * @since 2020/7/31 11:13
 */
public class RangeShardingTableAlgorithm implements RangeShardingAlgorithm<Integer> {

    private int shardingTableIndex = 4;

    @Override
    public Collection<String> doSharding(final Collection<String> tableNames,
                                         final RangeShardingValue<Integer> shardingValue) {
        Set<String> result = new LinkedHashSet<>();
        int lower = shardingValue.getValueRange().lowerEndpoint();
        int upper = shardingValue.getValueRange().upperEndpoint();
        for (int i = lower; i <= upper; i++) {
            for (String each : tableNames) {
                if (each.endsWith(i % shardingTableIndex + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }

}
