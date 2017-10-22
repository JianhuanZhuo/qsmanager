package cn.keepfight.qsmanager.dao.annual;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.util.List;

public class SupAnnualDaoWrapper implements DaoWrapper<SupAnnualDao>{

    private LongProperty month = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> tradeTotal = new SimpleObjectProperty<>();
    private ObjectProperty<List<SupInvoiceDao>> invoice = new SimpleObjectProperty<>();
    private ObjectProperty<List<SupRemitDao>> remit = new SimpleObjectProperty<>();

    public SupAnnualDaoWrapper() {}
    public SupAnnualDaoWrapper(SupAnnualDao data){
        this();
        wrap(data);
    }

    @Override
    public void wrap(SupAnnualDao data) {
        FXReflectUtils.attrsCopy(data, this);
//        setMonth(data.getMonth());
//        setTradeTotal(data.getTradeTotal());
//        setInvoice(data.getInvoice());
//        setRemit(data.getRemit());
    }

    @Override
    public SupAnnualDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new SupAnnualDao());
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

    public BigDecimal getTradeTotal() {
        return tradeTotal.get();
    }

    public ObjectProperty<BigDecimal> tradeTotalProperty() {
        return tradeTotal;
    }

    public void setTradeTotal(BigDecimal tradeTotal) {
        this.tradeTotal.set(tradeTotal);
    }

    public List<SupInvoiceDao> getInvoice() {
        return invoice.get();
    }

    public ObjectProperty<List<SupInvoiceDao>> invoiceProperty() {
        return invoice;
    }

    public void setInvoice(List<SupInvoiceDao> invoice) {
        this.invoice.set(invoice);
    }

    public List<SupRemitDao> getRemit() {
        return remit.get();
    }

    public ObjectProperty<List<SupRemitDao>> remitProperty() {
        return remit;
    }

    public void setRemit(List<SupRemitDao> remit) {
        this.remit.set(remit);
    }
}
