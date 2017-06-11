package cn.keepfight.qsmanager.model;

import java.util.Formatter;

/**
 * 送货供应表选择器模型
 * Created by tom on 2017/6/10.
 */
public class ReceiptSelection {
    private Long sid; // 供应商ID
    private String year;
    private String month;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getYear() {
        return Long.valueOf(year);
    }

    public void setYear(Long year) {
        this.year = year.toString();
    }

    public Long getMonth() {
        return Long.valueOf(month);
    }

    public void setMonth(Long month) {
        if (month < 10) {
            this.month = "0" + month;
        } else {
            this.month = month.toString();
        }
    }
}
