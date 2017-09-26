package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.UserDao;
import cn.keepfight.qsmanager.model.CustomModel;

import java.util.List;

/**
 * 用户表映射器
 * Created by tom on 2017/6/7.
 */
public interface UserMapper extends Mapper {

    /**
     * 选择全部用户
     */
    List<UserDao> selectAll() throws Exception;

    /**
     * 根据用户 ID 选择客户
     */
    UserDao selectByID(Long id) throws Exception;

    /**
     * 更新指定用户，以ID为准
     */
    void update(UserDao custom) throws Exception;

    /**
     * 插入指定用户，插入后，该用户的ID将得到更新
     */
    void insert(UserDao custom) throws Exception;

    /**
     * 删除指定用户，以ID为准。
     */
    void deleteByID(Long id) throws Exception;
}
