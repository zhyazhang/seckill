package com.aifurion.seckill.service;

import com.aifurion.seckill.pojo.Stock;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:17
 */
public interface StockService {


    /**
     * 检查库存
     * @param sid
     * @return
     */
    Stock checkStock(int sid);


    /**
     * 减库存
     * @param stock
     * @return
     */
    int saleStock(Stock stock);


    /**
     * 乐观锁实现
     * @param stock
     * @return
     */
    int updateStockByOptimisticLock(Stock stock);




}
