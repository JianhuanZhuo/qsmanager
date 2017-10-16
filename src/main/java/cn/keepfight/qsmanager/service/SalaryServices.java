package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.OperatorMapper;
import cn.keepfight.qsmanager.Mapper.SalaryMapper;
import cn.keepfight.qsmanager.Mapper.StuffMapper;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao_i;
import cn.keepfight.qsmanager.dao.salary.YearStaticDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工服务接口
 * Created by tom on 2017/6/7.
 */
public abstract class SalaryServices {
    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 选择指定年份
     */
    public static List<YearStaticDao> staticSalaryByYear(Long year) throws Exception {
        return FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::staticSalaryByYear, year);
    }

    /**
     * 选择指定年月的工资情况
     */
    public static List<SalaryDao> selectSalarysByMonth(Long year, Long month) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            List<SalaryDao_i> internalRes = session.getMapper(SalaryMapper.class).selectSalarysByMonth(year, month);
            List<SalaryDao> res = new ArrayList<>(12);
            // 填充
            for (SalaryDao_i e: internalRes) {
                res.add(new SalaryDao(e,session.getMapper(StuffMapper.class).selectByID(e.getStuffDaoID()) ));
            }
            return res;
        }
    }


    /**
     * 选择指定年月中可以增加工资条目的员工
     */
    public static List<StuffDao> selectStuffCanAddByMonth(Long year, Long month) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(SalaryMapper.class).selectStuffCanAddByMonth(year, month);
        }
    }


    /**
     * 添加一条员工工资记录
     */
    public static void addNewSalary(SalaryDao salaryDao) throws Exception{
        FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::addNewSalary, salaryDao);
    }
}
