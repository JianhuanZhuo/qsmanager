package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.*;

import java.util.List;

/**
 * 订单映射器
 * Created by tom on 2017/6/20.
 */
public interface OrderMapper extends Mapper{

    /**
     * 选择指定条件的全部订单，其中客户、年份、月份、选择状态可以为空，表示忽视该条件
     * @param selection 指定条件选择器，包括时间、客户、订单状态，
     */
    List<OrderModelFull> selectAll(OrderSelection selection) throws Exception;

    /**
     * 查询指定客户和年份的全部月份的订购总额，需要联表查询
     * 该查询包含两个参数：cid 指定客户 year 指定年份
     */
    List<AnnualTotalModel> supAnnualTotal(OrderSelection selection) throws Exception;

    /**
     * 新增订单，订单号为，YY-MM-DD-NUM 当天的全部订单序号
     * @param order 订单模型全，其中订单号由 SQL 语句搞定，可以吗？
     */
    void insert(OrderModel order) throws Exception;

    /**
     * 删除指定 ID 的订单记录。
     */
    void delete(OrderModel model) throws Exception;

    /**
     * 修改一条订单记录
     */
    void update(OrderModel model) throws Exception;

    /**
     * 获取订单全部可用的年份
     */
    List<Long> selectYear() throws Exception;

    /**
     * 获得下一个插入的 Ct 数
     */
    Long getCt(Long orderdate) throws Exception;
}
