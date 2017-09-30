package cn.keepfight.qsmanager.dao;

import java.util.Date;

/**
 * 系统操作对象dao.
 * 表示可以登陆该系统的用户对象
 * Created by 卓建欢 on 2017/9/25.
 */
public class OperatorDao {

    private Long id;

    private UserDao userDao;

    private String account;
    private String password;

    /**
     * 菜单访问权限字符串，这是一个用 ~ 拼接的串
     * 空字符串表示无访问权限<br/>
     * <pre>#</pre> 表示全部访问
     */
    private String authority;

    /**
     * 最后一次登陆时间
     */
    private Date last_login_stamp;

    public String getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setAccount(String account) {
        System.out.println("setAccount:"+account);
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        System.out.println("setAuthority:"+authority);
        this.authority = authority;
    }

    public Date getLast_login_stamp() {
        return last_login_stamp;
    }

    public OperatorDao setLast_login_stamp(Date last_login_stamp) {
        this.last_login_stamp = last_login_stamp;
        return this;
    }

    /**
     * 设置最后一次登陆时间为当前系统时间
     */
    public void setLast_login_stamp(){
        this.last_login_stamp = new Date(System.currentTimeMillis());
    }
}
