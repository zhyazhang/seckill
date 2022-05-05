package com.aifurion.seckill.service.impl;

import com.aifurion.seckill.dao.OrderMapper;
import com.aifurion.seckill.dao.StockMapper;
import com.aifurion.seckill.pojo.Order;
import com.aifurion.seckill.pojo.Stock;
import com.aifurion.seckill.service.OrderService;
import com.aifurion.seckill.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * 常规秒杀思路
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
     * 创建订单
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


}
