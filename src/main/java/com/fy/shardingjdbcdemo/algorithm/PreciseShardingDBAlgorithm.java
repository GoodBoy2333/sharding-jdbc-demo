package com.fy.shardingjdbcdemo.algorithm;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * <p>
 *
 * </p >
 *
 * @author fangyan
 * @since 2020/7/30 20:30
 */
public class PreciseShardingDBAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
        /*
         * 作用：散列到具体的哪个库里面去
         * shardingValue ： SQL -> SELECT *  FROM t_order WHERE order _id IN(1,3,6)
         * shardingValue = [1,3,6]
         * */
        for (String each : collection) {
            /**
             * 此方法如果参数所表示的字符序列是由该对象表示的字符序列的后缀返回true, 否则为false;
             *  请注意，如果参数是空字符串或等于此String对象由equals（Object）方法确定结果为 true。
             *  ds0.endsWith("0") -> true ;
             */
            if (each.endsWith(String.valueOf(preciseShardingValue.getValue() % collection.size()))) {
                //返回相应的数据库
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
