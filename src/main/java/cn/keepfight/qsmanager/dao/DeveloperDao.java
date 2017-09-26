package cn.keepfight.qsmanager.dao;

import java.io.Serializable;

/**
 * 开发者对象
 * Created by 卓建欢 on 2017/9/25.
 */
public class DeveloperDao implements Serializable{

    Long id;

    private OperatorDao operatorDao;

    public OperatorDao getOperatorDao() {
        return operatorDao;
    }

    public void setOperatorDao(OperatorDao operatorDao) {
        this.operatorDao = operatorDao;

        // 设置访问权限为全部可访问类型
        this.operatorDao.setAuthority("#");
    }
}
