package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.model.ReceiptModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import cn.keepfight.qsmanager.model.SupplyModel;

import java.util.List;

/**
 * 供应商服务接口
 * Created by tom on 2017/6/7.
 */
public interface ReceiptService {

//    /**
//     * 获得指定供应商的送货记录的全部年份
//     * @param sid
//     * @return
//     * @throws Exception
//     */
//    List<String> getYears(Long sid) throws Exception;

    /**
     * 选择特定供应商、特定时间的全部送货记录
     */
    List<ReceiptModelFull> selectAll(Long sid, Long year, Long month) throws Exception;

    /**
     * 插入一条送货记录
     */
    void insert(ReceiptModelFull model) throws Exception;

    /**
     * 删除指定 ID 的送货记录。
     */
    void delete(ReceiptModel model) throws Exception;
}
