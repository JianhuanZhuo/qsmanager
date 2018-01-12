package cn.keepfight.qsmanager.dao.analysis;

import java.math.BigDecimal;

public class SellAnalysisDao {

    private Long cid;
    private String cname;
    private BigDecimal sum;
    private String item_serial;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getItem_serial() {
        return item_serial;
    }

    public void setItem_serial(String item_serial) {
        this.item_serial = item_serial;
    }
}
