package cn.keepfight.qsmanager.dao.predict;

import javafx.beans.binding.ObjectBinding;

import java.math.BigDecimal;

/**
 *
 * Created by 卓建欢 on 2017/10/29.
 */
public class PredictTradeItemDao {
    private Long year;
    private Long month;
    private BigDecimal total;

    public PredictTradeItemDao(Long year, Long month, BigDecimal total) {
        this.year = year;
        this.month = month;
        this.total = total;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
