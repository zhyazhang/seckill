package com.aifurion.seckill.service.impl;

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
}
