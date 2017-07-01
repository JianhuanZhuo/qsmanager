package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.*;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 支出管理控制器
 * Created by tom on 2017/6/6.
 */
public class OutComeController implements ContentController {

    @FXML
    private VBox root;

    // 送货列表部分
    @FXML
    private ChoiceBox<SupplyModel> rec_sup_sel;
    @FXML
    private ChoiceBox<Long> rec_year_sel;
    @FXML
    private ChoiceBox<Long> rec_mon_sel;
    @FXML
    private Button rec_add;
    @FXML
    private Button rec_print_mon;

    @FXML
    private ListView<ReceiptModelFull> receiptFullList;

    // 年度对账表部分
    @FXML
    private ChoiceBox<SupplyModel> an_sup_sel;
    @FXML
    private ChoiceBox<Long> an_year_sel;
    @FXML
    private Button an_print;
    @FXML
    private TableView<SupAnnualMonModel> anTable;

    @FXML
    private TableColumn<SupAnnualMonModel, String> mon;
    @FXML
    private TableColumn<SupAnnualMonModel, String> total;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billunit;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billdate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billtotal;
    @FXML
    private TableColumn<SupAnnualMonModel, String> rate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> ratetotal;
    @FXML
    private TableColumn<SupAnnualMonModel, String> remitunit;
    @FXML
    private TableColumn<SupAnnualMonModel, String> pattern;
    @FXML
    private TableColumn<SupAnnualMonModel, String> remitdate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> paytotal;
    @FXML
    private TableColumn<SupAnnualMonModel, String> note;

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

    // 子控制器
    @FXML
    private VBox take;
    @FXML
    private OutComeTakeController takeController;
    @FXML
    private VBox pay;
    @FXML
    private OutComePayController payController;
    @FXML
    private VBox fac;
    @FXML
    private OutComeFacController facController;
    @FXML
    private HBox stat;
    @FXML
    private OutComeStatController statController;

    // 子界面
    private ReceiptAddController receiptAddController;
    private SupAnnualAddController supAnnualAddController;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadSelection();
        Platform.runLater(() -> {
            try {
                receiptAddController = ViewPathUtil.loadViewForController("receipt_add.fxml");
                supAnnualAddController = ViewPathUtil.loadViewForController("sup_annual_add.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void showed() {
    }

    @FXML
    public void initialize() {
        initUI();
        initAction();
    }

    private void initUI() {

        receiptFullList.setCellFactory(param->new ListCell<ReceiptModelFull>() {
                    @Override
                    protected void updateItem(ReceiptModelFull item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            try {
                                ReceiptController controller = ViewPathUtil.loadViewForController("receipt.fxml");
                                controller.fill(item, OutComeController.this);
                                setGraphic(controller.getRoot());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // 设置供应商下拉转换器、年转换器
        rec_sup_sel.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName(), "全部供应商"));
        rec_year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));
        an_sup_sel.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName(), "全部供应商"));
        an_year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));

        rec_mon_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        rec_mon_sel.getItems().add(null);
        rec_mon_sel.setConverter(FXUtils.converter(x->x+"月", "全部月份"));

        // 打印按钮使能
        rec_print_mon.disableProperty().bind(rec_mon_sel.getSelectionModel().selectedItemProperty().isNotNull()
                .and(rec_sup_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .and(rec_year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .not()
        );
        an_print.disableProperty().bind(an_sup_sel.getSelectionModel().selectedItemProperty().isNotNull()
                .and(an_year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .not()
        );


        // 添加表格转换器
        mon.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getMon().toString() + "月"));
        total.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getTotalStr()));
        billunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getBillunit()));
        billdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getBilldate())));
        billtotal.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getBilltotalStr()));
        rate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRateStr()));
        remitunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRemitunit()));
        pattern.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPattern()));
        remitdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getRemitdate())));
        paytotal.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPaytotalStr()));
        note.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getNote()));

        ratetotal.setCellValueFactory(param -> {
            String str;
            try {
                str = param.getValue().getRate().multiply(param.getValue().getBilltotal()).toString();
            } catch (Exception e) {
                // 乘法有错，则无视之
                str = "0";
            }
            return new SimpleStringProperty(str);
        });

        //计算
        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualMonModel::getTotal, an_total_annu::setText);
        FXWidgetUtil.calculate(anTable.getItems(),
                (model -> model.getRate().multiply(model.getBilltotal())),
                an_total_rate::setText);
        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualMonModel::getPaytotal, an_total_pay::setText);

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
                return annu.add(rate).subtract(bf).subtract(pay).toString();
            }
        });

    }

    private void initAction() {
        rec_add.setOnAction(event -> {
            Optional<ReceiptModelFull> op = CustomDialog.gen().build(receiptAddController);
            op.ifPresent(model -> {
                try {
                    QSApp.service.getReceiptService().insert(model);
                    loadReceipt();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("新增供应送货失败", "新增供应送货失败，请检查网络是否通畅");
                }
            });
        });

        // 监听选择以加载列表、年度对账表表格
        rec_sup_sel.setOnAction(event -> loadReceipt());
        rec_year_sel.setOnAction(event -> loadReceipt());
        rec_mon_sel.setOnAction(event -> loadReceipt());
        an_sup_sel.setOnAction(event -> loadAnnu());
        an_year_sel.setOnAction(event -> loadAnnu());

        // 双击原料表进行编辑
        anTable.setRowFactory(tv -> {
            TableRow<SupAnnualMonModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // 防止空双击
                if (an_year_sel.getSelectionModel().isEmpty() || an_sup_sel.getSelectionModel().isEmpty()) {
                    return;
                }
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SupAnnualMonModel rowData = row.getItem();
                    Optional<SupAnnualMonModel> op = CustomDialog.gen().build(supAnnualAddController, rowData);
                    op.ifPresent(model -> {
                        rowData.setTotal(model.getTotal());
                        rowData.setBillunit(model.getBillunit());
                        rowData.setBilldate(model.getBilldate());
                        rowData.setBilltotal(model.getBilltotal());
                        rowData.setRate(model.getRate());
                        rowData.setRemitunit(model.getRemitunit());
                        rowData.setPattern(model.getPattern());
                        rowData.setRemitdate(model.getRemitdate());
                        rowData.setPaytotal(model.getPaytotal());
                        rowData.setNote(model.getNote());
                        try {
                            QSApp.service.getSupAnnualService().updateMon(rowData);
                            loadAnnu();
                        } catch (Exception e) {
                            //@TODO
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
            return row;
        });
    }


    /**
     * 加载供应商和年份下拉选择的选项
     */
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<Long> years = QSApp.service.getReceiptService().selectYear();
                rec_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
                rec_year_sel.getItems().setAll(years);
                an_sup_sel.getItems().setAll(rec_sup_sel.getItems());
                an_year_sel.getItems().setAll(years);
                facController.setYearSelection(years);
                payController.setYearSelection(years);
                statController.setYearSelection(years);
                takeController.setYearSelection(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 所谓事件处理器，加载供应送货数据
     */
    private void loadReceipt() {
        if (rec_sup_sel.getSelectionModel().isEmpty() ||
                rec_year_sel.getSelectionModel().isEmpty() ||
                rec_mon_sel.getSelectionModel().isEmpty()) {
            return;
        }
        Long sid = rec_sup_sel.getSelectionModel().getSelectedItem().getId();
        Long year = rec_year_sel.getSelectionModel().getSelectedItem();
        Long mon = rec_mon_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                receiptFullList.getItems().setAll(FXCollections.observableList(QSApp.service.getReceiptService().selectAll(sid, year, mon)));
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
        if (an_sup_sel.getSelectionModel().isEmpty() ||
                an_year_sel.getSelectionModel().isEmpty()) {
            return;
        }
        System.out.println("get sid and year");
        Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
        Long year = an_year_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                System.out.println("in runlater!");
                SupAnnualModelFull full = QSApp.service.getSupAnnualService().selectAnnual(sid, year);
                System.out.println("full mon size: " + full.getMons().size());
                anTable.getItems().setAll(
                        FXCollections.observableList(full.getMons()));
                //@TODO 添加对结余的支持和附件
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 从供应送货列表中删除指定的对象
     */
    void deleteSelected(ReceiptModelFull modelFull) {
        Platform.runLater(() -> {
            try {
                QSApp.service.getReceiptService().delete(new ReceiptModel(modelFull));
                receiptFullList.getItems().remove(modelFull);
            } catch (Exception e) {
                WarningBuilder.build("删除失败，请检查网络是否通畅！");
            }
        });
    }

    void updateReceipt(ReceiptModelFull modelFull) {
        Optional<ReceiptModelFull> op = CustomDialog.gen().build(receiptAddController, modelFull);
        op.ifPresent(model -> {
            try {
                model.setId(modelFull.getId());
                QSApp.service.getReceiptService().update(model);
                loadReceipt();
            } catch (Exception e1) {
                e1.printStackTrace();
                WarningBuilder.build("新增供应送货失败", "新增供应送货失败，请检查网络是否通畅");
            }
        });
    }
}
