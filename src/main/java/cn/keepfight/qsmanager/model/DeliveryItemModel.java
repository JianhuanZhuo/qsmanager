package cn.keepfight.qsmanager.model;

/**
 * 送货历史记录数
 * Created by tom on 2017/6/6.
 */
public class DeliveryItemModel {
    private Long id;
    private Long did;
    private Long iid;
    private Long num;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
