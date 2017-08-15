package cn.keepfight.utils;

import cn.keepfight.qsmanager.QSApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import org.junit.Test;

import javax.print.attribute.standard.OrientationRequested;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/6/5.
 */
public class ViewPathUtilTest {
    @Test
    public void getFrameView() throws Exception {
        System.out.println(ViewPathUtil.getFrameView("main.fxml"));
    }

    @Test
    public void loadView() throws Exception {

            if ("70盒/箱×28箱/板×13板".matches("\\d+盒/箱×\\d+箱/板×\\d+板(\\+\\d+)?")){
                System.out.println("ok");
            }
    }

    @Test
    public void getLoader() throws Exception {
    }

    @Test
    public void loadViewForController() throws Exception {
    }

    @Test
    public void testTime() throws IOException {
//        System.out.println(FXUtils.stampToLocalDateTime(1497996816000L));
//        System.out.println(Instant.ofEpochMilli(1497996816000L).atZone(ZoneId.systemDefault()));

        PageLayout p = Printer.getDefaultPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, 25, 25, 25, 25);
        System.out.println(p.getTopMargin());
        System.out.println(p.getBottomMargin());
        System.out.println(p.getLeftMargin());
        System.out.println("p.getRightMargin()"+p.getRightMargin());

        System.out.println(p.getPrintableHeight());
        System.out.println("p.getPrintableWidth()"+p.getPrintableWidth());
    }

    @Test
    public void testHH() throws Exception {
        QSApp.service.getCustomService().selectAll().forEach(p-> System.out.println(p.getSerial()));
    }

    @Test
    public void testOrder() throws Exception {
//        QSApp.service.getOrderService().selectYear().forEach(System.out::println);

        System.out.println( new BigDecimal("10.0000000"));
        System.out.println( new BigDecimal("10.0000000").stripTrailingZeros());
        System.out.println( new BigDecimal("10.0000000").stripTrailingZeros().toPlainString());
        System.out.println( new BigDecimal("10.0000000").toPlainString());
    }
}