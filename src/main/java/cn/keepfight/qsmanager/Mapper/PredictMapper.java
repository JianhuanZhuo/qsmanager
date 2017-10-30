package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import java.util.List;

/**
 *
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
}
