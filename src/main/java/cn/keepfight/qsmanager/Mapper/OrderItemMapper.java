package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.OrderItemModel;


import java.util.List;

/**
 * 订单货项映射器
 * Created by tom on 2017/6/20.
 */
public interface OrderItemMapper {

    /**
     * 查询指定订单的全部订单项
     */
    List<OrderItemModel> selectAllByOid(Long oid) throws Exception;


    /**
     * 插入货项
     * @param item 需要插入的货项
     */
    void insert(OrderItemModel item) throws Exception;

    /**
     * 删除指定订单项，以ID为准。
     */
    void delete(OrderItemModel model) throws Exception;

    /**
     * 删除指定订单 ID 的全部明细。
     */
    void deleteByOid(Long Oid) throws Exception;
}
