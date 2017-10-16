package cn.keepfight.qsmanager.model;

import cn.keepfight.qsmanager.QSApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
//        cnt = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
    }

    @Ignore
    @Test
    public void testConnect() throws SQLException {
        Statement smt = cnt.createStatement();
        smt.execute("SELECT * FROM custom;");
    }

    @Test
    public void testSQL() throws Exception {
        ObservableList<ReceiptModelFull> receipts = FXCollections.observableList(QSApp.service.getReceiptService().selectAll(6L, 2017L, 6L));
        receipts.forEach(e->{
            System.out.println(e.getId());
            System.out.println(e.getSid());
            System.out.println(e.getSerial());
            System.out.println(e.getRdate());
        });
    }

    @Test
    public void testIfNull() throws Exception {
        ObservableList<ReceiptModelFull> receipts = FXCollections.observableList(QSApp.service.getReceiptService().selectAll(6L, 2017L, null));
        receipts.forEach(e->{
            System.out.println(e.getId());
            System.out.println(e.getSid());
            System.out.println(e.getSerial());
            System.out.println(e.getRdate());
        });
    }


}
