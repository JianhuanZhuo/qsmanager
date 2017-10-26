package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.annual.InvoiceDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustInvoiceMapper {

    /**
     * 指定客户 ID、年份、月份选择发票记录
     */
    List<InvoiceDao> selectInvoiceByMonthAndSup(
            @Param("cust_id") Long cust_id,
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 插入发票记录
     */
    void insertInvoice(InvoiceDao dao) throws Exception;

    /**
     * 列表形式插入多个发票记录
     */
    void insertInvoiceList(List<InvoiceDao> daos) throws Exception;

    /**
     * 指定 id 删除发票记录
     */
    void deleteInvoiceByID(Long id) throws Exception;
}
