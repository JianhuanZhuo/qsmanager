package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.model.*;

import java.util.List;

/**
 * 客户年对账表Mapper接口
 * Created by tom on 2017/6/15.
 */
public interface CustAnnuMapper {

    /**
     * 选择指定年份、指定客户的年度对账表
     */
    CustAnnualModel selectAnnual(AnnualSelection selection) throws Exception;

    /**
     * 指定年份、指定客户查询年度对账的每月明细
     */
    List<CustAnnualMonModel> selectMon(AnnualSelection selection) throws Exception;

    /**
     * 插入新的年对账表，如果指定的年份的对账表已存在，则异常
     */
    void insertAnnual(CustAnnualModel model) throws Exception;

    /**
     * 更新年对账表
     */
    void updateAnnual(CustAnnualModel model) throws Exception;

    /**
     * 插入新的月明细，如果指定对账单的当前月份已存在会插入错误
     */
    void insertMon(CustAnnualMonModel model) throws Exception;

    /**
     * 更新月明细
     */
    void updateMon(CustAnnualMonModel model) throws Exception;
}
