package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;

/**
 * 年份统计Dao
 *
 * Created by tom on 2017/10/15.
 */
public class YearStaticDao {
    private String month;
    private BigDecimal total;
    private BigDecimal given;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public YearStaticDao setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public BigDecimal getGiven() {
        return given;
    }

    public YearStaticDao setGiven(BigDecimal given) {
        this.given = given;
        return this;
    }
}
