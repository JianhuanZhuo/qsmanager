package cn.keepfight.qsmanager.dao.analysis;


import java.math.BigDecimal;

public class MaterialAnalysisDao_i {
    private String month;
    private String name;
    private BigDecimal sum;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
