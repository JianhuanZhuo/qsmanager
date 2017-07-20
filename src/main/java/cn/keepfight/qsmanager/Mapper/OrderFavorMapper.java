package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.OrderFavorModel;
import cn.keepfight.qsmanager.model.ProductModel;

import java.util.List;

/**
 * 原料表映射器
 * Created by tom on 2017/6/7.
 */
public interface OrderFavorMapper extends Mapper {

    /**
     * 指定客户选择全部常见订购列表
     */
    List<ProductModel> selectAll(Long cid) throws Exception;

    /**
     * 插入指定常见订购列表，插入后，该用户的ID将得到更新
     */
    void insert(OrderFavorModel model) throws Exception;

    /**
     * 删除指定常见订购列表，以ID为准。
     */
    void delete(OrderFavorModel model) throws Exception;
}
