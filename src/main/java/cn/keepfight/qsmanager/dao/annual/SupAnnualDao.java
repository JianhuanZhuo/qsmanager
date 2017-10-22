package cn.keepfight.qsmanager.dao.annual;

import java.math.BigDecimal;
import java.util.List;

public class SupAnnualDao {
    private Long month;
    private BigDecimal tradeTotal;
    private List<SupInvoiceDao> invoice;
    private List<SupRemitDao> remit;

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

    public List<SupInvoiceDao> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<SupInvoiceDao> invoice) {
        this.invoice = invoice;
    }

    public List<SupRemitDao> getRemit() {
        return remit;
    }

    public void setRemit(List<SupRemitDao> remit) {
        this.remit = remit;
    }
}
