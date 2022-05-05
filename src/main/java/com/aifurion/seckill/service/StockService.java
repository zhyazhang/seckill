package com.aifurion.seckill.service;

import com.aifurion.seckill.pojo.Stock;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:17
 */
public interface StockService {


    Stock checkStock(int sid);


    int saleStock(Stock stock);




}
