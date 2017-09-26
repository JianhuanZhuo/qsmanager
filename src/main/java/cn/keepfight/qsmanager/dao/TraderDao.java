package cn.keepfight.qsmanager.dao;

/**
 * 贸易商数据访问对象
 * Created by tom on 2017/9/25.
 */
public class TraderDao {

    private Long id;
    private String serial;
    private String name;
    private String name_full;
    private String phone;
    private String fax;
    private String publicAccountName;
    private String publicAccountSerial;
    private String privateAccountName;
    private String privateAccountSerial;
    private String addr1;
    private String addr2;
    private String addr3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_full() {
        return name_full;
    }

    public void setName_full(String name_full) {
        this.name_full = name_full;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPublicAccountName() {
        return publicAccountName;
    }

    public void setPublicAccountName(String publicAccountName) {
        this.publicAccountName = publicAccountName;
    }

    public String getPublicAccountSerial() {
        return publicAccountSerial;
    }

    public void setPublicAccountSerial(String publicAccountSerial) {
        this.publicAccountSerial = publicAccountSerial;
    }

    public String getPrivateAccountName() {
        return privateAccountName;
    }

    public void setPrivateAccountName(String privateAccountName) {
        this.privateAccountName = privateAccountName;
    }

    public String getPrivateAccountSerial() {
        return privateAccountSerial;
    }

    public void setPrivateAccountSerial(String privateAccountSerial) {
        this.privateAccountSerial = privateAccountSerial;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }
}
