package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.SupAnnuMapper;
import cn.keepfight.qsmanager.Mapper.SupInvoiceMapper;
import cn.keepfight.qsmanager.Mapper.SupRemitMapper;
import cn.keepfight.qsmanager.dao.annual.SupAnnualDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class SupAnnualServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 指定供应商和年份选择对账表
     */
    public static List<SupAnnualDao> staticAnnualMonByMonAndSup(Long sid, Long year) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            List<SupAnnualDao> res = session.getMapper(SupAnnuMapper.class).staticAnnualMonByMonAndSup(sid, year);
            for (SupAnnualDao d : res) {
                d.setInvoice(session.getMapper(SupInvoiceMapper.class).selectInvoiceByMonthAndSup(sid, year, d.getMonth()));
                d.setRemit(session.getMapper(SupRemitMapper.class).selectRemitByMonthAndSup(sid, year, d.getMonth()));
            }
            return res;
        }
    }
}
