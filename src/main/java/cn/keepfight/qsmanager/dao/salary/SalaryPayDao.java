package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 工资发放dao，对应 stuff_income
 * Created by tom on 2017/10/19.
 */
public class SalaryPayDao {
    private Long salary_id;
    private BigDecimal income;
    private Date date;

    public Long getSalary_id() {
        return salary_id;
    }

    public SalaryPayDao setSalary_id(Long salary_id) {
        this.salary_id = salary_id;
        return this;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public SalaryPayDao setIncome(BigDecimal income) {
        this.income = income;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public SalaryPayDao setDate(Date date) {
        this.date = date;
        return this;
    }
}
