package cn.keepfight.qsmanager.model;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by tom on 2017/6/5.
 */
public class SQLTest {
    static Connection cnt;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        cnt = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
    }

    @Ignore
    @Test
    public void testConnect() throws SQLException {
        Statement smt = cnt.createStatement();
        smt.execute("SELECT * FROM custom;");
    }

}
