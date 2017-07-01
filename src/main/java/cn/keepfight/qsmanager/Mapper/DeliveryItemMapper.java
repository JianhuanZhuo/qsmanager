package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.DeliveryItemModel;

import java.util.List;

/**
 * 送货明细映射器
 * Created by tom on 2017/6/21.
 */
public interface DeliveryItemMapper {

    /**
     * 根据送货单 ID 查询明细
     */
    List<DeliveryItemModel> selectByDid(Long did) throws Exception;

    /**
     * 插入一条新的送货明细
     */
    void insert(DeliveryItemModel model);

    /**
     * 根据送货单 ID 删除送货明细记录
     */
    void deleteByDid(Long did) throws Exception;
}
