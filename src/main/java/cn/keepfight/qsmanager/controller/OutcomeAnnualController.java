package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.dao.annual.SupAnnualDaoWrapper;
import cn.keepfight.qsmanager.dao.annual.SupInvoiceDao;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
import cn.keepfight.qsmanager.service.SupAnnualServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.YearScrollPicker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
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

    public Button btn_add_invoice;
    public Button btn_add_remit;
    public Button btn_year_sel;
    private YearScrollPicker yearScrollPicker;

    @FXML
    private Button an_print;
    @FXML
    private TableView<SupAnnualDaoWrapper> anTable;

    @FXML
    private TableColumn<SupAnnualDaoWrapper, String> mon;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, BigDecimal> total;

    @FXML
    private TableColumn<SupAnnualDaoWrapper, List<String>> billunit;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, List<Date>> billdate;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, List<BigDecimal>> billtotal;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, List<BigDecimal>> rate;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, List<BigDecimal>> ratetotal;

    @FXML
    private TableColumn<SupAnnualDaoWrapper, String> remitunit;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, String> pattern;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, Date> remitdate;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, BigDecimal> paytotal;
    @FXML
    private TableColumn<SupAnnualDaoWrapper, String> note;

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
    @FXML
    private Label an_attach;

    // 子界面
    private SupAnnualAddController supAnnualAddController;
    private Long year = 2017L;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        an_sup_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "全部供应商"));

        an_print.disableProperty().bind(an_sup_sel.getSelectionModel().selectedItemProperty().isNull());

        yearScrollPicker = FXWidgetUtil.getYearPicker();
        yearScrollPicker.setOnClose(year -> {
        });
        btn_year_sel.setOnAction(event -> yearScrollPicker.show(btn_year_sel, year));

//
//        // 添加表格转换器
        FXWidgetUtil.connectDecimalColumn(total, SupAnnualDaoWrapper::tradeTotalProperty);
        FXWidgetUtil.connectNum(mon, SupAnnualDaoWrapper::monthProperty);
        FXWidgetUtil.cellMoney(total);

        billunit.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        SupInvoiceDao::getUnit).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billunit, x->x);

        billdate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        SupInvoiceDao::getDate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billdate, x->FXUtils.stampToDate(x.getTime()));

        billtotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        SupInvoiceDao::getTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billtotal, FXUtils::deciToMoney);

        rate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        SupInvoiceDao::getRate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(rate, FXUtils::decimalRateStr);

        ratetotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        SupInvoiceDao::getRateTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(ratetotal, FXUtils::deciToMoney);

//        FXWidgetUtil.connectDecimal(paytotal, SupAnnualDaoWrapper::getPaytotal);
//        FXWidgetUtil.connectDecimal(ratetotal, SupAnnualDaoWrapper::getBilltotal);
//        mon.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(cellFeature.getValue().getMon().toString() + "月"));
//        billunit.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(cellFeature.getValue().getBillunit()));
//        billdate.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getBilldate())));
//        remitunit.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(cellFeature.getValue().getRemitunit()));
//        pattern.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(cellFeature.getValue().getPattern()));
//        remitdate.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getRemitdate())));
//        note.setCellValueFactory(cellFeature ->
//                new SimpleStringProperty(cellFeature.getValue().getNote()));
//
//        rate.setCellValueFactory(cellFeature ->
//                new SimpleObjectProperty<>(cellFeature.getValue().getRate()));
//        rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.rateConverter()));

//
//        //计算
//        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualDaoWrapper::getTotal, an_total_annu::setText);
//        FXWidgetUtil.calculate(anTable.getItems(),
//                (model -> model.getRate().multiply(model.getBilltotal())),
//                an_total_rate::setText);
//        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualDaoWrapper::getPaytotal, an_total_pay::setText);
//
//        // 计算实际应付合计
//        an_total_actu.textProperty().bind(new ObjectBinding<String>() {
//            {
//                this.bind(an_count_bf.textProperty(),
//                        an_total_annu.textProperty(),
//                        an_total_rate.textProperty(),
//                        an_total_pay.textProperty());
//            }
//
//            @Override
//            protected String computeValue() {
//                BigDecimal bf = FXUtils.getDecimal(an_count_bf.getText(), new BigDecimal(0L));
//                BigDecimal annu = FXUtils.getDecimal(an_total_annu.getText(), new BigDecimal(0L));
//                BigDecimal rate = FXUtils.getDecimal(an_total_rate.getText(), new BigDecimal(0L));
//                BigDecimal pay = FXUtils.getDecimal(an_total_pay.getText(), new BigDecimal(0L));
//                return annu.add(rate).subtract(bf).subtract(pay).toString();
//            }
//        });

//
//        an_sup_sel.setOnAction(event -> loadAnnu());
//
//        // 双击原料表进行编辑
//        anTable.setRowFactory(tv -> {
//            TableRow<SupAnnualDaoWrapper> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                // 防止空双击
//                if (an_sup_sel.getSelectionModel().isEmpty()) {
//                    return;
//                }
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//                    SupAnnualDaoWrapper rowData = row.getItem();
//                    Optional<SupAnnualDaoWrapper> op = CustomDialog.gen().build(supAnnualAddController, rowData);
//                    op.ifPresent(model -> {
//                        rowData.setTotal(model.getTotal());
//                        rowData.setBillunit(model.getBillunit());
//                        rowData.setBilldate(model.getBilldate());
//                        rowData.setBilltotal(model.getBilltotal());
//                        rowData.setRate(model.getRate());
//                        rowData.setRemitunit(model.getRemitunit());
//                        rowData.setPattern(model.getPattern());
//                        rowData.setRemitdate(model.getRemitdate());
//                        rowData.setPaytotal(model.getPaytotal());
//                        rowData.setNote(model.getNote());
//                        try {
//                            QSApp.service.getSupAnnualService().updateMon(rowData);
//                            loadAnnu();
//                        } catch (Exception e) {
//                            //@TODO
//                            throw new RuntimeException(e);
//                        }
//                    });
//                }
//            });
//            return row;
//        });

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

        btn_add_invoice.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_INVOICE, FXUtils.ps(
                new Pair<>("year", year),
                new Pair<>("month", 5L),
                new Pair<>("sup_id", an_sup_sel.getSelectionModel().getSelectedItem().getId())
        )));
        btn_add_remit.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_REMIT, FXUtils.ps(
                new Pair<>("year", year),
                new Pair<>("month", 5L),
                new Pair<>("sup_id", an_sup_sel.getSelectionModel().getSelectedItem().getId())
        )));
    }

    @Override
    public void loaded() {

        loadSelection();
//        Platform.runLater(() -> {
//            try {
//                supAnnualAddController = ViewPathUtil.loadViewForController("SupInvoiceAddController.fxml");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    @Override
    public void showed(Properties params) {
        try {
            anTable.getItems().setAll(
                    SupAnnualServers.staticAnnualMonByMonAndSup(4L, 2017L)
                            .stream().map(SupAnnualDaoWrapper::new)
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("年度对账表");
    }


    /**
     * 加载供应商和年份下拉选择的选项
     */
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                an_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 所谓事件处理器，加载对账表
     */
    private void loadAnnu() {
        System.out.println("load annu");
        if (an_sup_sel.getSelectionModel().isEmpty()) {
            return;
        }
        Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
        Long year = yearScrollPicker.get();

//        new Thread(() -> {
//            try {
//                SupAnnualModelFull full = QSApp.service.getSupAnnualService().selectAnnual(sid, year);
//                Platform.runLater(() -> anTable.getItems().setAll(FXCollections.observableList(full.getMons())));
//                //@TODO 添加对结余的支持和附件
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).run();
    }
}
