package cn.keepfight.qsmanager.model;

import cn.keepfight.utils.FXReflectUtils;
import cn.keepfight.utils.FXUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private boolean deli = false;
    private boolean building = false;

    private String cust;

    private List<OrderItemModel> goodsModels = new ArrayList<>();

    @Override
    public void set(OrderModel orderModel) {
        FXReflectUtils.attrAssign(orderModel, this, "id", "cid", "serial", "orderdate", "note", "deli", "building");
    }

    @Override
    public OrderModel get() {
        OrderModel model = new OrderModel();
        FXReflectUtils.attrAssign(this, model, "id", "cid", "serial", "orderdate", "note", "deli", "building");
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
        // 为每个设置对齐 ID
        if (goodsModels!=null && getId()!=null){
            goodsModels.stream().filter(Objects::nonNull).forEach(x->x.setOid(getId()));
        }
        this.goodsModels = goodsModels;
    }

    public String getCust() {
        return cust;
    }

    public void setCust(String cust) {
        this.cust = cust;
    }

    public boolean isDeli() {
        return deli;
    }

    public void setDeli(boolean deli) {
        this.deli = deli;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }
}
