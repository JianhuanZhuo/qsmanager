package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 年份统计Dao
 *
 * Created by tom on 2017/10/15.
 */
public class YearStaticDaoWrapper implements DaoWrapper<YearStaticDao> {
    private StringProperty month = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> given = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> tarby = new SimpleObjectProperty<>();

    public YearStaticDaoWrapper(){
        tarby.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(total, given);
            }
            @Override
            protected BigDecimal computeValue() {
                try {
                return total.get().subtract(given.get());
                }catch (Exception e) {
//                    e.printStackTrace();
                    return new BigDecimal(0);
                }
            }
        });
    }

    public YearStaticDaoWrapper(YearStaticDao dao) {
        this();
        wrap(dao);
    }

    @Override
    public void wrap(YearStaticDao data) {
//        setMonth(data.getMonth());
//        setTotal(data.getTotal());
//        setGiven(data.getGiven());
        FXReflectUtils.attrsCopy(data, this);
    }

    @Override
    public YearStaticDao get() {
        YearStaticDao dao = new YearStaticDao();
//        dao.setMonth(getMonth());
//        dao.setTotal(getTotal());
//        dao.setGiven(getGiven());
        FXReflectUtils.attrsCopy(this, dao);
        return dao;
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }

    public BigDecimal getGiven() {
        return given.get();
    }

    public ObjectProperty<BigDecimal> givenProperty() {
        return given;
    }

    public void setGiven(BigDecimal given) {
        this.given.set(given);
    }

    public BigDecimal getTarby() {
        return tarby.get();
    }

    public ObjectProperty<BigDecimal> tarbyProperty() {
        return tarby;
    }
}
