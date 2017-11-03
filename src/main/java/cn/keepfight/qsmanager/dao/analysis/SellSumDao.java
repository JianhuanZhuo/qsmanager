package cn.keepfight.qsmanager.dao.analysis;

import java.math.BigDecimal;

public class SellSumDao {
    private String ym;
    private BigDecimal sum;

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
