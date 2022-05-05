package com.aifurion.seckill.pojo;

import java.util.Date;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:31
 */
public class Order {

    private Integer id;

    private Integer sid;

    private Date createTime;

    public Order() {
    }

    public Order(Integer id, Integer sid, Date createTime) {
        this.id = id;
        this.sid = sid;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", sid=" + sid +
                ", createTime=" + createTime +
                '}';
    }
}
