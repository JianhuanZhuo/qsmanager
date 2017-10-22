package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;

/**
 * 员工每月拖欠工资 Dao
 * Created by 卓建欢 on 2017/10/16.
 */
public class StuffTardyDao {
    private String ym;
    private BigDecimal sum;
    private Long salary_id;// 对应月的工资记录ID

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public Long getSalary_id() {
        return salary_id;
    }

    public void setSalary_id(Long salary_id) {
        this.salary_id = salary_id;
    }
}
