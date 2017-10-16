package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;

/**
 * 年份统计Dao
 *
 * Created by tom on 2017/10/15.
 */
public class YearStaticDao {
    private int month;
    private BigDecimal total;
    private BigDecimal given;

    public int getMonth() {
        return month;
    }

    public YearStaticDao setMonth(int month) {
        this.month = month;
        return this;
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
