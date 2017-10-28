package cn.keepfight.qsmanager.dao.tax;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.*;

import java.math.BigDecimal;

/**
 * 发票抵扣数据包装器
 * Created by 卓建欢 on 2017/10/26.
 */
public class TaxInvoiceDaoWrapper implements DaoWrapper<TaxInvoiceDao>{
    // 类别变量，其中 0 为销项，1 为进项
    private LongProperty id = new SimpleLongProperty();
    private LongProperty tid = new SimpleLongProperty();
    private LongProperty category = new SimpleLongProperty();
    private StringProperty unit = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private StringProperty Note = new SimpleStringProperty();

    public TaxInvoiceDaoWrapper(TaxInvoiceDao data){
        wrap(data);
    }

    @Override
    public void wrap(TaxInvoiceDao data) {
        FXReflectUtils.attrsCopy(data, this);
    }

    @Override
    public TaxInvoiceDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new TaxInvoiceDao());
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public long getTid() {
        return tid.get();
    }

    public LongProperty tidProperty() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid.set(tid);
    }

    public long getCategory() {
        return category.get();
    }

    public LongProperty categoryProperty() {
        return category;
    }

    public void setCategory(long category) {
        this.category.set(category);
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }

    public String getNote() {
        return Note.get();
    }

    public StringProperty noteProperty() {
        return Note;
    }

    public void setNote(String note) {
        this.Note.set(note);
    }
}
