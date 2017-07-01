package cn.keepfight.qsmanager.model;

import cn.keepfight.utils.FXUtils;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

import java.math.BigDecimal;

/**
 * 订单项表模型
 * Created by tom on 2017/6/6.
 */
public class OrderItemModel implements ModelFull<ProductModel> {

    private LongProperty id = new SimpleLongProperty();
    private LongProperty oid = new SimpleLongProperty();
    private ObjectProperty<BigDecimal> rate = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> rebate = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> delifee = new SimpleObjectProperty<>();
    private StringProperty serial = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty detail = new SimpleStringProperty();
    private LongProperty packDefault = new SimpleLongProperty();// 选择的装数参考，如果当前选择的是散装，那么还有机会可以选择回默认装数
    private ObjectProperty<BigDecimal> price = new SimpleObjectProperty<>();
    private StringProperty picurl = new SimpleStringProperty();
    private StringProperty unit = new SimpleStringProperty();


    private ObjectProperty<BigDecimal> num = new SimpleObjectProperty<>(new BigDecimal(0));
    private LongProperty pack = new SimpleLongProperty(-1L);

    // 辅助运算对象
    transient private ObjectProperty<BigDecimal> rateTotal = new SimpleObjectProperty<>();
    transient private ObjectProperty<BigDecimal> takeTotal = new SimpleObjectProperty<>();
    transient private ObjectProperty<BigDecimal> totalWithRate = new SimpleObjectProperty<>();
    transient private ObjectProperty<BigDecimal> rebateTotal = new SimpleObjectProperty<>();
    transient private ObjectProperty<BigDecimal> actPayTotal = new SimpleObjectProperty<>();

    {
        FXUtils.bindProperties(takeTotal, this::getTakeTotal, pack, num, price);
        FXUtils.bindProperties(rateTotal, this::getRateTotal, takeTotal, rate);
        FXUtils.bindProperties(totalWithRate, this::getTotalWithRate, rateTotal, takeTotal);
        FXUtils.bindProperties(rebateTotal, this::getRebateTotal, rebate, num);
        FXUtils.bindProperties(actPayTotal, this::getActualPayTotal, totalWithRate, delifee, rateTotal);
    }

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

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public long getOid() {
        return oid.get();
    }

    public LongProperty oidProperty() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid.set(oid);
    }

    public BigDecimal getRate() {
        return rate.get();
    }

    public ObjectProperty<BigDecimal> rateProperty() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate.set(rate);
    }

    public BigDecimal getRebate() {
        return rebate.get();
    }

    public ObjectProperty<BigDecimal> rebateProperty() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate.set(rebate);
    }

    public BigDecimal getDelifee() {
        return delifee.get();
    }

    public ObjectProperty<BigDecimal> delifeeProperty() {
        return delifee;
    }

    public void setDelifee(BigDecimal delifee) {
        this.delifee.set(delifee);
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDetail() {
        return detail.get();
    }

    public StringProperty detailProperty() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail.set(detail);
    }

    public long getPackDefault() {
        return packDefault.get();
    }

    public LongProperty packDefaultProperty() {
        return packDefault;
    }

    public void setPackDefault(long packDefault) {
        this.packDefault.set(packDefault);
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public String getPicurl() {
        return picurl.get();
    }

    public StringProperty picurlProperty() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl.set(picurl);
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public BigDecimal getNum() {
        return num.get();
    }

    public ObjectProperty<BigDecimal> numProperty() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num.set(num);
    }

    public long getPack() {
        return pack.get();
    }

    public LongProperty packProperty() {
        return pack;
    }

    public void setPack(long pack) {
        this.pack.set(pack);
    }

    public BigDecimal getTakeTotal(){
        try {
            return getPrice().multiply(getNum()).multiply(new BigDecimal(getPack()));
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public ObjectProperty<BigDecimal> takeTotalProperty(){
        return takeTotal;
    }

    public BigDecimal getRateTotal(){
        try {
            return getTakeTotal().multiply(getRate());
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public ObjectProperty<BigDecimal> rateTotalProperty(){
        return rateTotal;
    }

    public BigDecimal getTotalWithRate(){
        return getTakeTotal().add(getRateTotal());
    }

    public ObjectProperty<BigDecimal> totalWithRateProperty(){
        return totalWithRate;
    }

    public BigDecimal getRebateTotal(){
        try {
            return getRebate().multiply(getNum());
        }catch (Exception e){
            return new BigDecimal(0);
        }
    }

    public ObjectProperty<BigDecimal> rebateTotalProperty(){
        return rebateTotal;
    }

    public BigDecimal getActualPayTotal(){
        BigDecimal delifee = getDelifee();
        if (delifee == null){
            delifee = new BigDecimal(0);
        }
        return getTotalWithRate().subtract(getRebateTotal()).add(delifee);
    }

    public ObjectProperty<BigDecimal> actPayTotalProperty(){
        return actPayTotal;
    }

    public void update(OrderItemModel model) {
        setPicurl(model.getPicurl());
        setName(model.getName());
        setSerial(model.getSerial());
        setDetail(model.getDetail());
        setPrice(model.getPrice());
        setPackDefault(model.getPackDefault());
        setPack(model.getPack());
        setUnit(model.getUnit());
        setRate(model.getRate());
        setRebate(model.getRebate());
        setDelifee(model.getDelifee());
    }
}
