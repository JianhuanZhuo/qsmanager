package cn.keepfight.qsmanager.dao.analysis;

import java.math.BigDecimal;

public class UnitMonthDao_i {
    private Long uid;
    private String uname;
    private Integer month;
    private BigDecimal sum;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
