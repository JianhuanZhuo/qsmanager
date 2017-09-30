package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.StuffMapper;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * 员工服务.
 * 使用静态方法提供服务
 * Created by tom on 2017/9/27.
 */
public abstract class StuffServices {

    private static SqlSessionFactory factory = SqlSessionServices.getFactory();

    public static List<StuffDao> selectAll() throws Exception {
//        return FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::selectAll);
        List<StuffDao> res =  FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::selectAll);
        res.forEach(x->{
            System.out.println(x.getName()+":"+x.getOperatorDao().getAuthority());
            System.out.println(x.getName()+":"+x.getOperatorDao().getAccount());
        });
        return res;
    }

    public static  StuffDao selectAllByID(Long id) throws Exception {
        return FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::selectByID, id);
    }

    public static  void update(StuffDao stuff) throws Exception {
        OperatorServices.update(stuff.getOperatorDao());
        FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::update, stuff);
    }

    public static  void insert(StuffDao stuff) throws Exception {
        OperatorServices.insert(stuff.getOperatorDao());
        FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::insert, stuff);
    }

    /**
     * 删除指定 ID 的员工
     */
    public static  void delete(Long id) throws Exception {
        OperatorServices.delete(selectAllByID(id).getOperatorDao());
        FXUtils.getMapper(factory, StuffMapper.class, StuffMapper::deleteByID, id);
    }

    /**
     * 删除指定员工
     */
    public static  void delete(StuffDao stuff) throws Exception {
        delete(stuff.getId());
    }
}
