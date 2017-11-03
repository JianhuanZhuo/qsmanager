package cn.keepfight.qsmanager.controller.tax;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.tax.TaxDao;
import cn.keepfight.qsmanager.dao.tax.TaxInvoiceDao;
import cn.keepfight.qsmanager.dao.tax.TaxInvoiceDaoWrapper;
import cn.keepfight.qsmanager.service.TaxServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.MonthPicker;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import sun.awt.windows.ThemeReader;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaxController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_ym;
    public Button btn_save_param;
    public TextField tf_p1;
    public TextField tf_p2;
    public TextField tf_p3;
    public TextField tf_p4;
    public TextField tf_p5;
    public TextField tf_p6;

    public TextField tf_a;
    public TextField tf_b;
    public TextField tf_c;
    public TextField tf_d;
    public TextField tf_e;
    public TextField tf_f;
    public TextField tf_g;
    public TextField tf_h;
    public TextField tf_k;
    public TextField tf_j;

    public Button btn_out_load;
    public Button btn_out_add;
    public Button btn_out_del;
    public TableView<TaxInvoiceDaoWrapper> table_out;
    public TableColumn<TaxInvoiceDaoWrapper, Number> tab_out_serial;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_out_unit;
    public TableColumn<TaxInvoiceDaoWrapper, BigDecimal> tab_out_total;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_out_note;

    public Button btn_in_add;
    public Button btn_in_del;
    public TableView<TaxInvoiceDaoWrapper> table_in;
    public TableColumn<TaxInvoiceDaoWrapper, Number> tab_in_serial;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_in_unit;
    public TableColumn<TaxInvoiceDaoWrapper, BigDecimal> tab_in_total;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_in_note;
    public Label tax_warn;
    private Long data_year;
    private Long data_month;
    private MonthPicker monthPicker = FXWidgetUtil.getMonthPicker();
    private TaxDao dao;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("发票抵扣计算");
    }

    @Override
    public void showed(Properties params) {
        LocalDate now = FXUtils.stampToLocalDate();
        data_year = (Long) params.getOrDefault("year", (long) now.getYear());
        data_month = (Long) params.getOrDefault("month", (long) now.getMonthValue());
        btn_ym.setText(data_year + "年" + data_month + "月");

        try {
            dao = TaxServers.selectByMonth(data_year, data_month);
            if (dao == null) {
                dao = new TaxDao();
                dao.setYear(data_year);
                dao.setMonth(data_month);
                dao.setP1(new BigDecimal(35).movePointLeft(3));
                dao.setP2(new BigDecimal(117).movePointLeft(2));
                dao.setP3(new BigDecimal(17).movePointLeft(2));
                dao.setP4(new BigDecimal(794).movePointLeft(3));
                dao.setP5(new BigDecimal(46).movePointLeft(3));
                dao.setP6(new BigDecimal(940));
                dao.setTotal(new BigDecimal(0));
                TaxServers.insertTax(dao);
            }
            Platform.runLater(() -> {
                tf_p1.setText(FXUtils.decimalStr(dao.getP1()));
                tf_p2.setText(FXUtils.decimalStr(dao.getP2()));
                tf_p3.setText(FXUtils.decimalStr(dao.getP3()));
                tf_p4.setText(FXUtils.decimalStr(dao.getP4()));
                tf_p5.setText(FXUtils.decimalStr(dao.getP5()));
                tf_p6.setText(FXUtils.decimalStr(dao.getP6()));

                if (dao.getInvoices() != null) {
                    table_out.getItems().setAll(
                            dao.getInvoices().stream()
                                    .filter(x -> x.getCategory().equals(0L))
                                    .map(TaxInvoiceDaoWrapper::new)
                                    .collect(Collectors.toList())
                    );
                    table_in.getItems().setAll(
                            dao.getInvoices().stream()
                                    .filter(x -> x.getCategory().equals(1L))
                                    .map(TaxInvoiceDaoWrapper::new)
                                    .collect(Collectors.toList())
                    );
                } else {
                    table_out.getItems().clear();
                    table_in.getItems().clear();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_out_serial.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));
        FXWidgetUtil.connect(tab_out_unit, TaxInvoiceDaoWrapper::unitProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_total, TaxInvoiceDaoWrapper::totalProperty);
        FXWidgetUtil.connect(tab_out_note, TaxInvoiceDaoWrapper::noteProperty);

        tab_in_serial.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));
        FXWidgetUtil.connect(tab_in_unit, TaxInvoiceDaoWrapper::unitProperty);
        FXWidgetUtil.connectDecimalColumn(tab_in_total, TaxInvoiceDaoWrapper::totalProperty);
        FXWidgetUtil.connect(tab_in_note, TaxInvoiceDaoWrapper::noteProperty);

        FXWidgetUtil.cellMoney(tab_in_total, tab_out_total);

        btn_ym.setOnAction(event -> monthPicker.show(btn_ym, new Pair<>(data_year, data_month)));
        monthPicker.setOnClose(ym -> {
            data_year = ym.getKey();
            data_month = ym.getValue();
            btn_ym.setText(data_year + "年" + data_month + "月");
            QSApp.mainPane.reload(FXUtils.ps(
                    new Pair<>("year", data_year),
                    new Pair<>("month", data_month)
            ));
        });

        btn_out_add.setOnAction(event -> {
            Optional<TaxInvoiceDao> x = getTaxInvoice(dao.getId(), 0L);
            x.ifPresent(invoiceDao -> new Thread(() -> {
                try {
                    TaxServers.insertTaxInvoice(invoiceDao);
                    System.out.println("invoiceDao.getId():" + invoiceDao.getId());
                    Platform.runLater(() -> table_out.getItems().add(new TaxInvoiceDaoWrapper(invoiceDao)));
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("添加失败！请检查网络是否可用");
                }
            }).start());
        });

        btn_in_add.setOnAction(event -> {
            Optional<TaxInvoiceDao> x = getTaxInvoice(dao.getId(), 1L);
            x.ifPresent(dao -> new Thread(() -> {
                try {
                    TaxServers.insertTaxInvoice(dao);
                    Platform.runLater(() -> table_in.getItems().add(new TaxInvoiceDaoWrapper(dao)));
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("添加失败！请检查网络是否可用");
                }
            }).start());
        });

        btn_out_del.setOnAction(event -> {
            try {
                TaxInvoiceDaoWrapper select = table_out.getSelectionModel().getSelectedItem();
                TaxServers.deleteTaxInvoice(select.get().getId());
                table_out.getItems().remove(select);
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("添加失败！请检查网络是否可用");
            }
        });

        btn_in_del.setOnAction(event -> {
            try {
                TaxInvoiceDaoWrapper select = table_in.getSelectionModel().getSelectedItem();
                TaxServers.deleteTaxInvoice(select.get().getId());
                table_in.getItems().remove(select);
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("添加失败！请检查网络是否可用");
            }
        });

        btn_save_param.setOnAction(event -> saveParam(tf_k.getText()));
        tf_k.textProperty().addListener((observable, oldValue, newValue) -> saveParam(newValue));

        FXWidgetUtil.calculate(table_out.getItems(), TaxInvoiceDaoWrapper::getTotal, tf_a::setText);
        FXWidgetUtil.simpleTriOper(tf_b, (x, y) -> x.divide(y, 2, BigDecimal.ROUND_UP), BigDecimal::multiply, tf_a, tf_p2, tf_p3, FXUtils::deciToMoney);
        FXWidgetUtil.simpleBiSub(tf_c, tf_a, tf_b);
        FXWidgetUtil.calculate(table_in.getItems(), TaxInvoiceDaoWrapper::getTotal, tf_d::setText);
        FXWidgetUtil.simpleBiMultiply(tf_e, tf_a, tf_p4);
        FXWidgetUtil.simpleBiSub(tf_f, tf_d, tf_e);
        FXWidgetUtil.simpleBiSub(tf_g, tf_a, tf_d);
        FXWidgetUtil.simpleTriOper(tf_h, (x, y) -> x.divide(y, 2, BigDecimal.ROUND_UP), BigDecimal::multiply, tf_g, tf_p2, tf_p3, FXUtils::deciToMoney);
        FXWidgetUtil.simpleTriOper(tf_k, BigDecimal::multiply, BigDecimal::add, tf_a, tf_p5, tf_p6);
        FXWidgetUtil.simpleBiOper(tf_j, (x, y) -> {
            if (y.equals(new BigDecimal(0))) {
                return new BigDecimal(0);
            } else {
                return x.divide(y, 5, BigDecimal.ROUND_UP);
            }
        }, tf_h, tf_c);

        tax_warn.textProperty().bind(new StringBinding() {
            {
                bind(tf_j.textProperty(), tf_p1.textProperty());
            }
            @Override
            protected String computeValue() {
                try {
                    BigDecimal a = new BigDecimal(tf_j.getText());
                    BigDecimal b = new BigDecimal(tf_p1.getText());
                    BigDecimal c = new BigDecimal("0.03");
                    if (a.compareTo(b) >= 0){
                        return "（注意：税赋已超）";
                    }else if (a.compareTo(c)<0){
                        return "（注意：税赋未达标）";
                    }
                    return "";
                } catch (Exception e) {
                    return "（注意：税赋未达标）";
                }
            }
        });
    }

    private Optional<TaxInvoiceDao> getTaxInvoice(Long tid, Long category) {
        Dialog<TaxInvoiceDao> dialog = new Dialog<>();
        dialog.setTitle("添加发票");
        dialog.setHeaderText("添加发票需要填写下面的信息，点击确定后添加");

        ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField unit = new TextField();
        unit.setPromptText("开票单位");
        TextField total = new TextField();
        unit.setPromptText("含税金额");
        TextField note = new TextField();
        unit.setPromptText("备注信息");

        grid.add(new Label("开票单位:"), 0, 0);
        grid.add(unit, 1, 0);
        grid.add(new Label("含税金额:"), 0, 1);
        grid.add(total, 1, 1);
        grid.add(new Label("备注信息:"), 0, 2);
        grid.add(note, 1, 2);

        Node loginButton = dialog.getDialogPane().lookupButton(okButtonType);
        loginButton.setDisable(true);
        total.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                new BigDecimal(newValue);
                loginButton.setDisable(false);
            } catch (Exception e) {
                loginButton.setDisable(true);
            }
        });


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                TaxInvoiceDao dao = new TaxInvoiceDao();
                dao.setTid(tid);
                dao.setCategory(category);
                dao.setUnit(unit.getText());
                dao.setTotal(new BigDecimal(total.getText()));
                dao.setNote(note.getText());
                return dao;
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void saveParam(String k){
        if (dao==null){
            return;
        }
        new Thread(()->{
            dao.setYear(data_year);
            dao.setMonth(data_month);
            dao.setP1(FXUtils.getDecimal(tf_p1));
            dao.setP2(FXUtils.getDecimal(tf_p2));
            dao.setP3(FXUtils.getDecimal(tf_p3));
            dao.setP4(FXUtils.getDecimal(tf_p4));
            dao.setP5(FXUtils.getDecimal(tf_p5));
            dao.setP6(FXUtils.getDecimal(tf_p6));
            dao.setTotal(FXUtils.getDecimal(k));
            try {
                TaxServers.updateTax(dao);
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("保存失败，请检查网络是否可用");
            }
        }).start();
    }
}
