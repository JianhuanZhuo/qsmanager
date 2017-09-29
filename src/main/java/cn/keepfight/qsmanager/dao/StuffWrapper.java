package cn.keepfight.qsmanager.dao;

import javafx.beans.property.*;

import java.math.BigDecimal;

/**
 * 员工数据包装器
 * Created by tom on 2017/9/27.
 */
public class StuffWrapper implements DaoWrapper<StuffDao>{

    private LongProperty id = new SimpleLongProperty();
    private StringProperty serial = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> salary_basic = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> salary_annual = new SimpleObjectProperty<>(new BigDecimal(0));

    private OperatorWrapper operatorWrapper = new OperatorWrapper();

    @Override
    public void wrap(StuffDao data) {
        id.set(data.getId());
        serial.set(data.getSerial());
        name.set(data.getName());
        salary_annual.set(data.getSalary_annual());
        salary_basic.set(data.getSalary_basic());

        operatorWrapper.wrap(data.getOperatorDao());
    }

    @Override
    public StuffDao get() {
        StuffDao stuffDao = new StuffDao();
        stuffDao.setId(getId());
        stuffDao.setName(getName());
        stuffDao.setSerial(getSerial());
        stuffDao.setSalary_basic(getSalary_basic());
        stuffDao.setSalary_annual(getSalary_annual());

        stuffDao.setOperatorDao(operatorWrapper.get());
        return stuffDao;
    }

    ////////////////////////////////////////////////////////////////////////////
    public OperatorWrapper getOperatorWrapper() {
        return operatorWrapper;
    }

    ////////////////////////////////////////////////////////////////////////////
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public BigDecimal getSalary_basic() {
        return salary_basic.get();
    }

    public ObjectProperty<BigDecimal> salary_basicProperty() {
        return salary_basic;
    }

    public void setSalary_basic(BigDecimal salary_basic) {
        this.salary_basic.set(salary_basic);
    }

    public BigDecimal getSalary_annual() {
        return salary_annual.get();
    }

    public ObjectProperty<BigDecimal> salary_annualProperty() {
        return salary_annual;
    }

    public void setSalary_annual(BigDecimal salary_annual) {
        this.salary_annual.set(salary_annual);
    }
}
