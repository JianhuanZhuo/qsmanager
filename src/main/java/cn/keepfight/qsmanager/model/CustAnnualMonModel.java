package cn.keepfight.qsmanager.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 供应商年度对账月明细表模型
 * Created by tom on 2017/6/6.
 */
public class CustAnnualMonModel {
    private Long id;
    private Long said;
    private int mon;
    private BigDecimal total;
    private String billunit;
    private Timestamp billdate;
    private BigDecimal billtotal;
    private BigDecimal rate;
    private String remitunit;
    private String pattern;
    private Timestamp remitdate;
    private BigDecimal paytotal;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSaid() {
        return said;
    }

    public void setSaid(Long said) {
        this.said = said;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getBillunit() {
        return billunit;
    }

    public void setBillunit(String billunit) {
        this.billunit = billunit;
    }

    public Timestamp getBilldate() {
        return billdate;
    }

    public void setBilldate(Timestamp billdate) {
        this.billdate = billdate;
    }

    public BigDecimal getBilltotal() {
        return billtotal;
    }

    public void setBilltotal(BigDecimal billtotal) {
        this.billtotal = billtotal;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRemitunit() {
        return remitunit;
    }

    public void setRemitunit(String remitunit) {
        this.remitunit = remitunit;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Timestamp getRemitdate() {
        return remitdate;
    }

    public void setRemitdate(Timestamp remitdate) {
        this.remitdate = remitdate;
    }

    public BigDecimal getPaytotal() {
        return paytotal;
    }

    public void setPaytotal(BigDecimal paytotal) {
        this.paytotal = paytotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
