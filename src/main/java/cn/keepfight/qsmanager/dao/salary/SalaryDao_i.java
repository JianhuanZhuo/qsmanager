package cn.keepfight.qsmanager.dao.salary;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 内部使用的 Dao
 * Created by tom on 2017/10/15.
 */
public class SalaryDao_i {
    private long id;
    private long year;
    private long month;
    private long stuffDaoID;
    private BigDecimal basicSalary;
    private BigDecimal ageSalary;
    private BigDecimal totalSalary;
    private BigDecimal tardySalary;//只读量

    public long getId() {
        return id;
    }

    public SalaryDao_i setId(long id) {
        this.id = id;
        return this;
    }

    public long getYear() {
        return year;
    }

    public SalaryDao_i setYear(long year) {
        this.year = year;
        return this;
    }

    public long getMonth() {
        return month;
    }

    public SalaryDao_i setMonth(long month) {
        this.month = month;
        return this;
    }

    public long getStuffDaoID() {
        return stuffDaoID;
    }

    public SalaryDao_i setStuffDaoID(long stuffDaoID) {
        this.stuffDaoID = stuffDaoID;
        return this;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public SalaryDao_i setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
        return this;
    }

    public BigDecimal getAgeSalary() {
        return ageSalary;
    }

    public SalaryDao_i setAgeSalary(BigDecimal ageSalary) {
        this.ageSalary = ageSalary;
        return this;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public SalaryDao_i setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
        return this;
    }

    public BigDecimal getTardySalary() {
        return tardySalary;
    }

    public SalaryDao_i setTardySalary(BigDecimal tardySalary) {
        this.tardySalary = tardySalary;
        return this;
    }
}
