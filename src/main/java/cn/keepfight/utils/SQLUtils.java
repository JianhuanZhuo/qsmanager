package cn.keepfight.utils;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Created by 卓建欢 on 2017/10/19.
 */
public abstract class SQLUtils {


    public static <T, R> R getMapperSession(SqlSession session, Class<T> clazz, FXUtils.ConsumerTR<T, R> consumer) throws Exception {
        return consumer.accept(session.getMapper(clazz));
    }

    public static <T, P> void getMapperSession(SqlSession session, Class<T> clazz, FXUtils.ConsumerT<T, P> consumer, P p) throws Exception {
        consumer.accept(session.getMapper(clazz), p);
    }

    public static <T, R, P> R getMapperSession(SqlSession session, Class<T> clazz, FXUtils.ConsumerTRP<T, R, P> consumer, P p) throws Exception {
        return consumer.accept(session.getMapper(clazz), p);
    }
}
