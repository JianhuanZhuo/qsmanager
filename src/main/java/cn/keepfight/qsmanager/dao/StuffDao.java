package cn.keepfight.qsmanager.dao;

import java.math.BigDecimal;

/**
 * 员工数据访问对象
 * Created by tom on 2017/9/24.
 */
public class StuffDao {

    private Long id;

    private String serial;

    private String name;

    private OperatorDao operatorDao;

    /**
     * 基本工资
     */
    private BigDecimal salary_basic;

    /**
     * 工龄工资
     */
    private BigDecimal salary_annual;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperatorDao getOperatorDao() {
        return operatorDao;
    }

    public void setOperatorDao(OperatorDao operatorDao) {
        this.operatorDao = operatorDao;
        // 员工默认为无访问权限
        operatorDao.setAuthority("");
    }

    public BigDecimal getSalary_basic() {
        return salary_basic;
    }

    public void setSalary_basic(BigDecimal salary_basic) {
        this.salary_basic = salary_basic;
    }

    public BigDecimal getSalary_annual() {
        return salary_annual;
    }

    public void setSalary_annual(BigDecimal salary_annual) {
        this.salary_annual = salary_annual;
    }
}
