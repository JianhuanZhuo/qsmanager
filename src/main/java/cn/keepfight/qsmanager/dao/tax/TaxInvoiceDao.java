package cn.keepfight.qsmanager.dao.tax;

import java.math.BigDecimal;

/**
 * 发票抵扣列表
 * Created by 卓建欢 on 2017/10/26.
 */
public class TaxInvoiceDao {
    private Long id;
    private Long tid;

    // 类别变量，其中 0 为销项，1 为进项
    private Long category;

    private String unit;
    private BigDecimal total;
    private String Note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
