package cn.keepfight.qsmanager.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * SQL 会话服务
 * Created by tom on 2017/9/27.
 */
public abstract class SqlSessionServices {

    private static SqlSessionFactory factory;

    static {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis.xml");
        } catch (IOException e) {
            // 要是这个也没读出来，那说明没得玩了
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(reader);
    }

    public static SqlSessionFactory getFactory() {
        return factory;
    }
}
