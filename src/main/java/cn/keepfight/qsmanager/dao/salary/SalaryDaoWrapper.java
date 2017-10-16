package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.qsmanager.dao.StuffWrapper;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 工资包装器
 * Created by tom on 2017/10/15.
 */
public class SalaryDaoWrapper implements DaoWrapper<SalaryDao> {

    private LongProperty id = new SimpleLongProperty();
    private LongProperty year = new SimpleLongProperty();
    private LongProperty month = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> basicSalary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> ageSalary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> totalSalary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> fixSalary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> otherSalary = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> willSalary = new SimpleObjectProperty<>();
    private BooleanProperty clear = new SimpleBooleanProperty(false);
    private ObjectProperty<Date> date = new SimpleObjectProperty<>();
    private StuffWrapper stuffDaoWrapper = new StuffWrapper();

    public SalaryDaoWrapper(){
        otherSalary.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(totalSalary, basicSalary, ageSalary);
            }
            @Override
            protected BigDecimal computeValue() {
                try {
                    return totalSalary.get().subtract(basicSalary.get()).subtract(ageSalary.get());
                }catch (Exception e){
                    return new BigDecimal(0);
                }
            }
        });
        willSalary.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(totalSalary, fixSalary);
            }
            @Override
            protected BigDecimal computeValue() {
                try {
                    return totalSalary.get().subtract(fixSalary.get());
                }catch (Exception e){
                    return new BigDecimal(0);
                }
            }
        });


    }
    public SalaryDaoWrapper(SalaryDao data){
        this();
        wrap(data);
    }


    @Override
    public void wrap(SalaryDao data) {
        setId(data.getId());
        setYear(data.getYear());
        setMonth(data.getMonth());
        setBasicSalary(data.getBasicSalary());
        setAgeSalary(data.getAgeSalary());
        setTotalSalary(data.getTotalSalary());
        setClear(data.getClear()!=0);
        setDate(data.getDate());
        setFixSalary(data.getFixSalary());

        stuffDaoWrapper.wrap(data.getStuffDao());
    }

    @Override
    public SalaryDao get() {
        SalaryDao res = new SalaryDao();

        res.setId(getId());
        res.setMonth(getMonth());
        res.setYear(getYear());
        res.setTotalSalary(getTotalSalary());
        res.setBasicSalary(getBasicSalary());
        res.setAgeSalary(getAgeSalary());
        res.setClear(isClear()?1:0);
        res.setDate(getDate());
        res.setFixSalary(getFixSalary());

        res.setStuffDao(getStuffDaoWrapper().get());

        return res;
    }

    //////////////////////////////////////////////////

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
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

    public BigDecimal getBasicSalary() {
        return basicSalary.get();
    }

    public ObjectProperty<BigDecimal> basicSalaryProperty() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary.set(basicSalary);
    }

    public BigDecimal getAgeSalary() {
        return ageSalary.get();
    }

    public ObjectProperty<BigDecimal> ageSalaryProperty() {
        return ageSalary;
    }

    public void setAgeSalary(BigDecimal ageSalary) {
        this.ageSalary.set(ageSalary);
    }

    public BigDecimal getTotalSalary() {
        return totalSalary.get();
    }

    public ObjectProperty<BigDecimal> totalSalaryProperty() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary.set(totalSalary);
    }

    public boolean isClear() {
        return clear.get();
    }

    public BooleanProperty clearProperty() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear.set(clear);
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public StuffWrapper getStuffDaoWrapper() {
        return stuffDaoWrapper;
    }

    public BigDecimal getOtherSalary() {
        return otherSalary.get();
    }

    public ObjectProperty<BigDecimal> otherSalaryProperty() {
        return otherSalary;
    }

    public void setOtherSalary(BigDecimal otherSalary) {
        this.otherSalary.set(otherSalary);
    }

    public BigDecimal getWillSalary() {
        return willSalary.get();
    }

    public ObjectProperty<BigDecimal> willSalaryProperty() {
        return willSalary;
    }

    public void setWillSalary(BigDecimal willSalary) {
        this.willSalary.set(willSalary);
    }

    public SalaryDaoWrapper setStuffDaoWrapper(StuffWrapper stuffDaoWrapper) {
        this.stuffDaoWrapper = stuffDaoWrapper;
        return this;
    }

    public BigDecimal getFixSalary() {
        return fixSalary.get();
    }

    public ObjectProperty<BigDecimal> fixSalaryProperty() {
        return fixSalary;
    }

    public void setFixSalary(BigDecimal fixSalary) {
        this.fixSalary.set(fixSalary);
    }
}
