package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.*;

import java.util.List;

/**
 * 供应送货记录表映射器
 * Created by tom on 2017/6/7.
 */
public interface ReceiptMapper extends Mapper {

    /**
     * 选择特定供应送货记录、特定时间的全部送货记录
     */
    List<ReceiptModelFull> selectAll(ReceiptSelection selection) throws Exception;


    /**
     * 查询指定供应商和年份的全部月份的供应总额
     */
    List<AnnualTotalModel> supAnnualTotal(ReceiptSelection selection);

    /**
     * 查询指定年份的全部供应商采购总额
     */
    List<MonStatModel> takeStat(String year);

    /**
     * 插入指定供应送货记录，插入后，该供应送货记录的ID将得到更新
     */
    void insert(ReceiptModel model) throws Exception;

    /**
     * 删除指定供应送货记录，以ID为准。
     */
    void delete(ReceiptModel model) throws Exception;


    /**
     * 获取供应商送货全部可用的年份
     */
    List<Long> selectYear() throws Exception;
}
