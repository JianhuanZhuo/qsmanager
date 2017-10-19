package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.SalaryMapper;
import cn.keepfight.qsmanager.Mapper.StuffMapper;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.*;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.SQLUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.Comparator;
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
            for (SalaryDao_i e : internalRes) {
                SalaryDao dao = new SalaryDao(e, session.getMapper(StuffMapper.class).selectByID(e.getStuffDaoID()));
                dao.setDetails(session.getMapper(SalaryMapper.class).selectTardyDetailsEachDate(year, month, e.getStuffDaoID()));
                res.add(dao);
            }
            res.sort(sortBySerial());
            return res;
        }
    }

    /**
     * 选择指定年月和工人 ID 查询当月工资以便修改
     */
    public static SalaryDao selectSalarysByMonthAndStuff(Long year, Long month, Long stuff_id) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            SalaryDao_i internalRes = session.getMapper(SalaryMapper.class).selectSalarysByMonthAndStuff(year, month, stuff_id);
            // 填充
            return new SalaryDao(internalRes, session.getMapper(StuffMapper.class).selectByID(internalRes.getStuffDaoID()));
        }
    }

    /**
     * 修改指定年月和工人 ID 的当月工资
     */
    public static void updateSalarysByMonthAndStuff(SalaryDao salaryDao) throws Exception {
        FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::updateSalarysByMonthAndStuff, salaryDao);
    }

    /**
     * 删除指定年月和工人 ID 的当月工资
     */
    public static void deleteByMonthAndStuff(SalaryDao salaryDao) throws Exception {
        FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::deleteByMonthAndStuff, salaryDao);
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
    public static void addNewSalary(SalaryDao salaryDao) throws Exception {
        FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::addNewSalary, salaryDao);
    }

    private static Comparator<SalaryDao> sortBySerial() {
        return (o1, o2) -> {
            if (o1 == null) {
                return 0;
            } else if (o2 == null) {
                return 1;
            }
            return o1.getStuffDao().getSerial().compareTo(o2.getStuffDao().getSerial());
        };
    }


    /**
     * 员工工资拖欠情况
     */
    public static List<SalaryTardyDao> selectStuffSalaryTardy() throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            List<SalaryTardyDao> res = SQLUtils.getMapperSession(session, SalaryMapper.class, SalaryMapper::selectStuffSalaryTardy);
            for (SalaryTardyDao d : res) {
                d.setDetails(SQLUtils.getMapperSession(session, SalaryMapper.class, SalaryMapper::selectTardyByStuff, d.getId()));
            }
            return res;
        }
    }


    /**
     * 批量插入工资发放
     */
    public static void insertIntoSalaryIncome(List<SalaryPayDao> list) throws Exception {
        FXUtils.getMapper(factory, SalaryMapper.class, SalaryMapper::insertIntoSalaryIncome, list);
    }


    /**
     * 删除指定日期下对某个月的工资发放
     */
    public static void deleteMonthSalaryIncomeByDate(Long year, Long month, String date) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            session.getMapper(SalaryMapper.class).deleteMonthSalaryIncomeByDate(year, month, date);
        }
    }
}
