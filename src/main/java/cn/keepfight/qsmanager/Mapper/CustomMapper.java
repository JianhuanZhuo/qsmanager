package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.CustomModel;

import java.util.List;

/**
 * 客户表映射器
 * Created by tom on 2017/6/7.
 */
public interface CustomMapper extends Mapper {

    /**
     * 选择全部客户即 <code>select * where utype=2</code>
     * 这里不包含自己的哦
     */
    List<CustomModel> selectAll() throws Exception;


    /**
     * 根据客户ID 选择客户
     */
    CustomModel selectAllByID(Long cid) throws Exception;

    /**
     * 更新指定用户，以ID为准
     */
    void update(CustomModel custom) throws Exception;

    /**
     * 插入指定用户，插入后，该用户的ID将得到更新
     */
    void insert(CustomModel custom) throws Exception;

    /**
     * 删除指定用户，以ID为准。
     */
    void delete(CustomModel custom) throws Exception;


    /**
     * 检查用户的合法性
     *
     * @param user 带有账号和密码的用户对象
     * @return 若账号密码合法，则返回 null
     */
    CustomModel checkLegality(CustomModel user) throws Exception;

}
