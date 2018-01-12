package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.AnalysisMapper;
import cn.keepfight.qsmanager.dao.TupleDao;
import cn.keepfight.qsmanager.dao.analysis.*;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * 查询指定年份的交易总量
     */
    public static List<TupleDao> staticTradeByYear(Long year) throws Exception{
        return FXUtils.getMapper(factory, AnalysisMapper.class, AnalysisMapper::staticTradeByYear, year);
    }

    /**
     * 指定年统计每个月的供应金额
     */
    public static List<UnitMonthDao> staticTakeTradeByYear(Long year) throws Exception{
        return FXUtils.getMapper(factory, AnalysisMapper.class, AnalysisMapper::staticTakeTradeByYear, year)
                .stream().collect(Collectors.groupingBy(UnitMonthDao_i::getUid))
                .values().stream().map(list->{
                    UnitMonthDao dao = new UnitMonthDao();
                    dao.setUid(list.get(0).getUid());
                    dao.setUname(list.get(0).getUname());
                    list.forEach(e->{
                        switch (e.getMonth()){
                            case 1:dao.setMon1(e.getSum());break;
                            case 2:dao.setMon2(e.getSum());break;
                            case 3:dao.setMon3(e.getSum());break;
                            case 4:dao.setMon4(e.getSum());break;
                            case 5:dao.setMon5(e.getSum());break;
                            case 6:dao.setMon6(e.getSum());break;
                            case 7:dao.setMon7(e.getSum());break;
                            case 8:dao.setMon8(e.getSum());break;
                            case 9:dao.setMon9(e.getSum());break;
                            case 10:dao.setMon10(e.getSum());break;
                            case 11:dao.setMon11(e.getSum());break;
                            case 12:dao.setMon12(e.getSum());break;
                        }
                    });
                    return dao;
        }).collect(Collectors.toList());
    }
}
