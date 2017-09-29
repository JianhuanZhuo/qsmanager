package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.OperatorMapper;
import cn.keepfight.qsmanager.Mapper.UserMapper;
import cn.keepfight.qsmanager.dao.OperatorDao;
import cn.keepfight.qsmanager.dao.UserDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * 系统操作者服务.
 * 使用静态方法提供服务
 * Created by tom on 2017/9/27.
 */
public abstract class OperatorServices {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    public static List<OperatorDao> selectAll() throws Exception {
        return FXUtils.getMapper(factory, OperatorMapper.class, OperatorMapper::selectAll);
    }

    public static OperatorDao selectAllByID(Long id) throws Exception {
        return FXUtils.getMapper(factory, OperatorMapper.class, OperatorMapper::selectByID, id);
    }

    public static void update(OperatorDao operator) throws Exception {
        UserServices.update(operator.getUserDao());
        FXUtils.getMapper(factory, OperatorMapper.class, OperatorMapper::update, operator);
    }

    public static void insert(OperatorDao operator) throws Exception {
        UserServices.insert(operator.getUserDao());
        FXUtils.getMapper(factory, OperatorMapper.class, OperatorMapper::insert, operator);
    }

    /**
     * 删除指定 ID 的系统操作者
     */
    public static void delete(Long id) throws Exception {
        UserServices.delete(selectAllByID(id).getUserDao());
        FXUtils.getMapper(factory, OperatorMapper.class, OperatorMapper::deleteByID, id);
    }

    /**
     * 删除指定系统操作者
     */
    public static void delete(OperatorDao operator) throws Exception {
        delete(operator.getId());
    }


    /**
     * 根据指定口令检查系统操作者的合法性
     *
     * @return 若账号密码合法返回口令对应的系统操作者，否则返回 null
     */
    public static OperatorDao checkLegality(String account, String password) throws Exception {
        try (SqlSession session = factory.openSession(true)) {
            return session.getMapper(OperatorMapper.class).checkLegality(account, password);
        }
    }
}
