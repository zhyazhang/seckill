package com.aifurion.seckill.service.impl;

import com.aifurion.seckill.dao.OrderMapper;
import com.aifurion.seckill.dao.StockMapper;
import com.aifurion.seckill.pojo.Order;
import com.aifurion.seckill.pojo.Stock;
import com.aifurion.seckill.service.OrderService;
import com.aifurion.seckill.service.StockService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:18
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    private Gson gson = new GsonBuilder().create();


    /**
     * 常规秒杀思路
     *
     * @param sid
     * @return
     */
    @Override
    public int createNormalOrder(int sid) {

        Stock stock = stockService.checkStock(sid);

        stockService.saleStock(stock);

        return createOrder(stock);

    }

    /**
     * 乐观锁
     *
     * @param sid
     * @return
     */
    @Override
    public int createOptimisticLock(int sid) {

        Stock stock = stockService.checkStock(sid);

        int count = stockService.updateStockByOptimisticLock(stock);

        if (count == 0) {
            throw new RuntimeException("并发更新库存失败");
        }
        return createOrder(stock);
    }

    /**
     * 创建订单
     *
     * @param stock
     * @return
     */
    private int createOrder(Stock stock) {

        Order order = new Order();

        order.setSid(stock.getId());
        order.setCreateTime(new Date());

        int result = orderMapper.insertOrder(order);

        if (result == 0) {
            throw new RuntimeException("创建订单失败");
        }
        return result;
    }


    /**
     * 乐观锁+读redis
     * @param sid
     * @return
     */
    @Override
    public int createOptimisticLockAndRedis(int sid) {

        //从redis读，校验库存
        Stock stock = stockService.checkStockWithRedis(sid);

        //更新数据块和redis
        stockService.updateStockOptimisticLockWithRedis(stock);

        //创建订单
        return createOrder(stock);
    }


    /**
     * 将消息发送到kafka
     * @param sid
     */
    @Override
    public void createOrderWithLimitAndRedisAndKafka(int sid) {

        // redis校验库存
        Stock stock = stockService.checkStockWithRedis(sid);

        // 下单请求发送至 kafka，需要序列化 stock
        kafkaTemplate.send(kafkaTopic, gson.toJson(stock));
        log.info("消息发送至 Kafka 成功");
    }

    /**
     * 消费kafka消息
     * @param stock
     */
    @Override
    public void consumerTopicToCreateOrderWithKafka(Stock stock) {

        // 乐观锁更新库存和 Redis
        stockService.updateStockOptimisticLockWithRedis(stock);

        int res = createOrder(stock);
        if (res == 1) {
            log.info("Kafka 消费 Topic 创建订单成功");
        } else {
            log.info("Kafka 消费 Topic 创建订单失败");
        }
    }
}
