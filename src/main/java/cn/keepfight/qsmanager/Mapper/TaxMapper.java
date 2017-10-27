package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.tax.TaxDao;
import org.apache.ibatis.annotations.Param;

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
            @Param("month") Long month)throws Exception;

    /**
     * 插入一条新的税赋记录
     */
    void insertTax(TaxDao dao) throws Exception;

    /**
     * 更新税赋记录
     */
    void updateTax(TaxDao dao) throws Exception;
}
