package com.fy.shardingjdbcdemo.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * <p>
 * 精准分表
 * </p >
 *
 * @author fangyan
 * @since 2020/7/31 11:12
 */

public class PreciseShardingTableAlgorithm implements PreciseShardingAlgorithm<Integer> {

    private int shardingTableIndex = 4;

    /**
     * 注释键 PreciseShardingDBAlgorithm
     *
     * @param tableNames
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> tableNames,
                             PreciseShardingValue<Integer> shardingValue) {
        for (String key : tableNames) {
            if (key.endsWith(shardingValue.getValue() % shardingTableIndex + "")) {
                return key;
            }
        }
        throw new UnsupportedOperationException();
    }

}
