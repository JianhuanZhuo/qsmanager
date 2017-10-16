package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;

/**
 * 员工每月拖欠工资 Dao
 * Created by 卓建欢 on 2017/10/16.
 */
public class SalaryTardyDao {
    private Long id;
    private String name;
    private BigDecimal sum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
