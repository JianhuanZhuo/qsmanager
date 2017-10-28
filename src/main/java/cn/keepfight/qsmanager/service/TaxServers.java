package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.SupInvoiceMapper;
import cn.keepfight.qsmanager.Mapper.TaxMapper;
import cn.keepfight.qsmanager.Mapper.UserMapper;
import cn.keepfight.qsmanager.dao.tax.TaxDao;
import cn.keepfight.qsmanager.dao.tax.TaxInvoiceDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public abstract class TaxServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 根据年月选择指定的税赋抵扣
     */
    public static TaxDao selectByMonth(Long year, Long month) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            TaxDao res = session.getMapper(TaxMapper.class).selectByMonth(year, month);
            if (res != null)
                res.setInvoices(session.getMapper(TaxMapper.class).selectInvoiceListByTid(res.getId()));
            return res;
        }
    }

    /**
     * 插入一条新的税赋记录
     */
    public static void insertTax(TaxDao dao) throws Exception {
        FXUtils.getMapper(factory, TaxMapper.class, TaxMapper::insertTax, dao);
    }

    /**
     * 插入一条新的发票记录
     */
    public static void insertTaxInvoice(TaxInvoiceDao dao) throws Exception {
        FXUtils.getMapper(factory, TaxMapper.class, TaxMapper::insertTaxInvoice, dao);
    }

    /**
     * 插入指定 ID 的发票
     */
    public static void deleteTaxInvoice(Long invoiceID) throws Exception {
        FXUtils.getMapper(factory, TaxMapper.class, TaxMapper::deleteTaxInvoice, invoiceID);
    }

    /**
     * 清空指定税 ID 的所有发票
     */
    public static void clearTaxInvoiceByTid(Long taxID) throws Exception {
        FXUtils.getMapper(factory, TaxMapper.class, TaxMapper::clearTaxInvoiceByTid, taxID);
    }

    /**
     * 更新税赋记录
     */
    public static void updateTax(TaxDao dao) throws Exception {
        FXUtils.getMapper(factory, TaxMapper.class, TaxMapper::updateTax, dao);
    }
}
