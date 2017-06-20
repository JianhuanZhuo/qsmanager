package cn.keepfight.qsmanager.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * 订单项表模型
 * Created by tom on 2017/6/6.
 */
public class OrderItemModel implements ModelFull<ProductModel> {

    private Long id;
    private Long oid;
    private BigDecimal rate;
    private BigDecimal rebate;
    private BigDecimal delifee;
    private String serial;
    private String name;
    private String detail;
    private Long packDefault;  // 选择的装数参考，如果当前选择的是散装，那么还有机会可以选择回默认装数
    private BigDecimal price;
    private String picurl;
    private String unit;


    private ObjectProperty<BigDecimal> num = new SimpleObjectProperty<>(new BigDecimal(0));
    private LongProperty pack = new SimpleLongProperty(-1L);

    public OrderItemModel(){}
    public OrderItemModel(ProductModel model){
        set(model);
    }

    @Override
    public void set(ProductModel model) {
        setId(model.getId());
        setDetail(model.getDetail());
        setName(model.getName());
        setSerial(model.getSerial());
        setPackDefault(model.getPack());
        setPrice(model.getPrice());
        setUnit(model.getUnit());
        setPicurl(model.getPicurl());
    }

    @Override
    public ProductModel get() {
        return null;
    }

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

    public BigDecimal getNum() {
        return num.get();
    }

    public ObjectProperty<BigDecimal> getNumProperty() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num.set(num);
    }


    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getDelifee() {
        return delifee;
    }

    public void setDelifee(BigDecimal delifee) {
        this.delifee = delifee;
    }

    public Long getPackDefault() {
        return packDefault;
    }

    public void setPackDefault(Long packDefault) {
        this.packDefault = packDefault;
    }

    public long getPack() {
        return pack.get();
    }

    public LongProperty packProperty() {
        return pack;
    }

    public void setPack(long packSel) {
        this.pack.set(packSel);
    }

    public BigDecimal getTakeTotal(){
        try {
            return getPrice().multiply(getNum()).multiply(new BigDecimal(getPack()));
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public BigDecimal getRateTotal(){
        try {
            return getTakeTotal().multiply(getRate());
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public BigDecimal getTotalWithRate(){
        return getTakeTotal().add(getRateTotal());
    }

    public BigDecimal getRebateTotal(){
        try {
            return getRebate().multiply(getNum());
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public BigDecimal getActualPayTotal(){
        BigDecimal delifee = getDelifee();
        if (delifee == null){
            delifee = new BigDecimal(0);
        }
        return getTotalWithRate().subtract(getRebateTotal()).subtract(delifee);
    }
}
