package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.AnalysisMapper;
import cn.keepfight.qsmanager.dao.analysis.PriceAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.SellAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.SellSumDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public abstract class AnalysisServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 统计指定年月的销售数量
     */
    public static List<SellAnalysisDao> staticSellByMonth(Long year, Long month) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(AnalysisMapper.class).staticSellByMonth(year, month);
        }
    }


    /**
     * 统计指定客户的每月销售箱数
     */
    public static List<SellSumDao> staticSellSumByCust(Long cid) throws Exception{
        return FXUtils.getMapper(factory, AnalysisMapper.class, AnalysisMapper::staticSellSumByCust, cid);
    }


    /**
     * 选择指定年份的产品分月销售数和利润
     */
    public static List<PriceAnalysisDao> staticPriceByYear(Long year) throws Exception{
        return FXUtils.getMapper(factory, AnalysisMapper.class, AnalysisMapper::staticPriceByYear, year);
    }
}
