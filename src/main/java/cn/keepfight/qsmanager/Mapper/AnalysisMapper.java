package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.analysis.PriceAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.SellAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.SellSumDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnalysisMapper {

    /**
     * 统计指定年月的销售数量
     */
    List<SellAnalysisDao> staticSellByMonth(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 统计指定客户的每月销售箱数
     */
    List<SellSumDao> staticSellSumByCust(Long cid) throws Exception;

    /**
     * 选择指定年份的产品分月销售数和利润
     */
    List<PriceAnalysisDao> staticPriceByYear(Long year) throws Exception;
}
