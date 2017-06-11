package cn.keepfight.qsmanager.model;

import java.sql.Timestamp;

/**
 * 送货单表模型
 * Created by tom on 2017/6/6.
 */
public class DeliveryModel {
    private Long id;
    private Long oid;
    private String serial;
    private Timestamp ddate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Timestamp getDdate() {
        return ddate;
    }

    public void setDdate(Timestamp ddate) {
        this.ddate = ddate;
    }
}
