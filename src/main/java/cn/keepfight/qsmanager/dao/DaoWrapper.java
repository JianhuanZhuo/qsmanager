package cn.keepfight.qsmanager.dao;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 指定参数原生数据 Dao 对象的包装数据类型接口
 * Created by 卓建欢 on 2017/9/25.
 * @param <D> 原生数据类型
 */
public interface DaoWrapper<D>  {

    /**
     * 包装原生数据 dao 对象
     * @param data 原生 dao 数据对象
     */
    void wrap(D data);

    /**
     * 从包装类型对象中抽取出原生数据对象
     * @return 原生数据对象
     */
    D get();
}
