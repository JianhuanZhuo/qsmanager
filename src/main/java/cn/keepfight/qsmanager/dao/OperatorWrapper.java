package cn.keepfight.qsmanager.dao;

import javafx.beans.property.*;

import java.util.Date;

/**
 * 系统操作者包装器
 * Created by tom on 2017/9/27.
 */
public class OperatorWrapper implements DaoWrapper<OperatorDao> {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty account = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty authority = new SimpleStringProperty();
    private ObjectProperty<Date> last_login_stamp = new SimpleObjectProperty<>();

    private UserWrapper userWrapper = new UserWrapper();

    @Override
    public void wrap(OperatorDao data) {
        setId(data.getId());
        setAccount(data.getAccount());
        setPassword(data.getPassword());
        setAuthority(data.getAuthority());
        setLast_login_stamp(data.getLast_login_stamp());

        System.out.println("setAuthority:"+data.getAuthority());

        userWrapper.wrap(data.getUserDao());
    }

    @Override
    public OperatorDao get() {
        OperatorDao operator = new OperatorDao();
        operator.setId(getId());
        operator.setAccount(getAccount());
        operator.setPassword(getPassword());
        operator.setAuthority(getAuthority());
        operator.setLast_login_stamp(getLast_login_stamp());

        operator.setUserDao(userWrapper.get());
        return operator;
    }

    ////////////////////////////////////////////////////////////////////////////
    // 嵌套包装器
    public UserWrapper getUserWrapper() {
        return userWrapper;
    }


    ///////////////////////////////////////////////////////////////////////////
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getAccount() {
        return account.get();
    }

    public StringProperty accountProperty() {
        return account;
    }

    public void setAccount(String account) {
        this.account.set(account);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getAuthority() {
        return authority.get();
    }

    public StringProperty authorityProperty() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority.set(authority);
    }

    public Date getLast_login_stamp() {
        return last_login_stamp.get();
    }

    public ObjectProperty<Date> last_login_stampProperty() {
        return last_login_stamp;
    }

    public void setLast_login_stamp(Date last_login_stamp) {
        this.last_login_stamp.set(last_login_stamp);
    }

    public OperatorWrapper setUserWrapper(UserWrapper userWrapper) {
        this.userWrapper = userWrapper;
        return this;
    }
}
