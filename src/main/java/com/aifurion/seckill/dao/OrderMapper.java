package com.aifurion.seckill.dao;

import com.aifurion.seckill.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:19
 */
@Mapper
public interface OrderMapper {


    /**
     * 插入订单数据
     * @param order
     * @return
     */
    @Insert("INSERT INTO sk_order (id, sid, create_time) VALUES " +
            "(#{id, jdbcType = INTEGER}, #{sid, jdbcType = INTEGER}, " +
            "#{createTime, jdbcType = TIMESTAMP})")
    int insertOrder(Order order);


}
