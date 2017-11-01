package cn.keepfight.qsmanager.dao.predict;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * 历史列表 Dao 包装器
 * Created by 卓建欢 on 2017/10/31.
 */
public class PredictHistoryDaoWrapper implements DaoWrapper<PredictHistoryDao>{
    private LongProperty year = new SimpleLongProperty();
    private LongProperty month = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> out_pri = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_pub = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> income = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> outcome = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> outcome_sup = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_tax = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_fix = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_salary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_salary_lef = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_factory = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_fee = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_water = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_elect = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_eng = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> out_other = new SimpleObjectProperty<>();

    public PredictHistoryDaoWrapper(PredictHistoryDao dao){
        wrap(dao);
    }

    @Override
    public void wrap(PredictHistoryDao data) {
        FXReflectUtils.attrsCopy(data, this);
    }

    @Override
    public PredictHistoryDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new PredictHistoryDao());
    }

    public long getYear() {
        return year.get();
    }

    public LongProperty yearProperty() {
        return year;
    }

    public void setYear(long year) {
        this.year.set(year);
    }

    public long getMonth() {
        return month.get();
    }

    public LongProperty monthProperty() {
        return month;
    }

    public void setMonth(long month) {
        this.month.set(month);
    }

    public BigDecimal getOut_pri() {
        return out_pri.get();
    }

    public ObjectProperty<BigDecimal> out_priProperty() {
        return out_pri;
    }

    public void setOut_pri(BigDecimal out_pri) {
        this.out_pri.set(out_pri);
    }

    public BigDecimal getOut_pub() {
        return out_pub.get();
    }

    public ObjectProperty<BigDecimal> out_pubProperty() {
        return out_pub;
    }

    public void setOut_pub(BigDecimal out_pub) {
        this.out_pub.set(out_pub);
    }

    public BigDecimal getIncome() {
        return income.get();
    }

    public ObjectProperty<BigDecimal> incomeProperty() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income.set(income);
    }

    public BigDecimal getOutcome() {
        return outcome.get();
    }

    public ObjectProperty<BigDecimal> outcomeProperty() {
        return outcome;
    }

    public void setOutcome(BigDecimal outcome) {
        this.outcome.set(outcome);
    }

    public BigDecimal getOut_tax() {
        return out_tax.get();
    }

    public ObjectProperty<BigDecimal> out_taxProperty() {
        return out_tax;
    }

    public void setOut_tax(BigDecimal out_tax) {
        this.out_tax.set(out_tax);
    }

    public BigDecimal getOut_fix() {
        return out_fix.get();
    }

    public ObjectProperty<BigDecimal> out_fixProperty() {
        return out_fix;
    }

    public void setOut_fix(BigDecimal out_fix) {
        this.out_fix.set(out_fix);
    }

    public BigDecimal getOut_salary() {
        return out_salary.get();
    }

    public ObjectProperty<BigDecimal> out_salaryProperty() {
        return out_salary;
    }

    public void setOut_salary(BigDecimal out_salary) {
        this.out_salary.set(out_salary);
    }

    public BigDecimal getOut_salary_lef() {
        return out_salary_lef.get();
    }

    public ObjectProperty<BigDecimal> out_salary_lefProperty() {
        return out_salary_lef;
    }

    public void setOut_salary_lef(BigDecimal out_salary_lef) {
        this.out_salary_lef.set(out_salary_lef);
    }

    public BigDecimal getOut_factory() {
        return out_factory.get();
    }

    public ObjectProperty<BigDecimal> out_factoryProperty() {
        return out_factory;
    }

    public void setOut_factory(BigDecimal out_factory) {
        this.out_factory.set(out_factory);
    }

    public BigDecimal getOut_fee() {
        return out_fee.get();
    }

    public ObjectProperty<BigDecimal> out_feeProperty() {
        return out_fee;
    }

    public void setOut_fee(BigDecimal out_fee) {
        this.out_fee.set(out_fee);
    }

    public BigDecimal getOut_water() {
        return out_water.get();
    }

    public ObjectProperty<BigDecimal> out_waterProperty() {
        return out_water;
    }

    public void setOut_water(BigDecimal out_water) {
        this.out_water.set(out_water);
    }

    public BigDecimal getOut_elect() {
        return out_elect.get();
    }

    public ObjectProperty<BigDecimal> out_electProperty() {
        return out_elect;
    }

    public void setOut_elect(BigDecimal out_elect) {
        this.out_elect.set(out_elect);
    }

    public BigDecimal getOut_eng() {
        return out_eng.get();
    }

    public ObjectProperty<BigDecimal> out_engProperty() {
        return out_eng;
    }

    public void setOut_eng(BigDecimal out_eng) {
        this.out_eng.set(out_eng);
    }

    public BigDecimal getOut_other() {
        return out_other.get();
    }

    public ObjectProperty<BigDecimal> out_otherProperty() {
        return out_other;
    }

    public void setOut_other(BigDecimal out_other) {
        this.out_other.set(out_other);
    }

    public BigDecimal getOutcome_sup() {
        return outcome_sup.get();
    }

    public ObjectProperty<BigDecimal> outcome_supProperty() {
        return outcome_sup;
    }

    public void setOutcome_sup(BigDecimal outcome_sup) {
        this.outcome_sup.set(outcome_sup);
    }
}
