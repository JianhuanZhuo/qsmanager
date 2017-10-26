package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.annual.AnnualDao;
import cn.keepfight.qsmanager.model.*;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商年对账表Mapper接口
 * Created by tom on 2017/6/15.
 */
public interface SupAnnuMapper {

    /**
     * 选择指定年份、指定供应商的年度对账表
     */
    SupAnnualModel selectAnnual(AnnualSelection selection) throws Exception;

    /**
     * 指定年份、指定供应商查询年度对账的每月明细
     */
    List<SupAnnualMonModel> selectMon(AnnualSelection selection) throws Exception;

    /**
     * 指定年份统计付款
     */
    List<MonStatModel> payStat(Long year) throws Exception;

    /**
     * 指定年份统计汇总
     */
    List<TPStatModel> tpStat(Long year) throws Exception;

    /**
     * 插入新的年对账表，如果指定的年份的对账表已存在，则异常
     */
    void insertAnnual(SupAnnualModel model) throws Exception;

    /**
     * 更新年对账表
     */
    void updateAnnual(SupAnnualModel model) throws Exception;

    /**
     * 插入新的月明细，如果指定对账单的当前月份已存在会插入错误
     */
    void insertMon(SupAnnualMonModel model) throws Exception;

    /**
     * 更新月明细
     */
    void updateMon(SupAnnualMonModel model) throws Exception;


    /**
     * 指定供应商和年份选择对账表
     */
    List<AnnualDao> staticAnnualMonByMonAndSup(
            @Param("sid") Long sid,
            @Param("year") Long year) throws Exception;

    /**
     *
     * 指定供应商和年份统计，该年份之前的
     */
    BigDecimal staticAnnualLeft(
            @Param("sid") Long sid,
            @Param("year") Long year) throws Exception;

}
