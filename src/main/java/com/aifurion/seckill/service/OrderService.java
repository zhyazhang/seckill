package com.aifurion.seckill.service;

import com.aifurion.seckill.pojo.Stock;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:17
 */
public interface OrderService {


    /**
     * 秒杀基本思路 存在超卖问题
     * @param sid
     * @return
     */
    int createNormalOrder(int sid);


    /**
     * 乐观锁
     * @param sid
     * @return
     */
    int createOptimisticLock(int sid);


    /**
     * 乐观锁和读redis
     * @param sid
     * @return
     */
    int createOptimisticLockAndRedis(int sid);


    /**
     * 限流 + Redis 缓存库存 + Kafka 异步下单
     * @param sid
     */
    void createOrderWithLimitAndRedisAndKafka(int sid);


    /**
     * 消费kafka消息
     * @param stock
     */
    void consumerTopicToCreateOrderWithKafka(Stock stock);

}
