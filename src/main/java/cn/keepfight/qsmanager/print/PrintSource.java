package cn.keepfight.qsmanager.print;

/**
 * 打印源
 * Created by tom on 2017/6/27.
 */
public class PrintSource {
    private Long cust; // 客户ID
    private Long sup;  // 供应商ID
    private Long year; // 年份
    private Long month;// 月份
    private Long item; // 订单ID

    public Long getCust() {
        return cust;
    }

    public void setCust(Long cust) {
        this.cust = cust;
    }

    public Long getSup() {
        return sup;
    }

    public void setSup(Long sup) {
        this.sup = sup;
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

    public Long getItem() {
        return item;
    }

    public void setItem(Long item) {
        this.item = item;
    }
}
