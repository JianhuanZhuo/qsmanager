package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.CustInvoiceMapper;
import cn.keepfight.qsmanager.Mapper.SupInvoiceMapper;
import cn.keepfight.qsmanager.dao.annual.InvoiceDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class CustInvoiceServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 指定供应商 ID、年份、月份选择发票记录
     */
    public static List<InvoiceDao> selectInvoiceByMonthAndSup(Long sup_id, Long year, Long month) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(CustInvoiceMapper.class).selectInvoiceByMonthAndSup(sup_id, year, month);
        }
    }

    /**
     * 插入发票记录
     */
    public static void insertInvoice(InvoiceDao dao) throws Exception {
        FXUtils.getMapper(factory, CustInvoiceMapper.class, CustInvoiceMapper::insertInvoice, dao);
    }

    /**
     * 列表形式插入多个发票记录
     */
    public static void insertInvoiceList(List<InvoiceDao> daos) throws Exception {
        FXUtils.getMapper(factory, CustInvoiceMapper.class, CustInvoiceMapper::insertInvoiceList, daos);
    }

    /**
     * 指定 id 删除发票记录
     */
    public static void deleteInvoiceByID(Long id) throws Exception {
        FXUtils.getMapper(factory, CustInvoiceMapper.class, CustInvoiceMapper::deleteInvoiceByID, id);
    }
}
