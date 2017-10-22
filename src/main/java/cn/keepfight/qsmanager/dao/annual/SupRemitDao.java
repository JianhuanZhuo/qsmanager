package cn.keepfight.qsmanager.dao.annual;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 供应商发票存储对象
 */
public class SupRemitDao {
    private Long id;
    private Long sup_id;
    private Long year;
    private Long month;
    private String unit;
    private Date date;
    private String mode;
    private String note;
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSup_id() {
        return sup_id;
    }

    public void setSup_id(Long sup_id) {
        this.sup_id = sup_id;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
