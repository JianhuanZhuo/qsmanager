package cn.keepfight.qsmanager.dao.annual;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.sql.Date;

public class InvoiceDaoWrapper implements DaoWrapper<InvoiceDao>{

    private LongProperty id = new SimpleLongProperty();
    private LongProperty sup_id = new SimpleLongProperty();
    private LongProperty year = new SimpleLongProperty();
    private LongProperty month = new SimpleLongProperty();
    private StringProperty unit = new SimpleStringProperty();
    private ObjectProperty<Date> date = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> rate = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> rateTotal = new SimpleObjectProperty<>();

    public InvoiceDaoWrapper(InvoiceDao dao) {
        wrap(dao);
    }

    @Override
    public void wrap(InvoiceDao data) {
        FXReflectUtils.attrsCopy(data, this);
    }

    @Override
    public InvoiceDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new InvoiceDao());
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

    public long getSup_id() {
        return sup_id.get();
    }

    public LongProperty sup_idProperty() {
        return sup_id;
    }

    public void setSup_id(long sup_id) {
        this.sup_id.set(sup_id);
    }

    public long getYear() {
        return year.get();
    }

    public LongProperty yearProperty() {
        return year;
    }

    public void setYear(long year) {
        this.year.set(year);
    }

    public long getMonth() {
        return month.get();
    }

    public LongProperty monthProperty() {
        return month;
    }

    public void setMonth(long month) {
        this.month.set(month);
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

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
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

    public BigDecimal getRate() {
        return rate.get();
    }

    public ObjectProperty<BigDecimal> rateProperty() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate.set(rate);
    }

    public BigDecimal getRateTotal() {
        return rateTotal.get();
    }

    public ObjectProperty<BigDecimal> rateTotalProperty() {
        return rateTotal;
    }

    public void setRateTotal(BigDecimal rateTotal) {
        this.rateTotal.set(rateTotal);
    }
}
