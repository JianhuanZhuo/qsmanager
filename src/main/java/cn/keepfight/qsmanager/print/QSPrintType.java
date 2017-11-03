package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.print.*;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.print.PageOrientation;
import javafx.print.Paper;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * 打印件类型有：
 * <li>
 * <ol>送货单》》》客户》年份》月份》账单</ol>
 * <ol>收据单》》》客户》年份》月份》账单</ol>
 * <ol>安利专用送货单》》》！客户！》年份》月份》</ol>
 * <ol>客户月对账单</ol>
 * <ol>客户年度对账单</ol>
 * <ol>供应商月对账单</ol>
 * <ol>供应商年度对账单</ol>
 * </li>
 * Created by tom on 2017/6/26.
 */
public enum QSPrintType {
    DELIVERY("普通送货单 二分联", Paper.A4, PageOrientation.PORTRAIT, 25, "print_delivery.fxml", new PrintLocateDelivery(), 2),
    DELIVERY_SIMPLE("普通送货单(无金额) 二分联", Paper.A4, PageOrientation.PORTRAIT, 25, "print_delivery_simple.fxml", new PrintLocateDelivery(), 2),
    RECEIPT("普通收据单 二分联", Paper.A4, PageOrientation.PORTRAIT, 25, "print_receipt.fxml", new PrintLocateDelivery(), 2),
    DELIVERY_ANLI("安利专用送货单 二分联", Paper.A4, PageOrientation.PORTRAIT, 25, "print_anli.fxml", new PrintLocateDelivery(), 2),
    MON_CUST("客户月对账单 A4横向", Paper.A4, "print_mon_cust.fxml", new PrintLocateMonCust(), 1),
    MON_CUST_RATE("客户月对账单(发票) A4横向", Paper.A4, "print_mon_cust_rate.fxml", new PrintLocateMonCust(), 1),
    YEAR_CUST("客户年度对账单 A4横向", Paper.A4, "print_year_cust.fxml", new PrintLocateYearCust(), 1),
    MON_SUP("供应商月对账单 A4横向", Paper.A4, "print_mon_sup.fxml", new PrintLocateMonSup(), 1),
    YEAR_SUP("供应商年度对账单 A4横向", Paper.A4, "print_year_sup.fxml", new PrintLocateYearSup(), 1);

    private String desc;
    private Paper paper;
    private PageOrientation orientation;
    private double marginReqire; // 最小间距要求
    private String viewPath;
    private PrintSourceLocate locate;

    private PrintTemplate controller;

    private int folder;

    QSPrintType(String desc, Paper paper, String viewPath, PrintSourceLocate locate, int folder) {
        this(desc, paper, PageOrientation.LANDSCAPE, 25, viewPath, locate, folder);
    }

    QSPrintType(String desc, Paper paper, PageOrientation orientation, double marginReqire, String viewPath, PrintSourceLocate locate, int folder) {
        this.desc = desc;
        this.paper = paper;
        this.orientation = orientation;
        this.marginReqire = marginReqire;
        this.viewPath = viewPath;
        this.locate = locate;
        this.folder = folder;
    }

    public String getDesc() {
        return desc;
    }

    public Paper getPaper() {
        return paper;
    }

    public PageOrientation getOrientation() {
        return orientation;
    }

    public double getMarginReqire() {
        return marginReqire;
    }

    /**
     * 获得控制器进行操作的消费器，异步进行操作
     */
    public void getController(Consumer<PrintTemplate> action) {
        if (controller != null) {
            action.accept(controller);
            return;
        }
        if (!viewPath.equals("")) {
            Platform.runLater(() -> {
                controller = ViewPathUtil.loadViewForController(viewPath);
                action.accept(controller);

            });
        }
    }

    public PrintSourceLocate getLocate() {
        return locate;
    }

    public int getFolder() {
        return folder;
    }
}
