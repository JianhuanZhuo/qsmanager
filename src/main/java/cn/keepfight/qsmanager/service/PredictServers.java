package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.PredictMapper;
import cn.keepfight.qsmanager.Mapper.SalaryMapper;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * 预算表服务
 * Created by 卓建欢 on 2017/10/29.
 */
public abstract class PredictServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    /**
     * 选择预算中供应商可收入列表
     */
    public static List<PredictTradeDao> selectOutcomePredictLeft() throws Exception{
        return FXUtils.getMapper(factory, PredictMapper.class, PredictMapper::selectOutcomePredictLeft);
    }

    /**
     * 选择预算中客户可收入列表
     */
    public static List<PredictTradeDao> selectIncomePredictLeft() throws Exception{
        return FXUtils.getMapper(factory, PredictMapper.class, PredictMapper::selectIncomePredictLeft);
    }

    /**
     * 选择指定年月的预算历史
     */
    public static PredictHistoryDao selectPredictHistory(Long year,  Long month) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(PredictMapper.class).selectPredictHistory(year, month);
        }
    }

    /**
     * 删除指定年月的预算历史
     */
    public static void delPredictHistory(Long year,  Long month) throws Exception{
        try (SqlSession session = factory.openSession(true)) {
            session.getMapper(PredictMapper.class).delPredictHistory(year, month);
        }
    }

    /**
     * 选择全部的预算历史
     */
    public static List<PredictHistoryDao> selectAllPredictHistory() throws Exception{
        return FXUtils.getMapper(factory, PredictMapper.class, PredictMapper::selectAllPredictHistory);
    }

    /**
     * 替换历史
     */
    public static void replaceHistory(PredictHistoryDao dao)throws Exception{
        FXUtils.getMapper(factory, PredictMapper.class, PredictMapper::replaceHistory, dao);
    }
}
