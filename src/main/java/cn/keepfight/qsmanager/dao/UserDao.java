package cn.keepfight.qsmanager.dao;

import java.io.Serializable;

/**
 * 系统用户数据访问对象
 * 有了这个对象，这可以访问系统
 * Created by tom on 2017/9/24.
 */
public class UserDao implements Serializable{

    private Long id;

    /**
     * 系统问候昵称
     */
    private String nickname;

    /**
     * 账户是否已停用
     */
    private Boolean halt;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public Boolean getHalt() {
        return halt;
    }

    public void setHalt(Boolean halt) {
        this.halt = halt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
