package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.PredictMapper;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import cn.keepfight.utils.FXUtils;
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
}
