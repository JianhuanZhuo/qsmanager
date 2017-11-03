package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预测 Mapper
 * Created by 卓建欢 on 2017/10/29.
 */
public interface PredictMapper {

    /**
     * 选择预算中供应商可收入列表
     */
    List<PredictTradeDao> selectOutcomePredictLeft() throws Exception;

    /**
     * 选择预算中客户可收入列表
     */
    List<PredictTradeDao> selectIncomePredictLeft() throws Exception;

    /**
     * 选择指定年月的预算历史
     */
    PredictHistoryDao selectPredictHistory(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 删除指定年月的预算历史
     */
    void delPredictHistory(
            @Param("year") Long year,
            @Param("month") Long month) throws Exception;

    /**
     * 选择全部的预算历史
     */
    List<PredictHistoryDao> selectAllPredictHistory() throws Exception;

    /**
     * 替换历史
     */
    void replaceHistory(PredictHistoryDao dao) throws Exception;
}
