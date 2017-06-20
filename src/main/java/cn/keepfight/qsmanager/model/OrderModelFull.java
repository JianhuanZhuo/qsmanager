package cn.keepfight.qsmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单全模型
 * Created by tom on 2017/6/19.
 */
public class OrderModelFull implements ModelFull<OrderModel>{
    private Long id;
    private Long cid;
    private String serial;
    private Long orderdate;
    private String note;
    private Long ct;

    private String cust;

    private List<OrderItemModel> goodsModels = new ArrayList<>();

    @Override
    public void set(OrderModel orderModel) {
        setId(orderModel.getId());
        setCid(orderModel.getCid());
        setSerial(orderModel.getSerial());
        setOrderdate(orderModel.getOrderdate());
        setNote(orderModel.getNote());
        setCt(orderModel.getCt());
    }

    @Override
    public OrderModel get() {
        OrderModel model = new OrderModel();
        model.setId(getId());
        model.setCid(getCid());
        model.setSerial(getSerial());
        model.setOrderdate(getOrderdate());
        model.setNote(getNote());
        model.setCt(getCt());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        System.out.println("OrderModelFull 设置ID：" + id);
        this.id = id;
        // 更新ID
        if (goodsModels != null){
            goodsModels.forEach(d -> d.setOid(id));
        }
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Long getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Long orderdate) {
        this.orderdate = orderdate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderItemModel> getOrderItemModels() {
        return goodsModels;
    }

    public void setOrderItemModels(List<OrderItemModel> goodsModels) {
        this.goodsModels = goodsModels;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }
}
