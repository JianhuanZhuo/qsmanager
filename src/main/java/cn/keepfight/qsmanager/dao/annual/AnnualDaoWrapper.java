package cn.keepfight.qsmanager.dao.annual;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AnnualDaoWrapper implements DaoWrapper<AnnualDao> {

    private LongProperty month = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> tradeTotal = new SimpleObjectProperty<>();
    private ObjectProperty<List<InvoiceDao>> invoice = new SimpleObjectProperty<>();
    private ObjectProperty<List<RemitDao>> remit = new SimpleObjectProperty<>();

    public AnnualDaoWrapper() {
    }

    public AnnualDaoWrapper(AnnualDao data) {
        this();
        wrap(data);
    }

    @Override
    public void wrap(AnnualDao data) {
        FXReflectUtils.attrsCopy(data, this);
//        setMonth(data.getMonth());
//        setTradeTotal(data.getTradeTotal());
//        setInvoice(data.getInvoice());
//        setRemit(data.getRemit());
    }

    @Override
    public AnnualDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new AnnualDao());
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

    public List<InvoiceDao> getInvoice() {
        return invoice.get();
    }

    public ObjectProperty<List<InvoiceDao>> invoiceProperty() {
        return invoice;
    }

    public void setInvoice(List<InvoiceDao> invoice) {
        this.invoice.set(invoice);
    }

    public List<RemitDao> getRemit() {
        return remit.get();
    }

    public ObjectProperty<List<RemitDao>> remitProperty() {
        return remit;
    }

    public void setRemit(List<RemitDao> remit) {
        this.remit.set(remit);
    }

    public BigDecimal getInvoicesTotal() {
        List<InvoiceDao> ls = invoice.get();
        if (ls!=null){
            Optional<BigDecimal> res = ls.stream()
                    .map(InvoiceDao::getRate)
                    .reduce(BigDecimal::add);
            if(res.isPresent()){
                return res.get();
            }
        }
        return new BigDecimal(0);
    }

    public BigDecimal getInvoicesRateTotal() {
        List<InvoiceDao> ls = invoice.get();
        if (ls!=null){
            Optional<BigDecimal> res = ls.stream()
                    .map(InvoiceDao::getRateTotal)
                    .reduce(BigDecimal::add);
            if(res.isPresent()){
                return res.get();
            }
        }
        return new BigDecimal(0);
    }

    public BigDecimal getRemitTotal() {
        List<RemitDao> ls = remit.get();
        if (ls!=null){
            Optional<BigDecimal> res = ls.stream()
                    .map(RemitDao::getTotal)
                    .reduce(BigDecimal::add);
            if(res.isPresent()){
                return res.get();
            }
        }
        return new BigDecimal(0);
    }
}
