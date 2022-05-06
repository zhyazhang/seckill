package com.aifurion.seckill.service.impl;

import com.aifurion.seckill.common.stockWithRedis.RedisKeysConstant;
import com.aifurion.seckill.common.stockWithRedis.StockWithRedis;
import com.aifurion.seckill.common.utils.RedisPoolUtil;
import com.aifurion.seckill.dao.StockMapper;
import com.aifurion.seckill.pojo.Stock;
import com.aifurion.seckill.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:18
 */

@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockMapper stockMapper;


    /**
     * 检查库存
     *
     * @param sid
     * @return
     */
    @Override
    public Stock checkStock(int sid) {

        Stock stock = stockMapper.selectStockById(sid);

        if (stock.getCount() < 1) {
            throw new RuntimeException("库存不足");
        }

        return stock;
    }

    /**
     * 减库存
     *
     * @param stock
     * @return
     */
    @Override
    public int saleStock(Stock stock) {

        stock.setCount(stock.getCount() - 1);
        stock.setSale(stock.getSale() + 1);

        return stockMapper.updateStockById(stock);
    }


    @Override
    public int updateStockByOptimisticLock(Stock stock) {

        return stockMapper.updateByOptimistic(stock);
    }


    @Override
    public Stock getStockById(int id) {
        return stockMapper.selectStockById(id);
    }


    /**
     * 从redis读数据，校验库存
     *
     * @param id
     * @return
     */
    @Override
    public Stock checkStockWithRedis(int id) {

        int count = Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_COUNT + id));
        int sale = Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_SALE + id));
        int version =
                Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_VERSION + id));

        if (count < 1) {
            log.info("库存不足");
            throw new RuntimeException("库存不足 Redis currentCount: " + count);
        }

        // 热更新，但是在数据库中只有一个商品，直接赋值
        Stock stock = new Stock(id, count, sale, version);
        /*stock.setId(id);
        stock.setCount(count);
        stock.setSale(sale);
        stock.setVersion(version);*/

        return stock;
    }


    @Override
    public void updateStockOptimisticLockWithRedis(Stock stock) {

        //更新数据库
        int res = updateStockByOptimisticLock(stock);
        if (res == 0) {
            throw new RuntimeException("并发更新库存失败");
        }

        //更新redis
        StockWithRedis.updateRedisStock(stock);
    }
}
