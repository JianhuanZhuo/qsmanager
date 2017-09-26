package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.SupplierDao;

import java.util.List;

/**
 * 供应商表映射器
 * Created by tom on 2017/6/7.
 */
public interface SupplierMapper extends Mapper {

    /**
     * 选择全部供应商
     */
    List<SupplierDao> selectAll() throws Exception;

    /**
     * 根据供应商 ID 选择客户
     */
    SupplierDao selectByID(Long id) throws Exception;

//    /**
//     * 更新指定供应商，以ID为准
//     */
//    void update(SupplierDao custom) throws Exception;

    /**
     * 插入指定供应商，插入后，该供应商的ID将得到更新
     */
    void insert(SupplierDao custom) throws Exception;

    /**
     * 删除指定供应商，以ID为准。
     */
    void deleteByID(Long id) throws Exception;
}
