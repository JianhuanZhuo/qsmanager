package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.StuffDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 工资总额表
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
    private BigDecimal tardySalary;//只读量
    private List<StuffTardyDao> details;

    public SalaryDao(){}

    public SalaryDao(SalaryDao_i i, StuffDao stuffDao){
        setId(i.getId());
        setYear(i.getYear());
        setMonth(i.getMonth());
        setBasicSalary(i.getBasicSalary());
        setAgeSalary(i.getAgeSalary());
        setTotalSalary(i.getTotalSalary());

        // 内部只读量
        setTardySalary(i.getTardySalary());

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

    public BigDecimal getTardySalary() {
        return tardySalary;
    }

    public SalaryDao setTardySalary(BigDecimal tardySalary) {
        this.tardySalary = tardySalary;
        return this;
    }

    public List<StuffTardyDao> getDetails() {
        return details;
    }

    public SalaryDao setDetails(List<StuffTardyDao> details) {
        this.details = details;
        return this;
    }

    public BigDecimal getDetailByYM(String ym){
        for (StuffTardyDao d: details){
            if (d.getYm().equals(ym)){
                return d.getSum();
            }
        }
        throw new RuntimeException("wow????");
    }
}
