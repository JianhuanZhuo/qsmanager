package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.CustRemitMapper;
import cn.keepfight.qsmanager.Mapper.SupRemitMapper;
import cn.keepfight.qsmanager.dao.annual.RemitDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class CustRemitServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 指定供应商 ID、年份、月份选择汇款记录
     */
    public static List<RemitDao> selectRemitByMonthAndSup(Long sup_id, Long year, Long month) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(CustRemitMapper.class).selectRemitByMonthAndSup(sup_id, year, month);
        }
    }

    /**
     * 插入汇款记录
     */
    public static void insertRemit(RemitDao dao) throws Exception{
        FXUtils.getMapper(factory, CustRemitMapper.class, CustRemitMapper::insertRemit, dao);
    }

    /**
     * 列表形式插入多个汇款记录
     */
    public static void insertRemitList(List<RemitDao> daos) throws Exception{
        FXUtils.getMapper(factory, CustRemitMapper.class, CustRemitMapper::insertRemitList, daos);
    }

    /**
     * 指定 id 删除汇款记录
     */
    public static void deleteRemitByID(Long id) throws Exception{
        FXUtils.getMapper(factory, CustRemitMapper.class, CustRemitMapper::deleteRemitByID, id);
    }
}
