package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 收入管理控制器类
 * Created by tom on 2017/6/6.
 */
public class IncomeController implements ContentCtrl, Initializable {
    public TabPane tab_pane;
    @FXML
    private VBox root;

    @FXML
    private VBox deliveryListPane;
    @FXML
    private DeliveryListPaneController deliveryListPaneController;

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
    private TableColumn<CustAnnualMonModel, BigDecimal> rate;
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

    private CustAnnualAddController custAnnualAddController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 设置客户下拉转换器、年转换器
        an_cust_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "选择客户"));
        an_year_sel.setConverter(FXUtils.converter(x -> x + "年", "选择年份"));

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
                new SimpleObjectProperty<>(cellFeature.getValue().getRate()));
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

        rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.rateConverter()));


        an_cust_sel.setOnAction(event -> loadAnnu());
        an_year_sel.setOnAction(event -> loadAnnu());
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


        // 打印支持
        an_print.setOnAction(event -> {
            CustomModel selCust = an_cust_sel.getSelectionModel().getSelectedItem();
            Long year = an_year_sel.getSelectionModel().getSelectedItem();

            PrintSource source = new PrintSource();
            Long cid = selCust == null ? null : selCust.getId();
            source.setCust(cid);
            source.setYear(year);
            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.YEAR_CUST, source));
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
            custAnnualAddController = ViewPathUtil.loadViewForController("cust_annual_add.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        deliveryListPaneController.loaded();
    }

    @Override
    public void showed() {
        deliveryListPaneController.showed();
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void listDelivery(String serial) {
        deliveryListPaneController.listDelivery(serial);
    }
}
