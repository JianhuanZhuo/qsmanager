package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.*;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

/**
 * 员工表映射器
 * Created by tom on 2017/6/7.
 */
public interface SalaryMapper extends Mapper {

    /**
     * 选择指定年份的工资统计情况
     */
    List<YearStaticDao> staticSalaryByYear(Long year) throws Exception;

    /**
     * 选择指定年月的工资情况
     */
    List<SalaryDao_i> selectSalarysByMonth(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;


    /**
     * 选择指定年月和工人 ID 查询当月工资以便修改
     */
    SalaryDao_i selectSalarysByMonthAndStuff(
            @Param("year") Long year,
            @Param("month") Long month,
            @Param("stuff_id") Long stuff_id) throws Exception;


    /**
     * 修改指定年月和工人 ID 的当月工资
     */
    void updateSalarysByMonthAndStuff(SalaryDao salaryDao) throws Exception;

    /**
     * 删除指定年月和工人 ID 的当月工资
     */
    void deleteByMonthAndStuff(SalaryDao salaryDao) throws Exception;

    /**
     * 选择指定年月中可以增加工资条目的员工
     */
    List<StuffDao> selectStuffCanAddByMonth(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 添加一条员工工资记录
     */
    void addNewSalary(SalaryDao salaryDao) throws Exception;

    /**
     * 选择还款详情
     */
    List<StuffTardyDao> selectTardyDetailsEachDate(
            @Param("year") Long year,
            @Param("month") Long month,
            @Param("stuff_id") Long stuff_id) throws Exception;


    /**
     * 员工工资拖欠情况
     */
    List<SalaryTardyDao> selectStuffSalaryTardy() throws Exception;

    /**
     * 选择指定员工 ID 的拖欠工资及其月份，这个月份是对齐全部月份的+
     */
    List<StuffTardyDao> selectTardyByStuff(@Param("stuff_id") Long stuff_id) throws Exception;


    /**
     * 批量插入工资发放
     */
    void insertIntoSalaryIncome(List<SalaryPayDao> list) throws Exception;

    /**
     * 删除指定日期下对某个月的工资发放
     */
    void deleteMonthSalaryIncomeByDate(
            @Param("year") Long year,
            @Param("month") Long month,
            @Param("date") String date) throws  Exception;
}
