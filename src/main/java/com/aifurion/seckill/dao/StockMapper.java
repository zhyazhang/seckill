package com.aifurion.seckill.dao;

import com.aifurion.seckill.pojo.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:20
 */

@Mapper
public interface StockMapper {


    /**
     * 通过id获得库存信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM sk_stock WHERE id=#{id, jdbcType=INTEGER}")
    Stock selectStockById(@Param("id") int id);


    /**
     * 更新库存
     *
     * @param stock
     * @return
     */
    @Update("UPDATE sk_stock SET count = #{count, jdbcType = INTEGER}, " +
            "name = #{name, jdbcType = VARCHAR}, " +
            "sale = #{sale,jdbcType = INTEGER}," +
            "version = #{version,jdbcType = INTEGER} " +
            "WHERE id = #{id, jdbcType = INTEGER}")
    int updateStockById(Stock stock);


    /**
     * 乐观锁
     * @param stock
     * @return
     */
    @Update("UPDATE sk_stock SET count = count - 1, sale = sale + 1, version = version + 1 " +
            "WHERE id = #{id, jdbcType = INTEGER} " +
            "AND version = #{version, jdbcType = INTEGER}")
    int updateByOptimistic(Stock stock);


}

