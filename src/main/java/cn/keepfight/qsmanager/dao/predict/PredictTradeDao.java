package cn.keepfight.qsmanager.dao.predict;

import java.math.BigDecimal;

/**
 * 交易 Dao
 * Created by 卓建欢 on 2017/10/29.
 */
public class PredictTradeDao {
    private Long uid;
    private String name;
    private Long year;
    private Long month;
    private BigDecimal leftsum;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long sid) {
        this.uid = sid;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLeftsum() {
        return leftsum;
    }

    public void setLeftsum(BigDecimal leftsum) {
        this.leftsum = leftsum;
    }
}
