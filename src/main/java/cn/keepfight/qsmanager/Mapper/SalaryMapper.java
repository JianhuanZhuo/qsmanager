package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao_i;
import cn.keepfight.qsmanager.dao.salary.YearStaticDao;
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
     * 修改指定年月的固定工资发放时间
     */
    void updateSalarysDateByMonth(
            @Param("year") Long year,
            @Param("month") Long month,
            @Param("date") Date date) throws Exception;

    /**
     * 添加一条员工工资记录
     */
    void addNewSalary(SalaryDao salaryDao) throws Exception;

}
