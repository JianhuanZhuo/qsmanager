package cn.keepfight.qsmanager.dao;

import javafx.beans.property.*;

/**
 * 用户 Dao 包装器
 * Created by tom on 2017/9/27.
 */
public class UserWrapper implements DaoWrapper<UserDao>{

    private LongProperty id = new SimpleLongProperty();
    private StringProperty nickname = new SimpleStringProperty();
    private BooleanProperty halt = new SimpleBooleanProperty();
    private StringProperty note = new SimpleStringProperty();


    @Override
    public void wrap(UserDao data) {
        id.set(data.getId());
        nickname.set(data.getNickname());
        halt.set(data.getHalt());
        note.set(data.getNote());
    }

    @Override
    public UserDao get() {
        UserDao user = new UserDao();
        user.setId(getId());
        user.setNickname(getNickname());
        user.setHalt(isHalt());
        user.setNote(getNote());
        return user;
    }


    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getNickname() {
        return nickname.get();
    }

    public StringProperty nicknameProperty() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname.set(nickname);
    }

    public boolean isHalt() {
        return halt.get();
    }

    public BooleanProperty haltProperty() {
        return halt;
    }

    public void setHalt(boolean halt) {
        this.halt.set(halt);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }
}
