package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.ImageMapper;
import cn.keepfight.qsmanager.Mapper.PredictMapper;
import cn.keepfight.qsmanager.dao.ImageDao;
import cn.keepfight.qsmanager.dao.ImageListDao;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * 图片管理服务
 * Created by 卓建欢 on 2017/10/29.
 */
public abstract class ImageServers {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    public static List<ImageListDao> selectByCate(String cate) throws Exception {
        return FXUtils.getMapper(factory, ImageMapper.class, ImageMapper::selectByCate, cate);
    }

    public static String getImage(Long id) throws Exception {
        return FXUtils.getMapper(factory, ImageMapper.class, ImageMapper::getImage, id);
    }

    public static void updateName(Long id, String pname) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            session.getMapper(ImageMapper.class).updateName(id, pname);
        }
    }

    public static void updateNote(Long id, String note) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            session.getMapper(ImageMapper.class).updateNote(id, note);
        }
    }

    public static void del(Long id) throws Exception {
        FXUtils.getMapper(factory, ImageMapper.class, ImageMapper::del, id);
    }

    public static void insert(ImageDao dao) throws Exception {
        FXUtils.getMapper(factory, ImageMapper.class, ImageMapper::insert, dao);
    }
}
