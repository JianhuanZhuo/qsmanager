package cn.keepfight.qsmanager.model;

/**
 * 用户模型类，表示登录系统的用户
 * Created by tom on 2017/6/5.
 */
public class User {

    public User() {
    }

    public User(String acc, String psw) {
        this.acc = acc;
        this.psw = psw;
    }

    private int id;
    private String acc;
    private String psw;
    private int utype;
    private int relateID;//关联ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public int getUtype() {
        return utype;
    }

    public void setUtype(int utype) {
        this.utype = utype;
    }

    public int getRelateID() {
        return relateID;
    }

    public void setRelateID(int relateID) {
        this.relateID = relateID;
    }

    @Override
    public String toString() {
        return "" + id + "-" + utype + "-" + acc + "-" + psw + "-" + relateID;
    }
}
