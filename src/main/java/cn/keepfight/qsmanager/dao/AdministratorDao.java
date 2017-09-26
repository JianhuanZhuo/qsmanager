package cn.keepfight.qsmanager.dao;

import java.io.Serializable;

/**
 * 管理员对象
 * Created by 卓建欢 on 2017/9/25.
 */
public class AdministratorDao implements Serializable{

    private Long id;

    private OperatorDao operatorDao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperatorDao getOperatorDao() {
        return operatorDao;
    }

    public void setOperatorDao(OperatorDao operatorDao) {
        this.operatorDao = operatorDao;

        // 设置访问权限为全部可访问类型
        this.operatorDao.setAuthority("#");
    }
}
