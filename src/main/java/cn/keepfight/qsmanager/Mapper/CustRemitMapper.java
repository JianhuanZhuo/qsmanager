package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.annual.RemitDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustRemitMapper {

    /**
     * 指定供应商 ID、年份、月份选择汇款记录
     */
    List<RemitDao> selectRemitByMonthAndSup(
            @Param("cust_id") Long cust_id,
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 插入汇款记录
     */
    void insertRemit(RemitDao dao) throws Exception;

    /**
     * 列表形式插入多个汇款记录
     */
    void insertRemitList(List<RemitDao> daos) throws Exception;

    /**
     * 指定 id 删除汇款记录
     */
    void deleteRemitByID(Long id) throws Exception;
}
