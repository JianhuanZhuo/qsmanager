package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.StaticTotalMapper;
import cn.keepfight.qsmanager.dao.StaticTotalSellDao;
import cn.keepfight.qsmanager.dao.StaticTotalSellDao_i;
import cn.keepfight.qsmanager.dao.TupleDao;
import cn.keepfight.qsmanager.dao.analysis.MaterialAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.MaterialAnalysisDao_i;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class StaticTotalServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    public static List<StaticTotalSellDao> staticSellAll(Long year) throws Exception {
        return filterRawSell(staticSellAllRaw(year));
    }

    public static List<StaticTotalSellDao_i> staticSellAllRaw(Long year) throws Exception {
        return FXUtils.getMapper(factory, StaticTotalMapper.class, StaticTotalMapper::staticSellAll, year);
    }

    public static List<StaticTotalSellDao> filterRawSell(List<StaticTotalSellDao_i> dao_is) {
        if (dao_is == null) {
            return new ArrayList<>();
        }
        return dao_is.stream().collect(Collectors.groupingBy(StaticTotalSellDao_i::getUid))
                .values().stream().map(list -> {
                    StaticTotalSellDao dao = new StaticTotalSellDao();
                    dao.setUid(list.get(0).getUid());
                    dao.setUname(list.get(0).getUname());
                    dao.setUnitToSumMap(list.stream().collect(Collectors.toMap(StaticTotalSellDao_i::getUnit, StaticTotalSellDao_i::getSum)));
                    return dao;
                })
                .sorted(Comparator.comparing(StaticTotalSellDao::getUname))
                .collect(Collectors.toList());
    }

    public static List<StaticTotalSellDao> staticReceiptAll(Long year) throws Exception {
        return filterRawSell(staticReceiptAllRaw(year));
    }

    public static List<StaticTotalSellDao_i> staticReceiptAllRaw(Long year) throws Exception {
        return FXUtils.getMapper(factory, StaticTotalMapper.class, StaticTotalMapper::staticReceiptAll, year);
    }

    /**
     * 统计指定年份的每月原料采购情况
     */
    public static List<MaterialAnalysisDao> staticMaterialByYear(Long year) throws Exception {
        return staticMaterialByYearRawToDao(staticMaterialByYearRaw(year));
    }

    /**
     * 统计指定年份的每月原料采购情况
     */
    public static List<MaterialAnalysisDao_i> staticMaterialByYearRaw(Long year) throws Exception {
        return FXUtils.getMapper(factory, StaticTotalMapper.class, StaticTotalMapper::staticMaterialByYear, year);
    }

    /**
     * 统计指定年份的每月原料采购情况
     */
    public static List<MaterialAnalysisDao> staticMaterialByYearRawToDao(List<MaterialAnalysisDao_i> rawList) throws Exception {
        return rawList.stream().collect(Collectors.groupingBy(MaterialAnalysisDao_i::getMonth))
                .values().stream().map(list -> {
                    MaterialAnalysisDao dao = new MaterialAnalysisDao();
                    dao.setMonth(list.get(0).getMonth());
                    dao.setUnitMapSum(list.stream()
                            .collect(Collectors.toMap(MaterialAnalysisDao_i::getName, MaterialAnalysisDao_i::getSum)));
                    return dao;
                }).collect(Collectors.toList());
    }

    public static Map<String, BigDecimal> staticTradeAllByYear(Long year) throws Exception {
        return FXUtils.getMapper(factory, StaticTotalMapper.class, StaticTotalMapper::staticTradeAllByYear, year)
                .stream().collect(Collectors.toMap(TupleDao::getK, dao -> FXUtils.getDecimal(dao.getV())));
    }
}
