package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.annual.SupInvoiceDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupInvoiceMapper {

    /**
     * 指定供应商 ID、年份、月份选择发票记录
     */
    List<SupInvoiceDao> selectInvoiceByMonthAndSup(
            @Param("sup_id") Long sup_id,
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 插入发票记录
     */
    void insertInvoice(SupInvoiceDao dao) throws Exception;

    /**
     * 列表形式插入多个发票记录
     */
    void insertInvoiceList(List<SupInvoiceDao> daos) throws Exception;
}
