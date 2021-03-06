package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.dao.annual.AnnualDaoWrapper;
import cn.keepfight.qsmanager.dao.annual.InvoiceDao;
import cn.keepfight.qsmanager.dao.annual.RemitDao;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
import cn.keepfight.qsmanager.service.SupAnnualServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.ImageManager;
import cn.keepfight.widget.YearScrollPicker;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 支出年度对账表
 * Created by tom on 2017/9/4.
 */
public class OutcomeAnnualController implements ContentCtrl, Initializable {
    public VBox root;
    // 年度对账表部分
    @FXML
    private ChoiceBox<SupplyModel> an_sup_sel;

    public Button attachs;
    public Button btn_add_invoice;
    public Button btn_edit;
    public Button btn_add_remit;
    public Button btn_year_sel;
    private YearScrollPicker yearScrollPicker;

    @FXML
    private Button an_print;
    @FXML
    private TableView<AnnualDaoWrapper> anTable;

    @FXML
    private TableColumn<AnnualDaoWrapper, String> mon;
    @FXML
    private TableColumn<AnnualDaoWrapper, BigDecimal> total;

    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> billunit;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<Date>> billdate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> billtotal;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> rate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> ratetotal;

    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> remitunit;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> pattern;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<Date>> remitdate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> paytotal;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> note;

    @FXML
    private Label an_count_bf;
    @FXML
    private Label an_total_annu;
    @FXML
    private Label an_total_rate;
    @FXML
    private Label an_total_pay;
    @FXML
    private Label an_total_actu;

    // 子界面
    private Long data_year = 2017L;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        an_sup_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "全部供应商"));

        an_print.disableProperty().bind(an_sup_sel.getSelectionModel().selectedItemProperty().isNull());
        btn_edit.disableProperty().bind(an_sup_sel.getSelectionModel().selectedItemProperty().isNull()
                .or(anTable.getSelectionModel().selectedItemProperty().isNull()));
        btn_add_remit.disableProperty().bind(btn_edit.disableProperty());
        btn_add_invoice.disableProperty().bind(btn_edit.disableProperty());
        attachs.disableProperty().bind(an_print.disableProperty());

        yearScrollPicker = FXWidgetUtil.getYearPicker();
        yearScrollPicker.setOnClose(year -> {
            if (an_sup_sel.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
            QSApp.mainPane.reload(FXUtils.ps(
                    new Pair<>("year", year),
                    new Pair<>("sid", sid)
            ));
        });
        btn_year_sel.setOnAction(event -> yearScrollPicker.show(btn_year_sel, data_year));

//
//        // 添加表格转换器
        FXWidgetUtil.connectDecimalColumn(total, AnnualDaoWrapper::tradeTotalProperty);
        FXWidgetUtil.cellMoney(total);
        FXWidgetUtil.connect(mon, x -> x.monthProperty().asString().concat("月"));

        billunit.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getUnit).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billunit, x -> x);

        billdate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getDate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billdate, x -> FXUtils.stampToDate(x.getTime()));

        billtotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billtotal, FXUtils::deciToMoney);

        rate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getRate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(rate, FXUtils::decimalRateStr);

        ratetotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getRateTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(ratetotal, FXUtils::deciToMoney);

        /////////////////////////////////////////////////////////////////////////////
        // 汇款
        remitunit.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getUnit).collect(Collectors.toList())));
        FXWidgetUtil.cellList(remitunit, x -> x);

        pattern.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getMode).collect(Collectors.toList())));
        FXWidgetUtil.cellList(pattern, x -> x);

        note.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getNote).collect(Collectors.toList())));
        FXWidgetUtil.cellList(note, x -> x);

        remitdate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getDate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(remitdate, x -> FXUtils.stampToDate(x.getTime()));

        paytotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(paytotal, FXUtils::deciToMoney);


        //计算
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getTradeTotal, an_total_annu::setText);
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getInvoicesRateTotal, an_total_rate::setText);
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getRemitTotal, an_total_pay::setText);

        // 计算实际应付合计
        an_total_actu.textProperty().bind(new ObjectBinding<String>() {
            {
                this.bind(an_count_bf.textProperty(),
                        an_total_annu.textProperty(),
                        an_total_rate.textProperty(),
                        an_total_pay.textProperty());
            }

            @Override
            protected String computeValue() {
                BigDecimal bf = FXUtils.getDecimal(an_count_bf.getText(), new BigDecimal(0L));
                BigDecimal annu = FXUtils.getDecimal(an_total_annu.getText(), new BigDecimal(0L));
                BigDecimal rate = FXUtils.getDecimal(an_total_rate.getText(), new BigDecimal(0L));
                BigDecimal pay = FXUtils.getDecimal(an_total_pay.getText(), new BigDecimal(0L));
                return FXUtils.deciToMoney(annu.add(rate).add(bf).subtract(pay));
            }
        });

        an_sup_sel.setOnAction(event -> {
            Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
            QSApp.mainPane.reload(FXUtils.ps(
                    new Pair<>("year", data_year),
                    new Pair<>("sid", sid)
            ));
        });


        btn_edit.setOnAction(event -> {
            Long month = anTable.getSelectionModel().getSelectedItem().getMonth();
            Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
            QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ANNUAL_EDIT, FXUtils.ps(
                    new Pair<>("year", data_year),
                    new Pair<>("month", month),
                    new Pair<>("sid", sid)
            ));
        });
        // 双表进行编辑
        anTable.setRowFactory(tv -> {
            TableRow<AnnualDaoWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // 防止空双击
                if (an_sup_sel.getSelectionModel().isEmpty()) {
                    return;
                }
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    AnnualDaoWrapper rowData = row.getItem();
                    Long month = rowData.getMonth();
                    Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
                    QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ANNUAL_EDIT, FXUtils.ps(
                            new Pair<>("year", data_year),
                            new Pair<>("month", month),
                            new Pair<>("sid", sid)
                    ));
                }
            });
            return row;
        });

        attachs.setOnAction(event -> {
            SupplyModel x = an_sup_sel.getSelectionModel().getSelectedItem();
            String category = "OutcomeAnnual-" + x.getSerial() + "-" + yearScrollPicker.get();
            String title = "图片管理器：" + x.getSerial() + "-" + x.getName() + "-" + yearScrollPicker.get();
            try {
                ImageManager.newManager(category, title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 打印支持
        an_print.setOnAction(event -> {
            SupplyModel sup = an_sup_sel.getSelectionModel().getSelectedItem();
            Long year = yearScrollPicker.get();

            PrintSource source = new PrintSource();
            Long sid = sup == null ? null : sup.getId();
            source.setSup(sid);
            source.setYear(year);

//            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.YEAR_SUP, source));
            QSApp.mainPane.changeTo(
                    MainPaneList.PRINT_MANAGER,
                    FXUtils.ps(new Pair<>("selection", new PrintSelection(QSPrintType.YEAR_SUP, source))));
        });

        btn_add_invoice.setOnAction(event -> {
                    Long month = 5L;
                    if (anTable.getSelectionModel().getSelectedItem() != null) {
                        month = anTable.getSelectionModel().getSelectedItem().getMonth();
                    }
                    QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_INVOICE, FXUtils.ps(
                            new Pair<>("year", data_year),
                            new Pair<>("month", month),
                            new Pair<>("sup_id", an_sup_sel.getSelectionModel().getSelectedItem().getId())
                    ));
                }
        );
        btn_add_remit.setOnAction(event -> {
                    Long month = 5L;
                    if (anTable.getSelectionModel().getSelectedItem() != null) {
                        month = anTable.getSelectionModel().getSelectedItem().getMonth();
                    }
                    QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_REMIT, FXUtils.ps(
                            new Pair<>("year", data_year),
                            new Pair<>("month", month),
                            new Pair<>("sup_id", an_sup_sel.getSelectionModel().getSelectedItem().getId())
                    ));
                }
        );
    }

    @Override
    public void loaded() {
    }

    @Override
    public void showed(Properties params) {
        data_year = (Long) params.getOrDefault("year", 2017L);
        yearScrollPicker.set(data_year);
        btn_year_sel.setText(data_year + "年");
        anTable.getItems().clear();
        selectWithoutHandler(an_sup_sel, null);
        try {
            an_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!params.containsKey("sid")) {
            return;
        }
        Long sid = (Long) params.get("sid");
        for (SupplyModel x : an_sup_sel.getItems()) {
            if (x.getId().equals(sid)) {
                selectWithoutHandler(an_sup_sel, x);
                break;
            }
        }
        try {
            an_count_bf.setText(FXUtils.deciToMoney(SupAnnualServers.staticAnnualLeft(sid, data_year)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            anTable.getItems().setAll(
                    SupAnnualServers.staticAnnualMonByMonAndSup(sid, data_year)
                            .stream().map(AnnualDaoWrapper::new)
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("年度对账表");
    }

    private static <T> void selectWithoutHandler(ChoiceBox<T> box, T value) {
        Platform.runLater(() -> {
            EventHandler<ActionEvent> x = box.getOnAction();
            box.setOnAction(null);
            box.getSelectionModel().select(value);
            box.setOnAction(x);
        });
    }
}
