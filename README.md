# 分库分表简介

在准备开发工作前，我们需要了解一下分库分表的基础知识，为什么要分库分表，有哪些优缺点。带着问题去实践收获更多。

1. 为分库分表一定是为了**支撑高并发、数据量大**两个问题的。
2. 分库分表插件：
   1. Cobar
   2. TDDL
   3. Atlas
   4. Sharding-jdbc
   5. Mycat

**Cobar**：阿里 b2b 团队开发和开源的，属于 proxy 层方案，就是介于应用服务器和数据库服务器之间。应用程序通过 JDBC 驱动访问 Cobar 集群，Cobar 根据 SQL 和分库规则对 SQL 做分解，然后分发到 MySQL 集群不同的数据库实例上执行。早些年还可以用，但是最近几年都没更新了，基本没啥人用，差不多算是被抛弃的状态吧。而且不支持读写分离、存储过程、跨库 join 和分页等操作。

**TDDL**：淘宝团队开发的，属于 client 层方案。支持基本的 crud 语法和读写分离，但不支持 join、多表查询等语法。目前使用的也不多，因为还依赖淘宝的 diamond 配置管理系统。

**Atlas**：360 开源的，属于 proxy 层方案，以前是有一些公司在用的，但是确实有一个很大的问题就是社区最新的维护都在 5 年前了。所以，现在用的公司基本也很少了。

**Sharding-jdbc**：当当开源的，属于 client 层方案，是 ShardingSphere 的 client 层方案， ShardingSphere 还提供 proxy 层的方案 Sharding-Proxy。确实之前用的还比较多一些，因为 SQL 语法支持也比较多，没有太多限制，而且截至 2019.4，已经推出到了 4.0.0-RC1 版本，支持分库分表、读写分离、分布式 id 生成、柔性事务（最大努力送达型事务、TCC 事务）。而且确实之前使用的公司会比较多一些（这个在官网有登记使用的公司，可以看到从 2017 年一直到现在，是有不少公司在用的），目前社区也还一直在开发和维护，还算是比较活跃，个人认为算是一个现在也**可以选择的方案**。

**Mycat**：基于 Cobar 改造的，属于 proxy 层方案，支持的功能非常完善，而且目前应该是非常火的而且不断流行的数据库中间件，社区很活跃，也有一些公司开始在用了。但是确实相比于 Sharding jdbc 来说，年轻一些，经历的锤炼少一些。

**总结**：综上，现在其实建议考量的，就是 Sharding-jdbc 和 Mycat，这两个都可以去考虑使用。

Sharding-jdbc 这种 client 层方案的**优点在于不用部署，运维成本低，不需要代理层的二次转发请求，性能很高**，但是如果遇到升级啥的需要各个系统都重新升级版本再发布，各个系统都需要**耦合** Sharding-jdbc 的依赖。

Mycat 这种 proxy 层方案的**缺点在于需要部署**，自己运维一套中间件，运维成本高，但是**好处在于对于各个项目是透明的**，如果遇到升级之类的都是自己中间件那里搞就行了。

**水平拆分**的意思，就是把一个表的数据给弄到多个库的多个表里去，但是每个库的表结构都一样，只不过每个库表放的数据是不同的，所有库表的数据加起来就是全部数据。水平拆分的意义，就是将数据均匀放更多的库里，然后用多个库来扛更高的并发，还有就是用多个库的存储容量来进行扩容。

![](https://mmbiz.qpic.cn/mmbiz_png/Z2FKxRcFptNRl1m1sIU87B01TmNeHZITPbztdiaqI2xYJRNsQGNcxBZGxSgfq8aibWpkYpHQjfXSY6BNBzKoMfPg/0?wx_fmt=png)

**垂直拆分**的意思，就是**把一个有很多字段的表给拆分成多个表**，**或者是多个库上去**。每个库表的结构都不一样，每个库表都包含部分字段。一般来说，会**将较少的访问频率很高的字段放到一个表里去**，然后**将较多的访问频率很低的字段放到另外一个表里去**。因为数据库是有缓存的，你访问频率高的行字段越少，就可以在缓存里缓存更多的行，性能就越好。这个一般在表层面做的较多一些。

![](https://mmbiz.qpic.cn/mmbiz_png/Z2FKxRcFptNRl1m1sIU87B01TmNeHZITvk6yVEzTdbFo9GVibJbuauR5IhQuAU5YavvYnBYmqoHuQ0Oul2YAh1A/0?wx_fmt=png)

**表层面的拆分**，就是分表，将一个表变成 N 个表，就是**让每个表的数据量控制在一定范围内**，保证 SQL 的性能。否则单表数据量越大，SQL 性能就越差。一般是 200 万行左右，不要太多，但是也得看具体你怎么操作，也可能是 500 万，或者是 100 万。你的SQL越复杂，就最好让单表行数越少。好了，无论分库还是分表，上面说的那些数据库中间件都是可以支持的。就是基本上那些中间件可以做到你分库分表之后，**中间件可以根据你指定的某个字段值**，比如说 userid，**自动路由到对应的库上去，然后再自动路由到对应的表里去**。

## 两种分库分表的方式

一种是按照 range 来分，就是每个库一段连续的数据，这个一般是按比如**时间范围**来的，但是这种一般较少用，因为很容易产生热点问题，大量的流量都打在最新的数据上了。或者是按照某个字段 hash 一下均匀分散，这个较为常用。range 来分，好处在于说，扩容的时候很简单，因为你只要预备好，给每个月都准备一个库就可以了，到了一个新的月份的时候，自然而然，就会写新的库了；缺点，但是大部分的请求，都是访问最新的数据。实际生产用 range，要看场景。hash 分发，好处在于说，可以平均分配每个库的数据量和请求压力；坏处在于说扩容起来比较麻烦，会有一个数据迁移的过程，之前的数据需要重新计算 hash 值重新分配到不同的库或表。



------



## ShardingSphere入门开发



本小节，我们会使用ShardingSphere的子项目Sharding-JDBC 实现分库分表的功能，方式为**水平分表分库，hash均匀分布**。我们会将 `orders` 订单表，拆分到 **2** 个库，每个库 **2** 张订单表，一共 **4** 张表。订单详情表与配置表介绍Sharding-jdbc功能点使用，不必重点关注。库表的情况如下：

```
mall_0 库
  ├── orders_0
  └── orders_2
  └── orders_config
  └── orders_item_0
  └── orders_item_2
mall_1 库
  ├── orders_1
  └── orders_3
  └── orders_item_1
  └── orders_item_3
```

**数据库数量：2**

**数据表数量：4**

偶数后缀的表，在 mall_0 库下。奇数后缀的表，在 mall_1 库下。我们使用订单表上的 user_id 用户编号，进行分库分表的规则：

首先，按照 index = user_id % 2 计算，将记录路由到 mall_${index} 库。_

_然后，按照 index = user_id % 4 计算，将记录路由到 orders_${index} 表。

| 用户编号 | 数据库 | 数据表   |
| -------- | ------ | -------- |
| 1        | mall_1 | orders_1 |
| 2        | mall_0 | orders_2 |
| 3        | mall_1 | orders_3 |
| 4        | mall_0 | orders_0 |
| 5        | mall_1 | orders_1 |
| 6        | mall_0 | orders_2 |
| 7        | mall_1 | orders_3 |
| 8        | mall_0 | orders_0 |
| 9        | mall_1 | orders_1 |
| 10       | mall_0 | orders_2 |

因为本文重心在于提供示例。小伙伴们碰到不理解的地方，可以看看如下文档：

- [《ShardingSphere > 概念 & 功能 > 数据分片》](https://shardingsphere.apache.org/document/current/cn/features/sharding/concept/sharding/)

- [《ShardingSphere > 概念 & 功能 > 数据分片> 核心概念》](https://shardingsphere.apache.org/document/current/cn/features/sharding/concept/sharding/)

- [《ShardingSphere > 用户手册 > Sharding-JDBC > 使用手册 > 数据分片》](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/configuration/java-api/sharding/)

- ### 引入依赖

```java
<dependencies>
        <!-- 实现对 MyBatis 的自动化配置 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>
        <!-- MySQL5驱动 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.48</version>
        </dependency>
        <!-- 阿里连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.6</version>
        </dependency>
        <!-- 简化代码 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- sharding-jdbc自动化配置 -->
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.0.1</version>
        </dependency>
        <!-- 单元测试 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
```

- ### 应用配置文件

```
spring:
#  ShardingSphere 配置项
  shardingsphere:
    datasource:
      # 所有数据源的名字
      names: ds-mall-0, ds-mall-1
      # 数据源配置 00
      ds-mall-0:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://ngrok2.xiaomiqiu.cn:3037/mall_0?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: 123456
      # 数据源配置 01
      ds-mall-1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://ngrok2.xiaomiqiu.cn:3038/mall_1?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: 123456
    # 分片规则
    sharding:
      # 默认分表策略
#      default-table-strategy:
#        standard:
#          # 精确分片算法,用于处理使用单一键作为分片键的 = 与 IN 进行分片的场景
#          precise-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.PreciseShardingTableAlgorithm
#          # 范围分片算法,用于处理使用单一键作为分片键的 BETWEEN AND、>、<、>=、<=进行分片的场景
#          range-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.RangeShardingTableAlgorithm
#          # 分片键
#          sharding-column: user_id
#      # 默认分库策略
#      default-database-strategy:
#        standard:
#          precise-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.PreciseShardingDBAlgorithm
#          range-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.RangeShardingDBAlgorithm
#          sharding-column: user_id
      # 绑定表：分片规则一致的主表和子表
      binding-tables[0]: orders,orders_item
      tables:
        # orders 表配置
        orders:
          actualDataNodes: ds-mall-0.orders_$->{[0,2]}, ds-mall-1.orders_$->{[1,3]}
          # 主键生成策略:雪花算法
          key-generator:
            column: id
            type: SNOWFLAKE
          database-strategy:
            # 行表达式分片策略
            inline:
              # 算法表达式
              algorithm-expression: ds-mall-$->{user_id % 2}
              # 分片键
              sharding-column: user_id
          table-strategy:
            inline:
              algorithm-expression: orders_$->{user_id % 4}
              sharding-column: user_id
        # orders 表配置
        orders_item:
          actualDataNodes: ds-mall-0.orders_item_$->{[0,2]}, ds-mall-1.orders_item_$->{[1,3]}
          key-generator: # 主键生成策略:雪花算法
            column: id
            type: SNOWFLAKE
          database-strategy:
            inline:
              algorithm-expression: ds-mall-$->{user_id % 2}
              sharding-column: user_id
          table-strategy:
            inline:
              algorithm-expression: orders_item_$->{user_id % 4}
              sharding-column: user_id
        # order_config 表配置
        order_config:
          actualDataNodes: ds-mall-0.order_config # 仅映射到 ds-orders-0 数据源的 order_config 表
    # 拓展属性配置
    props:
      sql:
        show: true # 打印 SQL

# mybatis 配置内容
mybatis:
  mapper-locations: classpath:mapper/*.xml # 配置 Mapper XML 地址
  type-aliases-package: com.fy.shardingjdbcdemo.model # 配置数据库实体包路径
```

spring.shardingsphere.sharding 配置项，我们配置了 orders 和 order_config 逻辑表 。

1. 逻辑表 ：水平拆分的数据库（表）的相同逻辑和数据结构表的总称。例：订单数据根据主键尾数拆分为 10 张表，分别是 order_0 到 order_9 ，他们的逻辑表名为 order 。

2. 真实表 ：在分片的数据库中真实存在的物理表。即上个示例中的 order_0 到 order_9 。

3. 数据节点 ：数据分片的最小单元。由数据源名称和数据表组成，例：mall_0.t_order_0 。


orders 配置项，设置 orders 逻辑表，使用分库分表的规则。

actualDataNodes ：对应的数据节点，使用的是行表达式 。这里的意思是，ds-mall-0.orders_0,  ds-mall-0.orders_1，ds-mall-0.orders_2, ds-mall-1.orders_1，ds-mall-1.orders_2，ds-mall-1.orders_3

key-generator ：主键生成策略。这里采用分布式主键 SNOWFLAKE 方案。更多可以看 《 ShardingSphere > 概念 & 功能 > 数据分片 > 其他功能 > 分布式主键》 文档。

database-strategy ：按照 index = user_id % 2 分库，路由到 ds-mall-${index} 数据源（库）。

table-strategy ：index = user_id % 4 分表，路由到 orders_${index} 数据表。

order_config 配置项，设置 order_config 逻辑表，不使用分库分表。

actualDataNodes ：对应的数据节点，只对应数据源（库）为 ds-mall-0的order_config 表。

spring.shardingsphere.props 配置项，设置拓展属性配置。

### 创建数据库

sql脚本在源码database下。

### 查询配置表数据

```
@SpringBootTest
class ShardingJdbcDemoApplicationTests {
    @Autowired
    OrderConfigService orderConfigService;

    @Test
    void contextLoads_01() {
        OrderConfig orderConfig = orderConfigService.selectByPrimaryKey(1);
        System.out.println(orderConfig);
    }
}
```

日志

```
2020-07-31 16:48:27.698  INFO 4704 --- [           main] ShardingSphere-SQL                       : Logic SQL: select
     
    id, pay_timeout
   
    from order_config
    where id = ?
2020-07-31 16:48:27.699  INFO 4704 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: select
     
    id, pay_timeout
   
    from order_config
    where id = ? ::: [1]
null
```

- Logic SQL ：逻辑 SQL 日志，就是我们编写的。
- Actual SQL ：物理 SQL 日志，实际 Sharding-JDBC 向数据库真正发起的日志。
  - 在这里，我们可以看到 ds-mall-0 ，表名该物理 SQL ，是路由到 ds-mall-0 数据源执行。
  - 同时，查询的是 `order_config` 表。
  - 符合我们配置的 `order_config` 逻辑表，不使用分库分表，对应的数据节点仅有 `ds-mall-0.order_config` 。

### 通过id查询订单数据

```
@Test
void contextLoads_04() {
    Orders orders = ordersService.selectByPrimaryKey(1L);
    System.out.println(orders);
}
```

日志

```
2020-07-31 16:52:28.236  INFO 5600 --- [           main] ShardingSphere-SQL                       : Logic SQL: select
     
    id, user_id
   
    from orders
    where id = ?
2020-07-31 16:52:28.237  INFO 5600 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: select
     
    id, user_id
   
    from orders_0
    where id = ? ::: [1]
2020-07-31 16:52:28.237  INFO 5600 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: select
     
    id, user_id
   
    from orders_2
    where id = ? ::: [1]
2020-07-31 16:52:28.237  INFO 5600 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select
     
    id, user_id
   
    from orders_1
    where id = ? ::: [1]
2020-07-31 16:52:28.237  INFO 5600 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select
     
    id, user_id
   
    from orders_3
    where id = ? ::: [1]
null
```

- 明明只有一条 Logic SQL 操作，却发起了 4 条 Actual SQL 操作。这是为什么呢？

- 我们使用 `id = ?` 作为查询条件，因为 Sharding-JDBC 解析不到我们配置的 `user_id` 片键（分库分表字段），作为查询字段，所以只好 [全库表路由](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/route/#全库表路由) ，查询所有对应的数据节点，也就是配置的所有数据库的数据表。这样，在获得所有查询结果后，通过 [归并引擎](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/merge/) 合并返回最终结果。

  > 通过将 Actual SQL 在每个数据库的数据表执行，返回的结果都是符合条件的。
  >
  > 这样，和使用 Logic SQL 在逻辑表中执行的结果，实际是一致的。
  >
  > 胖友可以试着想一想噢。如果还是有疑惑，可以给艿艿留言。

- 那么，一次性发起这么多条 Actual SQL 是不是会顺序执行，导致很慢呢？实际上，Sharding-JDBC 有 [执行引擎](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/execute/) ，会并行执行这多条 Actual SQL 操作。所以呢，最终操作时长，由最慢的 Actual SQL 所决定。

- 虽然说，[执行引擎](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/execute/) 提供了并行执行 Actual SQL 操作的能力，我们还是推荐尽可能查询的时候，带有片键（分库分表字段）。对 Sharding-JDBC 性能感兴趣的小伙伴，可以看看 [《Sharding-JDBC 性能测试报告》](https://shardingsphere.apache.org/document/current/cn/manual/sharding-jdbc/stress-test/) 。

### 通过用户id查询订单数据

```
@Test
void contextLoads_05() {
    List<Orders> byUserId = ordersService.findByUserId(3);
    System.out.println(byUserId);
}
```

日志

```
2020-07-31 16:57:34.971  INFO 21768 --- [           main] ShardingSphere-SQL                       : Logic SQL: select
     
    id, user_id
   
    from orders
    where user_id=?
2020-07-31 16:57:34.972  INFO 21768 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select
     
    id, user_id
   
    from orders_3
    where user_id=? ::: [3]
[Orders(id=495962842143391744, userId=3)]
```

- 一条 Logic SQL 操作，发起了 1 条 Actual SQL 操作。这是为什么呢？
- 我们使用 user_id = ? 作为查询条件，因为 Sharding-JDBC 解析到我们配置的 user_id 片键（分库分表字段），作为查询字段，所以可以 标准路由 ，仅查询一个数据节点。这种，是 Sharding-JDBC 最为推荐使用的分片方式。
- 分库：user_id % 2 等于 3 % 2 = 1 ，所以路由到 ds-mall-1 数据源。
- 分表：user_id % 4 等于 3 % 4 = 3 ，所以路由到 orders_3 数据表。
- 两者一结合，只查询 ds-orders-1.orders_1 数据节点。

### 增加订单表数据

```
@Test
void contextLoads_02() {
     Orders o = Orders.builder().userId(4).build();
     int insert = ordersService.insert(o);
     System.out.println(insert);
}
```

日志

```
2020-07-31 17:02:28.004  INFO 13560 --- [           main] ShardingSphere-SQL                       : Logic SQL: insert into orders (user_id)
    values (?)
2020-07-31 17:02:28.004  INFO 13560 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: insert into orders_0 (user_id, id)
    values (?, ?) ::: [4, 496003911589560320]
1
```

- 不考虑 [广播表](https://shardingsphere.apache.org/document/current/cn/features/sharding/concept/sql/) 的情况下，插入语句必须带有片键（分库分表字段），否则 [执行引擎](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/execute/) 不知道插入到哪个数据库的哪个数据表中。毕竟，插入操作必然是单库单表。
- 我们会发现，Actual SQL 相比 Logic SQL 来说，增加了主键 `id` 为 496003911589560320。这是为什么呢？我们配置 `orders` 逻辑表，使用 SNOWFLAKE 算法生成分布式主键，而 [改写引擎](https://shardingsphere.apache.org/document/current/cn/features/sharding/principle/rewrite/) 在发现我们的 Logic SQL 并未设置插入的 `id` 主键编号，它会自动生成主键，改写 Logic SQL ，附加 `id` 成 Logic SQL 。

### 级联查询

```
@Test
void contextLoads_06() {
    List<Orders> byUserId = ordersService.findByUserIdOrId(7, 2L);
    System.out.println(byUserId);
}
```

对应sql

```
  <select id="findByUserIdOrId" resultMap="BaseResultMap">
    select i.id,i.user_id
    from orders o left join orders_item i on o.id=i.order_id
    where 1=1
    <if test="userId!=null and userId!=''">
      and o.user_id = #{userId,jdbcType=INTEGER}
    </if >
    <if test="id!=null and id!=''">
      and o.id = #{id,jdbcType=BIGINT}
    </if >
  </select>
```

日志

```
2020-07-31 17:08:44.560  INFO 8292 --- [           main] ShardingSphere-SQL                       : Logic SQL: select i.id,i.user_id
    from orders o left join orders_item i on o.id=i.order_id
    where 1=1
     
      and o.user_id = ?
     
     
      and o.id = ?
2020-07-31 17:08:44.561  INFO 8292 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select i.id,i.user_id
    from orders_3 o left join orders_item_1 i on o.id=i.order_id
    where 1=1
     
      and o.user_id = ?
     
     
      and o.id = ? ::: [7, 2]
2020-07-31 17:08:44.561  INFO 8292 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select i.id,i.user_id
    from orders_3 o left join orders_item_3 i on o.id=i.order_id
    where 1=1
     
      and o.user_id = ?
     
     
      and o.id = ? ::: [7, 2]
[]
```

此时发送sql为两条，出现[笛卡尔积关联](https://shardingsphere.apache.org/document/current/cn/features/sharding/concept/sql/)，多次查询子表。

#### 配置文件中添加绑定表关系

```
binding-tables[0]: orders,orders_item
```

再次执行后的日志

```
2020-07-31 17:16:14.742  INFO 9668 --- [           main] ShardingSphere-SQL                       : Logic SQL: select i.id,i.user_id
    from orders o left join orders_item i on o.id=i.order_id
    where 1=1
     
      and o.user_id = ?
     
     
      and o.id = ?
2020-07-31 17:16:14.742  INFO 9668 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select i.id,i.user_id
    from orders_3 o left join orders_item_3 i on o.id=i.order_id
    where 1=1
     
      and o.user_id = ?
     
     
      and o.id = ? ::: [7, 2]
[]
```

至此，我们已经完成了一个 Sharding-JDBC 的简单的分库分表的示例。仅支持 SQL 语句中的 `=` 和 `IN` 的分片操作，单分片键。如果 SQ L语句中出现 `=`, `>`, `<`, `>=`, `<=`, `IN` 和 `BETWEEN AND` 的分片操作将会报错不支持，下面我们介绍自定义分片算法实现SQL中的操作符。

### 配置文件

```
spring:
#  ShardingSphere 配置项
  shardingsphere:
    datasource:
      # 所有数据源的名字
      names: ds-mall-0, ds-mall-1
      # 数据源配置 00
      ds-mall-0:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://ngrok2.xiaomiqiu.cn:3037/mall_0?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: 123456
      # 数据源配置 01
      ds-mall-1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://ngrok2.xiaomiqiu.cn:3038/mall_1?useSSL=false&useUnicode=true&characterEncoding=UTF-8
        username: root
        password: 123456
    # 分片规则
    sharding:
      # 默认分表策略
      default-table-strategy:
        standard:
          # 精确分片算法,用于处理使用单一键作为分片键的 = 与 IN 进行分片的场景
          precise-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.PreciseShardingTableAlgorithm
          # 范围分片算法,用于处理使用单一键作为分片键的 BETWEEN AND、>、<、>=、<=进行分片的场景
          range-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.RangeShardingTableAlgorithm
          # 分片键
          sharding-column: user_id
      # 默认分库策略
      default-database-strategy:
        standard:
          precise-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.PreciseShardingDBAlgorithm
          range-algorithm-class-name: com.fy.shardingjdbcdemo.algorithm.RangeShardingDBAlgorithm
          sharding-column: user_id
      # 绑定表：分片规则一致的主表和子表
      binding-tables[0]: orders,orders_item
      tables:
        # orders 表配置
        orders:
          actualDataNodes: ds-mall-0.orders_$->{[0,2]}, ds-mall-1.orders_$->{[1,3]}
          # 主键生成策略:雪花算法
          key-generator:
            column: id
            type: SNOWFLAKE
#          database-strategy:
#            # 行表达式分片策略
#            inline:
#              # 算法表达式
#              algorithm-expression: ds-mall-$->{user_id % 2}
#              # 分片键
#              sharding-column: user_id
#          table-strategy:
#            inline:
#              algorithm-expression: orders_$->{user_id % 4}
#              sharding-column: user_id
        # orders 表配置
        orders_item:
          actualDataNodes: ds-mall-0.orders_item_$->{[0,2]}, ds-mall-1.orders_item_$->{[1,3]}
          key-generator: # 主键生成策略:雪花算法
            column: id
            type: SNOWFLAKE
#          database-strategy:
#            inline:
#              algorithm-expression: ds-mall-$->{user_id % 2}
#              sharding-column: user_id
#          table-strategy:
#            inline:
#              algorithm-expression: orders_item_$->{user_id % 4}
#              sharding-column: user_id
        # order_config 表配置
        order_config:
          actualDataNodes: ds-mall-0.order_config # 仅映射到 ds-orders-0 数据源的 order_config 表
    # 拓展属性配置
    props:
      sql:
        show: true # 打印 SQL

# mybatis 配置内容
mybatis:
  mapper-locations: classpath:mapper/*.xml # 配置 Mapper XML 地址
  type-aliases-package: com.fy.shardingjdbcdemo.model # 配置数据库实体包路径
```

改动为注释单表配置的数据库分库分表策略，支持全局默认**精准分库分表策略**与**范围分库分表策略**并添加自定义分库分表算法。

精准分库算法实现：

```
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
```

精准分表算法实现：

```
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
```

测试代码

```
@Test
void contextLoads_07() {
    List<Orders> byUserId = ordersService.findByUserIdBetween(6, 8);
    System.out.println(byUserId);
}
```

代码sql

```
<select id="findByUserIdBetween" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List"/>
    from orders
    where user_id <![CDATA[>]]> #{minUserId,jdbcType=INTEGER} and user_id <![CDATA[<]]> #{maxUserId,jdbcType=INTEGER}
</select>
```

断点调试

![](https://mmbiz.qpic.cn/mmbiz_png/Z2FKxRcFptNRl1m1sIU87B01TmNeHZITia3ibLWdVzPOnTRwbBo4Qx079TDlFWJYfNfBMcwwiaCVoLicZDHFqBlibFQ/0?wx_fmt=png)

通过求余运算得到库名称

![](https://mmbiz.qpic.cn/mmbiz_png/Z2FKxRcFptNRl1m1sIU87B01TmNeHZITNt9gOicaZicPhCxGccawTCoCqWUYjiaGXjCH61GbsoVEMKp2vKJRhdR8Q/0?wx_fmt=png)

![](https://mmbiz.qpic.cn/mmbiz_png/Z2FKxRcFptNRl1m1sIU87B01TmNeHZIT7lUiaMxRMLKpkxIibLtKZgyzMXwS4ks2RibmCqjHAHwia3C4VwjZwSPLUQ/0?wx_fmt=png)

日志

```
2020-07-31 17:29:12.117  INFO 660 --- [           main] ShardingSphere-SQL                       : Logic SQL: select
     
    id, user_id
   
    from orders
    where user_id  >  ? and user_id  <  ?
2020-07-31 17:29:12.117  INFO 660 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: select
     
    id, user_id
   
    from orders_0
    where user_id  >  ? and user_id  <  ? ::: [6, 8]
2020-07-31 17:29:12.117  INFO 660 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-0 ::: select
     
    id, user_id
   
    from orders_2
    where user_id  >  ? and user_id  <  ? ::: [6, 8]
2020-07-31 17:29:12.117  INFO 660 --- [           main] ShardingSphere-SQL                       : Actual SQL: ds-mall-1 ::: select
     
    id, user_id
   
    from orders_3
    where user_id  >  ? and user_id  <  ? ::: [6, 8]
[]
```

我们可以通过上面的分库分表策略验证SQL是否命中数据库表。

| 用户id | 数据库 | 数据表   |
| ------ | ------ | -------- |
| 6      | mall_0 | orders_2 |
| 7      | mall_1 | orders_3 |
| 8      | mall_0 | orders_0 |

准备应用到项目之前，通读 [《ShardingSphere 文档》](https://shardingsphere.apache.org/document/current/cn/overview/) 。学习不全面，线上两行泪。

代码地址：[GitHub](https://github.com/GoodBoy2333/sharding-jdbc-demo.git)

