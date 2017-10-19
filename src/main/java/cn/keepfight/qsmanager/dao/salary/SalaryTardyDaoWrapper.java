package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 表示当前工人拖欠工资明细的包装器
 * Created by tom on 2017/10/18.
 */
public class SalaryTardyDaoWrapper implements DaoWrapper<SalaryTardyDao> {
    private StringProperty name = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> sum = new SimpleObjectProperty<>();
    private List<StuffTardyDao> details;

    public SalaryTardyDaoWrapper(){}

    public SalaryTardyDaoWrapper(SalaryTardyDao data){
        this();
        wrap(data);
    }


    @Override
    public void wrap(SalaryTardyDao data) {
        setName(data.getName());
        setSum(data.getSum());
        setDetails(data.getDetails());
    }

    @Override
    public SalaryTardyDao get() {
        SalaryTardyDao res = new SalaryTardyDao();
        res.setName(getName());
        res.setSum(getSum());
        res.setDetails(getDetails());
        return res;
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

    public BigDecimal getSum() {
        return sum.get();
    }

    public ObjectProperty<BigDecimal> sumProperty() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum.set(sum);
    }

    public List<StuffTardyDao> getDetails() {
        return details;
    }

    public SalaryTardyDaoWrapper setDetails(List<StuffTardyDao> details) {
        this.details = details;
        return this;
    }
}
