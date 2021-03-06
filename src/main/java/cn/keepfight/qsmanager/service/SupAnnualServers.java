package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.SupAnnuMapper;
import cn.keepfight.qsmanager.Mapper.SupInvoiceMapper;
import cn.keepfight.qsmanager.Mapper.SupRemitMapper;
import cn.keepfight.qsmanager.dao.annual.AnnualDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.math.BigDecimal;
import java.util.List;

public class SupAnnualServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 指定供应商和年份选择对账表
     */
    public static List<AnnualDao> staticAnnualMonByMonAndSup(Long sid, Long year) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            List<AnnualDao> res = session.getMapper(SupAnnuMapper.class).staticAnnualMonByMonAndSup(sid, year);
            for (AnnualDao d : res) {
                d.setInvoice(session.getMapper(SupInvoiceMapper.class).selectInvoiceByMonthAndSup(sid, year, d.getMonth()));
                d.setRemit(session.getMapper(SupRemitMapper.class).selectRemitByMonthAndSup(sid, year, d.getMonth()));
            }
            return res;
        }
    }
    /**
     *
     * 指定供应商和年份统计，该年份之前的
     */
    public static BigDecimal staticAnnualLeft(Long sid, Long year) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(SupAnnuMapper.class).staticAnnualLeft(sid, year);
        }
    }
}
