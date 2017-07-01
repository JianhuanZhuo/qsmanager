package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 收入管理控制器类
 * Created by tom on 2017/6/6.
 */
public class IncomeController implements ContentController, Initializable {
    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<CustomModel> cust_sel;
    @FXML
    private ChoiceBox<Long> year_sel;
    @FXML
    private ChoiceBox<Long> mon_sel;
    @FXML
    private TextField order_sel;
    @FXML
    private Button load;
    @FXML
    private Button print;
    @FXML
    private ListView<DeliveryModelFull> deliveryList;

    @FXML
    private ChoiceBox<CustomModel> an_cust_sel;
    @FXML
    private ChoiceBox<Long> an_year_sel;
    @FXML
    private Button an_print;
    @FXML
    private TableView<CustAnnualMonModel> anTable;
    @FXML
    private TableColumn<CustAnnualMonModel, String> mon;
    @FXML
    private TableColumn<CustAnnualMonModel, String> total;
    @FXML
    private TableColumn<CustAnnualMonModel, String> billunit;
    @FXML
    private TableColumn<CustAnnualMonModel, String> billdate;
    @FXML
    private TableColumn<CustAnnualMonModel, String> billtotal;
    @FXML
    private TableColumn<CustAnnualMonModel, String> rate;
    @FXML
    private TableColumn<CustAnnualMonModel, String> ratetotal;
    @FXML
    private TableColumn<CustAnnualMonModel, String> remitunit;
    @FXML
    private TableColumn<CustAnnualMonModel, String> pattern;
    @FXML
    private TableColumn<CustAnnualMonModel, String> remitdate;
    @FXML
    private TableColumn<CustAnnualMonModel, String> paytotal;
    @FXML
    private TableColumn<CustAnnualMonModel, String> note;


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
    private Label attach;

    private DeliveryAddController deliveryAddController;
    private CustAnnualAddController custAnnualAddController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 设置客户下拉转换器、年转换器
        cust_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "全部客户"));
        year_sel.setConverter(FXUtils.converter(x -> x + "年", "全部年份"));

        an_cust_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "选择客户"));
        an_year_sel.setConverter(FXUtils.converter(x -> x + "年", "选择年份"));

        mon_sel.setConverter(FXUtils.converter(x -> x + "月", "全部月份"));
        mon_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        mon_sel.getItems().add(null);


        // 添加表格转换器
        mon.setCellValueFactory(cellFeature ->
                new SimpleStringProperty("" + cellFeature.getValue().getMon() + "月"));
        total.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getTotal())));
        billunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getBillunit()));
        billdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getBilldate())));
        billtotal.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getBilltotal())));
        rate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getRate())));
        remitunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRemitunit()));
        pattern.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPattern()));
        remitdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getRemitdate())));
        paytotal.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getPaytotal())));
        note.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getNote()));
        ratetotal.setCellValueFactory(param ->
                new SimpleStringProperty(FXUtils.decimalStr(param.getValue().getRateTotal())));


        FXUtils.limitNum(order_sel, 10, 0, false, null);

        load.setOnAction(e -> loadDeliverys());
        an_cust_sel.setOnAction(event -> loadAnnu());
        an_year_sel.setOnAction(event -> loadAnnu());

        // 设置列表单元构造器
        deliveryList.setCellFactory(x -> new DeliveryListCell(IncomeController.this));

        print.disableProperty().bind(
                cust_sel.getSelectionModel().selectedItemProperty().isNotNull()
                        .and(year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                        .and(mon_sel.getSelectionModel().selectedItemProperty().isNotNull())
                        .not()
        );
        an_print.disableProperty().bind(an_cust_sel.getSelectionModel().selectedItemProperty().isNotNull()
                .and(an_year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .not()
        );

        // 自动计算
        FXWidgetUtil.calculate(anTable.getItems(), CustAnnualMonModel::getTotal, an_total_annu::setText);
        FXWidgetUtil.calculate(anTable.getItems(), CustAnnualMonModel::getRateTotal, an_total_rate::setText);
        FXWidgetUtil.calculate(anTable.getItems(), CustAnnualMonModel::getPaytotal, an_total_pay::setText);

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


        // 双击原料表进行编辑
        anTable.setRowFactory(tv -> {
            TableRow<CustAnnualMonModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // 防止空双击
                if (an_year_sel.getSelectionModel().isEmpty() || an_cust_sel.getSelectionModel().isEmpty()) {
                    return;
                }
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    CustAnnualMonModel rowData = row.getItem();
                    Optional<CustAnnualMonModel> op = CustomDialog.gen().build(custAnnualAddController, rowData);
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
                            QSApp.service.getCustAnnualService().updateMon(rowData);
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

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {
        // 加载选择列表
        loadSelection();
        try {
            deliveryAddController = ViewPathUtil.loadViewForController("delivery_add.fxml");
            custAnnualAddController = ViewPathUtil.loadViewForController("cust_annual_add.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showed() {

    }

    void updateDelivery(DeliveryModelFull modelFull) {
        Optional<DeliveryModelFull> op = CustomDialog.gen().build(deliveryAddController, modelFull);
        op.ifPresent(model -> {
            try {
                model.setId(modelFull.getId());
                QSApp.service.getDeliveryService().update(model);
                loadDeliverys();
            } catch (Exception e1) {
                e1.printStackTrace();
                WarningBuilder.build("修改送货单失败", "修改送货单失败，请检查网络是否通畅");
            }
        });
    }

    /**
     * 从送货列表中删除指定的对象
     */
    void deleteSelected(DeliveryModelFull modelFull) {
        Platform.runLater(() -> {
            try {
                QSApp.service.getDeliveryService().delete(modelFull.get());
                deliveryList.getItems().remove(modelFull);
            } catch (Exception e) {
                WarningBuilder.build("删除失败，请检查网络是否通畅！");
            }
        });
    }

    /**
     * 所谓事件处理器，加载对账表
     */
    private void loadDeliverys() {
        CustomModel selCust = cust_sel.getSelectionModel().getSelectedItem();
        Long cid = selCust == null ? null : selCust.getId();
        Long year = year_sel.getSelectionModel().getSelectedItem();
        Long month = mon_sel.getSelectionModel().getSelectedItem();
        String orderSerial = (order_sel.getText() == null) || order_sel.getText().trim().equals("") ?
                null : order_sel.getText();

        Platform.runLater(() -> {
            try {
                DeliverySelection s = new DeliverySelection(cid, year, month, orderSerial);
                List<DeliveryModelFull> ls = QSApp.service.getDeliveryService().selectAll(s);
                deliveryList.getItems().setAll(ls);
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
        if (an_cust_sel.getSelectionModel().isEmpty() ||
                an_year_sel.getSelectionModel().isEmpty()) {
            return;
        }
        System.out.println("get sid and year");
        Long sid = an_cust_sel.getSelectionModel().getSelectedItem().getId();
        Long year = an_year_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                System.out.println("in runlater!");
                anTable.getItems().setAll(FXCollections.observableList(QSApp.service.getCustAnnualService().selectAnnual(sid, year).getMons()));
                //@TODO 添加对结余的支持和附件
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 加载下拉列表
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<CustomModel> custs = QSApp.service.getCustomService().selectAll();
                List<Long> years = QSApp.service.getOrderService().selectYear();

                an_cust_sel.getItems().setAll(custs);
                an_year_sel.getItems().setAll(new ArrayList<>(years));

                custs.add(null);
                cust_sel.getItems().setAll(FXCollections.observableList(custs));
                years.add(null);
                year_sel.getItems().setAll(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发货列表条目
     */
    private static class DeliveryListCell extends ListCell<DeliveryModelFull> {
        DeliveryItemController controller;
        IncomeController incomeController;

        DeliveryListCell(IncomeController incomeController) {
            this.incomeController = incomeController;
            try {
                controller = ViewPathUtil.loadViewForController("delivery.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateItem(DeliveryModelFull item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                setGraphic(controller.getRoot());
                controller.fill(item, incomeController);
            }
        }
    }
}
