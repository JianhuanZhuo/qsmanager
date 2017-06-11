package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import cn.keepfight.qsmanager.model.ReceiptModel;

import java.util.List;

/**
 * 供应送货明细表映射器
 * Created by tom on 2017/6/7.
 */
public interface ReceiptDetailMapper extends Mapper {

    /**
     * 选择属于某条送货记录的全部明细
     */
    List<ReceiptDetailModel> selectAll(Long rid) throws Exception;

    /**
     * 插入指定供应送货明细，插入后，该供应送货明细的ID将得到更新
     */
    void insert(ReceiptDetailModel model) throws Exception;

    /**
     * 删除指定供应送货明细，以ID为准。
     */
    void delete(ReceiptDetailModel model) throws Exception;
}
