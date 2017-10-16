package cn.keepfight.qsmanager.dao.salary;

import cn.keepfight.qsmanager.dao.StuffDao;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 员工的应还工资统计
 * Created by 卓建欢 on 2017/10/16.
 */
public class StuffSalaryStaticDao {
    private StuffDao stuffDao;
    private BigDecimal total;
    private Map<Pair<Long, Long>, BigDecimal> yearMonthToSalary;

    public StuffDao getStuffDao() {
        return stuffDao;
    }

    public void setStuffDao(StuffDao stuffDao) {
        this.stuffDao = stuffDao;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Map<Pair<Long, Long>, BigDecimal> getYearMonthToSalary() {
        return yearMonthToSalary;
    }

    public void setYearMonthToSalary(Map<Pair<Long, Long>, BigDecimal> yearMonthToSalary) {
        this.yearMonthToSalary = yearMonthToSalary;
    }
}
