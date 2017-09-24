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

    private UserDao userDao;

    private Boolean halt;

    /**
     * 基本工资
     */
    private BigDecimal salaryBasic;

    /**
     * 工龄工资
     */
    private BigDecimal salaryAnnual;

    /**
     * 备注信息
     */
    private String note;

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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Boolean getHalt() {
        return halt;
    }

    public void setHalt(Boolean halt) {
        this.halt = halt;
    }

    public BigDecimal getSalaryBasic() {
        return salaryBasic;
    }

    public void setSalaryBasic(BigDecimal salaryBasic) {
        this.salaryBasic = salaryBasic;
    }

    public BigDecimal getSalaryAnnual() {
        return salaryAnnual;
    }

    public void setSalaryAnnual(BigDecimal salaryAnnual) {
        this.salaryAnnual = salaryAnnual;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
