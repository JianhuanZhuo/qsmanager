package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.tax.TaxDao;
import cn.keepfight.qsmanager.dao.tax.TaxInvoiceDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 税
 * Created by 卓建欢 on 2017/10/26.
 */
public interface TaxMapper {

    /**
     * 根据年月选择指定的税赋抵扣
     */
    TaxDao selectByMonth(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    List<TaxInvoiceDao> selectInvoiceListByTid(Long tid) throws Exception;

    /**
     * 插入一条新的税赋记录
     */
    void insertTax(TaxDao dao) throws Exception;

    /**
     * 插入一条新的发票记录
     */
    void insertTaxInvoice(TaxInvoiceDao dao) throws Exception;

    /**
     * 插入指定 ID 的发票
     */
    void deleteTaxInvoice(Long invoiceID) throws Exception;

    /**
     * 清空指定税 ID 的所有发票
     */
    void clearTaxInvoiceByTid(Long taxID) throws Exception;

    /**
     * 更新税赋记录
     */
    void updateTax(TaxDao dao) throws Exception;
}
