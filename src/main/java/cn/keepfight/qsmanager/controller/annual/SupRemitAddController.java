package cn.keepfight.qsmanager.controller.annual;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.annual.RemitDao;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.service.CustRemitServers;
import cn.keepfight.qsmanager.service.SupRemitServers;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.MonthPicker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class SupRemitAddController implements Initializable, ContentCtrl {

    public VBox root;

    public Label lab_sup;
    public Button btn_month_sel;

    public TextField unit;
    public DatePicker date;
    public TextField total;

    public Button ok;
    public Button cancel;
    public TextField mode;
    public TextArea note;

    private MonthPicker monthPicker = FXWidgetUtil.getMonthPicker();
    private Long year;
    private Long month;
    private Long sup_id;
    private boolean sup_view = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        btn_month_sel.setOnAction(event -> monthPicker.show(btn_month_sel, new Pair<>(year, month)));
        monthPicker.setOnClose(ym -> {
            year = ym.getKey();
            month = ym.getValue();
            btn_month_sel.setText(year + "年" + month + "月");
        });

        ok.setOnAction(event -> {
            if (total.getText().equals("") || date.getValue() == null) {
                WarningBuilder.build("部分数据未填写，无法添加记录！");
                return;
            }
            BigDecimal t;
            try {
                t = new BigDecimal(total.getText().replace(",", ""));
            } catch (Exception e) {
                WarningBuilder.build("金额数据填写错误，无法添加记录！");
                return;
            }

            RemitDao dao = new RemitDao();
            dao.setSup_id(sup_id);
            dao.setYear(year);
            dao.setMonth(month);
            dao.setUnit(unit.getText());
            dao.setMode(mode.getText());
            dao.setDate(Date.valueOf(date.getValue()));
            dao.setTotal(t);
            dao.setNote(note.getText());
            new Thread(() -> {
                try {
                    if (sup_view) {
                        SupRemitServers.insertRemit(dao);
                    } else {
                        CustRemitServers.insertRemit(dao);
                    }
                    QSApp.mainPane.backNav();
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("添加失败，请检查网络是否可用！");
                }
            }).run();

            // 记录填写记录
            FXWidgetUtil.addDefaultList(
                    new Pair<>("prefer.sup.remit.unit", unit.getText()),
                    new Pair<>("prefer.sup.remit.mode", mode.getText())
            );
        });
        cancel.setOnAction(event -> QSApp.mainPane.backNav());

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(unit, "prefer.sup.remit.unit"),
                new Pair<>(mode, "prefer.sup.remit.mode")
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
        if (params.containsKey("sup_view")) {
            sup_view = (boolean) params.get("sup_view");
        }
    }

    @Override
    public void showedAfter(Properties params) {
        if (!params.containsKey("sup_id")) {
            WarningBuilder.build("参数错误！无指定供应商，请刷新后再添加");
            QSApp.mainPane.backNav();
        }

        year = (Long) params.getOrDefault("year", 2017L);
        month = (Long) params.getOrDefault("month", 1L);
        sup_id = (Long) params.get("sup_id");
        monthPicker.set(new Pair<>(year, month));
        btn_month_sel.setText(year + "年" + month + "月");
        try {
            if (sup_view) {
                SupplyModel sup = QSApp.service.getSupplyService().selectByID(sup_id);
                Platform.runLater(() -> lab_sup.setText(sup.getSerial() + "-" + sup.getName()));
            } else {
                CustomModel cust = QSApp.service.getCustomService().selectAllByID(sup_id);
                Platform.runLater(() -> lab_sup.setText(cust.getSerial() + "-" + cust.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("新增汇款");
    }
}
