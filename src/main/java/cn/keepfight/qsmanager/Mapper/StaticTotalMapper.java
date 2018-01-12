package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.StaticTotalSellDao_i;
import cn.keepfight.qsmanager.dao.TupleDao;
import cn.keepfight.qsmanager.dao.analysis.MaterialAnalysisDao_i;

import java.util.List;

public interface StaticTotalMapper {

    /**
     * 统计收入总表
     */
    List<StaticTotalSellDao_i> staticSellAll(Long year) throws Exception;

    /**
     * 统计指定年份的每月原料采购情况
     */
    List<MaterialAnalysisDao_i> staticMaterialByYear(Long year) throws Exception;


    /**
     * 统计支出总表
     */
    List<StaticTotalSellDao_i> staticReceiptAll(Long year) throws Exception;

    /**
     * 统计收支总表
     */
    List<TupleDao> staticTradeAllByYear(Long year) throws Exception;
}
