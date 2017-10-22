package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.SupRemitMapper;
import cn.keepfight.qsmanager.dao.annual.SupRemitDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class SupRemitServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 指定供应商 ID、年份、月份选择汇款记录
     */
    public static List<SupRemitDao> selectRemitByMonthAndSup(
            @Param("sup_id") Long sup_id,
            @Param("year") Long year,
            @Param("month") Long month) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(SupRemitMapper.class).selectRemitByMonthAndSup(sup_id, year, month);
        }
    }

    /**
     * 插入汇款记录
     */
    public static void insertRemit(SupRemitDao dao) throws Exception{
        FXUtils.getMapper(factory, SupRemitMapper.class, SupRemitMapper::insertRemit, dao);
    }

    /**
     * 列表形式插入多个汇款记录
     */
    public static void insertRemitList(List<SupRemitDao> daos) throws Exception{
        FXUtils.getMapper(factory, SupRemitMapper.class, SupRemitMapper::insertRemitList, daos);
    }
}
