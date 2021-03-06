package com.aifurion.seckill.controller;

import com.aifurion.seckill.common.limit.RedisLimit;
import com.aifurion.seckill.service.OrderService;
import com.aifurion.seckill.service.StockService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:14
 */

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/")
public class SecKillController {

    private static final String SUCCESS = "REQUEST SUCCESS";
    private static final String ERROR = "REQUEST ERROR";

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisLimit redisLimit;


    private RateLimiter rateLimiter = RateLimiter.create(10);


    /**
     * 秒杀基本过程,存在超卖问题
     *
     * @param sid
     * @return
     */
    @PostMapping("createNormalOrder")
    public String createNormalOrder(int sid) {
        int res = 0;
        try {
            res = orderService.createNormalOrder(sid);
        } catch (Exception e) {
            log.error("Exception:" + e);
        }

        return res == 1 ? SUCCESS : ERROR;
    }


    /**
     * 乐观锁实现
     *
     * @param sid
     * @return
     */
    @PostMapping("createOptimisticLockOrder")
    public String createOptimisticLockOrder(int sid) {

        int res = 0;

        try {
            res = orderService.createOptimisticLock(sid);
        } catch (Exception e) {
            log.error("Exception:" + e);
        }

        return res == 1 ? SUCCESS : ERROR;
    }

    /**
     * 乐观锁加redis限流
     *
     * @param sid
     * @return
     */
    @PostMapping("createOptimisticLockWithLimitOrder")
    public String createOptimisticLockWithLimitOrder(int sid) {

        int res = 0;
        try {
            if (redisLimit.limit()) {
                res = orderService.createOptimisticLock(sid);
            }
        } catch (Exception e) {
            log.error("Exception: " + e);
        }

        return res == 1 ? SUCCESS : ERROR;
    }


    /**
     * Guava令牌桶限流
     * @param sid
     * @return
     */

    @PostMapping("createGuavaLimitOrder")
    public String createGuavaLimitOrder(int sid) {
        int res = 0;

        try {
            if (rateLimiter.tryAcquire()) {
                res = orderService.createOptimisticLock(sid);
            }
        } catch (Exception e) {
            log.error("Exception: " + e);
        }

        return res == 1 ? SUCCESS : ERROR;

    }


    /**
     * redis限流+读redis+乐观锁
     *
     * @param sid
     * @return
     */
    @PostMapping("createOptimisticLockAndRedisOrder")
    public String createOptimisticLockAndRedisOrder(int sid) {

        int res = 0;
        try {
            if (redisLimit.limit()) {
            //if (rateLimiter.tryAcquire()){
                res = orderService.createOptimisticLockAndRedis(sid);
            }
        } catch (Exception e) {
            log.error("Exception:" + e);
        }

        return res == 1 ? SUCCESS : ERROR;
    }

    /**
     * 限流 + Redis 缓存库存 + Kafka 异步下单
     *
     * @param sid
     * @return
     */

    @PostMapping("createOrderWithLimitAndRedisAndKafka")
    public String createOrderWithLimitAndRedisAndKafka(int sid) {

        try {
            if (rateLimiter.tryAcquire()) {
                orderService.createOrderWithLimitAndRedisAndKafka(sid);
            }
        } catch (Exception e) {
            log.error("Exception:" + e);
        }

        return "请求正在处理，排队中";
    }

}
