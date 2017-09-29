package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.StuffDao;

import java.util.List;

/**
 * 员工表映射器
 * Created by tom on 2017/6/7.
 */
public interface StuffMapper extends Mapper {

    /**`
     * 选择全部员工
     */
    List<StuffDao> selectAll() throws Exception;

    /**
     * 根据员工 ID 选择客户
     */
    StuffDao selectByID(Long id) throws Exception;

    /**
     * 更新指定员工，以ID为准
     */
    void update(StuffDao custom) throws Exception;

    /**
     * 插入指定员工，插入后，该员工的ID将得到更新
     */
    void insert(StuffDao custom) throws Exception;

    /**
     * 删除指定员工，以ID为准。
     */
    void deleteByID(Long id) throws Exception;
}
