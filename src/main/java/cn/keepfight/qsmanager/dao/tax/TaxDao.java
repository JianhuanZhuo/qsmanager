package cn.keepfight.qsmanager.dao.tax;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发票抵扣表 Dao
 * Created by 卓建欢 on 2017/10/26.
 */
public class TaxDao {
    private Long id;
    private Long year;
    private Long month;
    private BigDecimal p1;
    private BigDecimal p2;
    private BigDecimal p3;
    private BigDecimal p4;
    private BigDecimal p5;
    private BigDecimal p6;
    private BigDecimal total;
    private List<TaxInvoiceDao> invoices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getP1() {
        return p1;
    }

    public void setP1(BigDecimal p1) {
        this.p1 = p1;
    }

    public BigDecimal getP2() {
        return p2;
    }

    public void setP2(BigDecimal p2) {
        this.p2 = p2;
    }

    public BigDecimal getP3() {
        return p3;
    }

    public void setP3(BigDecimal p3) {
        this.p3 = p3;
    }

    public BigDecimal getP4() {
        return p4;
    }

    public void setP4(BigDecimal p4) {
        this.p4 = p4;
    }

    public BigDecimal getP5() {
        return p5;
    }

    public void setP5(BigDecimal p5) {
        this.p5 = p5;
    }

    public BigDecimal getP6() {
        return p6;
    }

    public void setP6(BigDecimal p6) {
        this.p6 = p6;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<TaxInvoiceDao> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<TaxInvoiceDao> invoices) {
        this.invoices = invoices;
    }
}
