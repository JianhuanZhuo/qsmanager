package cn.keepfight.qsmanager.dao.predict;

import java.math.BigDecimal;

/**
 * 历史列表 Dao
 * Created by 卓建欢 on 2017/10/31.
 */
public class PredictHistoryDao {
    private Long year;
    private Long month;
    private BigDecimal out_pri;
    private BigDecimal out_pub;
    private BigDecimal income;
    private BigDecimal outcome;
    private BigDecimal outcome_sup;
    private BigDecimal out_tax;
    private BigDecimal out_fix;
    private BigDecimal out_salary;
    private BigDecimal out_salary_lef;
    private BigDecimal out_factory;
    private BigDecimal out_fee;
    private BigDecimal out_water;
    private BigDecimal out_elect;
    private BigDecimal out_eng;
    private BigDecimal out_other;

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

    public BigDecimal getOut_pri() {
        return out_pri;
    }

    public void setOut_pri(BigDecimal out_pri) {
        this.out_pri = out_pri;
    }

    public BigDecimal getOut_pub() {
        return out_pub;
    }

    public void setOut_pub(BigDecimal out_pub) {
        this.out_pub = out_pub;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getOutcome() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome = outcome;
    }

    public BigDecimal getOut_tax() {
        return out_tax;
    }

    public void setOut_tax(BigDecimal out_tax) {
        this.out_tax = out_tax;
    }

    public BigDecimal getOut_fix() {
        return out_fix;
    }

    public void setOut_fix(BigDecimal out_fix) {
        this.out_fix = out_fix;
    }

    public BigDecimal getOut_salary() {
        return out_salary;
    }

    public void setOut_salary(BigDecimal out_salary) {
        this.out_salary = out_salary;
    }

    public BigDecimal getOut_salary_lef() {
        return out_salary_lef;
    }

    public void setOut_salary_lef(BigDecimal out_salary_lef) {
        this.out_salary_lef = out_salary_lef;
    }

    public BigDecimal getOut_factory() {
        return out_factory;
    }

    public void setOut_factory(BigDecimal out_factory) {
        this.out_factory = out_factory;
    }

    public BigDecimal getOut_fee() {
        return out_fee;
    }

    public void setOut_fee(BigDecimal out_fee) {
        this.out_fee = out_fee;
    }

    public BigDecimal getOut_water() {
        return out_water;
    }

    public void setOut_water(BigDecimal out_water) {
        this.out_water = out_water;
    }

    public BigDecimal getOut_elect() {
        return out_elect;
    }

    public void setOut_elect(BigDecimal out_elect) {
        this.out_elect = out_elect;
    }

    public BigDecimal getOut_eng() {
        return out_eng;
    }

    public void setOut_eng(BigDecimal out_eng) {
        this.out_eng = out_eng;
    }

    public BigDecimal getOut_other() {
        return out_other;
    }

    public void setOut_other(BigDecimal out_other) {
        this.out_other = out_other;
    }

    public BigDecimal getOutcome_sup() {
        return outcome_sup;
    }

    public void setOutcome_sup(BigDecimal outcome_sup) {
        this.outcome_sup = outcome_sup;
    }
}
