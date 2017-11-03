package cn.keepfight.qsmanager.dao.analysis;

import java.math.BigDecimal;

public class PriceAnalysisDao {

    private String serial;
    private String ym;
    private BigDecimal num;
    private BigDecimal price;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
