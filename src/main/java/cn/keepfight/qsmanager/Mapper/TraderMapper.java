package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.TraderDao;

import java.util.List;

/**
 * 交易商表映射器
 * Created by tom on 2017/6/7.
 */
public interface TraderMapper extends Mapper {

    /**
     * 选择全部交易商
     */
    List<TraderDao> selectAll() throws Exception;

    /**
     * 根据交易商 ID 选择客户
     */
    TraderDao selectByID(Long id) throws Exception;

    /**
     * 更新指定交易商，以ID为准
     */
    void update(TraderDao custom) throws Exception;

    /**
     * 插入指定交易商，插入后，该交易商的ID将得到更新
     */
    void insert(TraderDao custom) throws Exception;

    /**
     * 删除指定交易商，以ID为准。
     */
    void deleteByID(Long id) throws Exception;
}
