package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.annual.SupRemitDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupRemitMapper {

    /**
     * 指定供应商 ID、年份、月份选择汇款记录
     */
    List<SupRemitDao> selectRemitByMonthAndSup(
            @Param("sup_id") Long sup_id,
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 插入汇款记录
     */
    void insertRemit(SupRemitDao dao) throws Exception;

    /**
     * 列表形式插入多个汇款记录
     */
    void insertRemitList(List<SupRemitDao> daos) throws Exception;
}
