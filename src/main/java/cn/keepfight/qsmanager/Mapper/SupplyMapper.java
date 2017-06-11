package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.SupplyModel;

import java.util.List;

/**
 * 供应商表映射器
 * Created by tom on 2017/6/7.
 */
public interface SupplyMapper extends Mapper {

    /**
     * 选择全部供应商即 <code>select *</code>
     * 这里不包含自己的哦
     */
    List<SupplyModel> selectAll() throws Exception;

    /**
     * 更新指定供应商，以ID为准
     */
    void update(SupplyModel model) throws Exception;

    /**
     * 插入指定供应商，插入后，该供应商的ID将得到更新
     */
    void insert(SupplyModel model) throws Exception;

    /**
     * 删除指定供应商，以ID为准。
     */
    void delete(SupplyModel model) throws Exception;
}
