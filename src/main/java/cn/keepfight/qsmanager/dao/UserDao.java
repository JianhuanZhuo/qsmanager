package cn.keepfight.qsmanager.dao;

import java.io.Serializable;

/**
 * 系统用户数据访问对象
 * 有了这个对象，这可以访问系统
 * Created by tom on 2017/9/24.
 */
public class UserDao implements Serializable{
    private Long id;
    private String account;
    private String password;

    /**
     * 菜单访问权限字符串，这是一个用 ~ 拼接的串
     */
    private String authority;

    /**
     * 账户是否已停用
     */
    private Boolean halt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHalt() {
        return halt;
    }

    public void setHalt(Boolean halt) {
        this.halt = halt;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDao userDao = (UserDao) o;

        if (id != null ? !id.equals(userDao.id) : userDao.id != null) return false;
        if (account != null ? !account.equals(userDao.account) : userDao.account != null) return false;
        if (password != null ? !password.equals(userDao.password) : userDao.password != null) return false;
        if (authority != null ? !authority.equals(userDao.authority) : userDao.authority != null) return false;
        return halt != null ? halt.equals(userDao.halt) : userDao.halt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        result = 31 * result + (halt != null ? halt.hashCode() : 0);
        return result;
    }
}
