package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.*;

import java.util.List;

/**
 * 送货映射器
 * Created by tom on 2017/6/21.
 */
public interface DeliveryMapper {

    /**
     * 选择指定条件的全部订单，其中客户、年份、月份、匹配订单号可以为空，表示忽视该条件
     * @param selection 指定条件选择器，包括时间、客户、匹配订单号
     */
    List<DeliveryModelFull> selectAll(DeliverySelection selection) throws Exception;

    /**
     * 根据ID选择送货单
     */
    DeliveryModelFull selectByID(Long id) throws Exception;
    /**
     * 插入一条新的送货记录
     */
    void insert(DeliveryModel model) throws Exception;

    /**
     * 修改送货记录
     */
    void update(DeliveryModel model) throws Exception;

    /**
     * 删除送货记录
     */
    void delete(DeliveryModel model) throws Exception;

    /**
     * 查询指定客户和年份的全部月份的送货总额
     */
    List<AnnualTotalModel> supAnnualTotal(DeliverySelection selection);
}
