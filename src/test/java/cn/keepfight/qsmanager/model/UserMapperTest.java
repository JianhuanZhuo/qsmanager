package cn.keepfight.qsmanager.model;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/6/5.
 */
public class UserMapperTest {
    private static UserMapper mapper;

    @BeforeClass
    public static void beforeClass() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession session = sqlSessionFactory.openSession(true);
        mapper = session.getMapper(cn.keepfight.qsmanager.model.UserMapper.class);
    }

    @Test
    public void selectUserByID() throws Exception {
        mapper.selectUserByID(1);
    }

    @Test
    public void insertUser() throws Exception {
        User user = mapper.selectUserByID(1);
        long id = user.getId();
        mapper.insertUser(user);
        assertNotEquals(id, user.getId());
    }

    @Test
    public void testCheck() throws Exception {
        User user = mapper.selectUserByID(1);
        if (user != null) {
            assertNotNull(mapper.checkLegality(user));
        }

        user = new User();
        user.setPsw("dev");
        user.setAcc("dev");
        assertEquals(mapper.checkLegality(user).getUtype(), 0);

        user = new User();
        user.setPsw("dev");
        user.setAcc("dev2");
        assertNull(mapper.checkLegality(user));
    }

}