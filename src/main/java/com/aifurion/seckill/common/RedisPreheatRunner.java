package com.aifurion.seckill.common;

import com.aifurion.seckill.common.stockWithRedis.RedisKeysConstant;
import com.aifurion.seckill.common.utils.RedisPoolUtil;
import com.aifurion.seckill.pojo.Stock;
import com.aifurion.seckill.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 13:30
 */

@Component
public class RedisPreheatRunner implements ApplicationRunner {

    @Autowired
    private StockService stockService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 从数据库中查询热卖商品存入redis
        Stock stock = stockService.getStockById(1);

        // 删除旧缓存
        RedisPoolUtil.del(RedisKeysConstant.STOCK_COUNT + stock.getCount());
        RedisPoolUtil.del(RedisKeysConstant.STOCK_SALE + stock.getSale());
        RedisPoolUtil.del(RedisKeysConstant.STOCK_VERSION + stock.getVersion());

        //缓存预热
        int sid = stock.getId();
        RedisPoolUtil.set(RedisKeysConstant.STOCK_COUNT + sid, String.valueOf(stock.getCount()));
        RedisPoolUtil.set(RedisKeysConstant.STOCK_SALE + sid, String.valueOf(stock.getSale()));
        RedisPoolUtil.set(RedisKeysConstant.STOCK_VERSION + sid,
                String.valueOf(stock.getVersion()));
    }

}
