package com.aifurion.seckill.pojo;

/**
 * @author ：zzy
 * @description：TODO
 * @date ：2022/5/5 9:31
 */
public class Stock {

    private Integer id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;

    public Stock() {
    }

    public Stock(Integer id, String name, Integer count, Integer sale, Integer version) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.sale = sale;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", sale=" + sale +
                ", version=" + version +
                '}';
    }
}
