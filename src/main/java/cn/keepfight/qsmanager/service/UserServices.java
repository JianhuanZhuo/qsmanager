package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.UserMapper;
import cn.keepfight.qsmanager.dao.UserDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * 用户服务.
 * 使用静态方法提供服务
 * Created by tom on 2017/9/27.
 */
public abstract class UserServices {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    public static List<UserDao> selectAll() throws Exception {
        return FXUtils.getMapper(factory, UserMapper.class, UserMapper::selectAll);
    }

    public static  UserDao selectAllByID(Long id) throws Exception {
        return FXUtils.getMapper(factory, UserMapper.class, UserMapper::selectByID, id);
    }

    public static  void update(UserDao custom) throws Exception {
        FXUtils.getMapper(factory, UserMapper.class, UserMapper::update, custom);
    }

    public static  void insert(UserDao custom) throws Exception {
        FXUtils.getMapper(factory, UserMapper.class, UserMapper::insert, custom);
    }

    /**
     * 删除指定 ID 的用户
     */
    public static  void delete(Long id) throws Exception {
        FXUtils.getMapper(factory, UserMapper.class, UserMapper::deleteByID, id);
    }

    /**
     * 删除指定用户
     */
    public static  void delete(UserDao user) throws Exception {
        delete(user.getId());
    }
}
