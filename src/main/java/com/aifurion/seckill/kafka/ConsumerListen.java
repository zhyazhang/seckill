package com.aifurion.seckill.kafka;

import com.aifurion.seckill.pojo.Stock;
import com.aifurion.seckill.service.OrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 15:58
 */
@Slf4j
@Component
public class ConsumerListen {

    private Gson gson = new GsonBuilder().create();

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "seckill_topic")
    public void listen(ConsumerRecord<String, String> record) {

        try {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            // Object -> String
            String message = (String) kafkaMessage.get();
            // 反序列化
            Stock stock = gson.fromJson((String) message, Stock.class);
            // 创建订单
            orderService.consumerTopicToCreateOrderWithKafka(stock);

        } catch (Exception e) {
            log.error("Exception:" + e);
        }
    }
}
