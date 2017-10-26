package cn.keepfight.qsmanager.dao.annual;

import java.math.BigDecimal;
import java.util.List;

public class AnnualDao {
    private Long month;
    private BigDecimal tradeTotal;
    private List<InvoiceDao> invoice;
    private List<RemitDao> remit;

    public Long getMonth() {
        return month;
    }

    public void setMonth(Long month) {
        this.month = month;
    }

    public BigDecimal getTradeTotal() {
        return tradeTotal;
    }

    public void setTradeTotal(BigDecimal tradeTotal) {
        this.tradeTotal = tradeTotal;
    }

    public List<InvoiceDao> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<InvoiceDao> invoice) {
        this.invoice = invoice;
    }

    public List<RemitDao> getRemit() {
        return remit;
    }

    public void setRemit(List<RemitDao> remit) {
        this.remit = remit;
    }
}
