package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.StuffDao;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by tom on 2017/10/15.
 */
public class SalaryDao {
    private long id;
    private long year;
    private long month;
    private StuffDao stuffDao;
    private BigDecimal basicSalary;
    private BigDecimal ageSalary;
    private BigDecimal totalSalary;
    private BigDecimal fixSalary;
    private int clear;
    private Date date;

    public SalaryDao(){}

    public SalaryDao(SalaryDao_i i, StuffDao stuffDao){
        setId(i.getId());
        setYear(i.getYear());
        setMonth(i.getMonth());
        setBasicSalary(i.getBasicSalary());
        setAgeSalary(i.getAgeSalary());
        setTotalSalary(i.getTotalSalary());
        setDate(i.getDate());
        setClear(i.getClear());
        setFixSalary(i.getFixSalary());

        setStuffDao(stuffDao);
    }

    public long getId() {
        return id;
    }

    public SalaryDao setId(long id) {
        this.id = id;
        return this;
    }

    public StuffDao getStuffDao() {
        return stuffDao;
    }

    public SalaryDao setStuffDao(StuffDao stuffDao) {
        this.stuffDao = stuffDao;
        return this;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public SalaryDao setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
        return this;
    }

    public BigDecimal getAgeSalary() {
        return ageSalary;
    }

    public SalaryDao setAgeSalary(BigDecimal ageSalary) {
        this.ageSalary = ageSalary;
        return this;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public SalaryDao setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public SalaryDao setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getClear() {
        return clear;
    }

    public SalaryDao setClear(int clear) {
        this.clear = clear;
        return this;
    }

    public long getYear() {
        return year;
    }

    public SalaryDao setYear(long year) {
        this.year = year;
        return this;
    }

    public long getMonth() {
        return month;
    }

    public SalaryDao setMonth(long month) {
        this.month = month;
        return this;
    }

    public BigDecimal getFixSalary() {
        return fixSalary;
    }

    public void setFixSalary(BigDecimal fixSalary) {
        this.fixSalary = fixSalary;
    }
}
