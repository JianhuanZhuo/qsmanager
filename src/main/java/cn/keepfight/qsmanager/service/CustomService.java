package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.model.CustomModel;

import java.util.List;

/**
 * 客户服务接口
 * Created by tom on 2017/6/7.
 */
public interface CustomService {

    /**
     * 选择全部客户即 <code>select *</code>
     */
    List<CustomModel> selectAll() throws Exception;

    /**
     * 更新指定客户，以ID为准
     */
    void update(CustomModel model) throws Exception;

    /**
     * 插入指定客户，插入后，该用户的ID将得到更新
     */
    void insert(CustomModel model) throws Exception;

    /**
     * 删除指定客户，以ID为准。
     */
    void delete(CustomModel model) throws Exception;
}
