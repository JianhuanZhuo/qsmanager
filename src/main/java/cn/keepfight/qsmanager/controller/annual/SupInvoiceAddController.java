package cn.keepfight.qsmanager.controller.annual;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.annual.InvoiceDao;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.service.CustInvoiceServers;
import cn.keepfight.qsmanager.service.SupInvoiceServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.MonthPicker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class SupInvoiceAddController implements Initializable, ContentCtrl {

    public VBox root;

    public Label lab_sup;
    public Button btn_month_sel;

    public TextField unit;
    public DatePicker date;
    public TextField total;
    public TextField rate;
    public TextField rateTotal;

    public Button ok;
    public Button cancel;

    private MonthPicker monthPicker = FXWidgetUtil.getMonthPicker();
    private Long year;
    private Long month;
    private Long sup_id;
    private boolean sup_view = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FXWidgetUtil.rateBiMultiply(rateTotal, total, rate);

        btn_month_sel.setOnAction(event -> monthPicker.show(btn_month_sel, new Pair<>(year, month)));
        monthPicker.setOnClose(ym -> {
            year = ym.getKey();
            month = ym.getValue();
            btn_month_sel.setText(year + "年" + month + "月");
        });

        ok.setOnAction(event -> {
            if (rate.getText().equals("")
                    || total.getText().equals("")
                    || date.getValue() == null) {
                WarningBuilder.build("部分数据未填写，无法添加记录！");
                return;
            }
            BigDecimal r;
            BigDecimal t;
            try {
                r = new BigDecimal(rate.getText().trim().replace("%", "").replace(",", ""));
                if (rate.getText().contains("%")) {
                    r = r.movePointLeft(2);
                }
                t = FXUtils.getDecimal(total);
            } catch (Exception e) {
                WarningBuilder.build("金额或税率数据填写错误，无法添加记录！");
                return;
            }

            InvoiceDao dao = new InvoiceDao();
            dao.setSup_id(sup_id);
            dao.setYear(year);
            dao.setMonth(month);
            dao.setUnit(unit.getText());
            dao.setDate(Date.valueOf(date.getValue()));
            dao.setTotal(t);
            dao.setRate(r);
            new Thread(() -> {
                try {
                    if (sup_view) {
                        SupInvoiceServers.insertInvoice(dao);
                    }else {
                        CustInvoiceServers.insertInvoice(dao);
                    }
                    QSApp.mainPane.backNav();
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("添加失败，请检查网络是否可用！");
                }
            }).run();

            // 记录填写记录
            FXWidgetUtil.addDefaultList(
                    new Pair<>("prefer.supan.billunit", unit.getText()),
                    new Pair<>("prefer.supan.rate", rate.getText())
            );
        });
        cancel.setOnAction(event -> QSApp.mainPane.backNav());

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(unit, "prefer.supan.billunit"),
                new Pair<>(rate, "prefer.supan.rate")
        );
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public void showed(Properties params) {
        if (params.containsKey("sup_view")){
             sup_view = (boolean)params.get("sup_view");
        }
    }

    @Override
    public void showedAfter(Properties params) {
        if (!params.containsKey("sup_id")) {
            WarningBuilder.build("参数错误！无指定客户，请刷新后再添加");
            params.keySet().forEach(x -> System.out.print("-" + x));
            QSApp.mainPane.backNav();
        }

        year = (Long) params.getOrDefault("year", 2017L);
        month = (Long) params.getOrDefault("month", 1L);
        sup_id = (Long) params.get("sup_id");
        monthPicker.set(new Pair<>(year, month));
        btn_month_sel.setText(year + "年" + month + "月");
        new Thread(() -> {
            try {
                if (sup_view){
                    SupplyModel sup = QSApp.service.getSupplyService().selectByID(sup_id);
                    Platform.runLater(()->lab_sup.setText(sup.getSerial() + "-" + sup.getName()));
                }else{
                    CustomModel cust = QSApp.service.getCustomService().selectAllByID(sup_id);
                    Platform.runLater(()->lab_sup.setText(cust.getSerial() + "-" + cust.getName()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).run();
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("新增发票");
    }
}
