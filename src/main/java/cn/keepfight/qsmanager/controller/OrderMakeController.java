package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Properties;

/**
 * 新增产品界面控制器
 * Created by tom on 2017/6/7.
 */
public class OrderMakeController implements ContentCtrl {

    @FXML
    private VBox root;

    @FXML
    private TextField serial;
    @FXML
    private DatePicker rdate;
    @FXML
    private ChoiceBox<CustomModel> cid;

    @FXML
    private Button item_add;
    @FXML
    private Button item_del;

    // 表格部分
    @FXML
    private TableView<OrderItemModel> table;
    @FXML
    private TableColumn<OrderItemModel, String> tab_name;
    @FXML
    private TableColumn<OrderItemModel, String> tab_serial;
    @FXML
    private TableColumn<OrderItemModel, String> tab_detail;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_price;
    @FXML
    private TableColumn<OrderItemModel, Long> tab_pack;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_num;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_total;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_rate;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_ratetotal;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_totallWithRate;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_rebate;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_allrebate;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_delifee;
    @FXML
    private TableColumn<OrderItemModel, BigDecimal> tab_actPay;
    @FXML
    private Label s_num;
    @FXML
    private Label s_rate;
    @FXML
    private Label s_rebate;
    @FXML
    private Label s_total;

    @FXML
    private Button item_ok;
    @FXML
    private Button item_cancel;
    @FXML
    private Button item_rate;
    @FXML
    private Button item_rebate;

    private OrderModelFull model;

    public static final String NO_SERIAL = "订单号待生成";
    // 子界面
    private OrderItemAddController addController = ViewPathUtil.loadViewForController("order_item_add.fxml");

    private StringProperty titlePropert = new SimpleStringProperty("订单信息");

    @FXML
    public void initialize() {

        FXUtils.limitLength(serial, 30);

        // 设置为当前时间
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        rdate.setValue(localDate);

        FXWidgetUtil.connectStrColumn(tab_name, OrderItemModel::nameProperty);
        FXWidgetUtil.connectStrColumn(tab_serial, OrderItemModel::serialProperty);
        FXWidgetUtil.connectStrColumn(tab_detail, OrderItemModel::detailProperty);
        FXWidgetUtil.connectDecimalColumn(tab_price, OrderItemModel::priceProperty);
        FXWidgetUtil.connectDecimalColumn(tab_num, OrderItemModel::numProperty);
        FXWidgetUtil.connectDecimalColumn(tab_total, OrderItemModel::takeTotalProperty);
        FXWidgetUtil.connectDecimalColumn(tab_rate, OrderItemModel::rateProperty);
        FXWidgetUtil.connectDecimalColumn(tab_ratetotal, OrderItemModel::rateProperty);
        FXWidgetUtil.connectDecimalColumn(tab_totallWithRate, OrderItemModel::totalWithRateProperty);
        FXWidgetUtil.connectDecimalColumn(tab_rebate, OrderItemModel::rebateProperty);
        FXWidgetUtil.connectDecimalColumn(tab_allrebate, OrderItemModel::rebateTotalProperty);
        FXWidgetUtil.connectDecimalColumn(tab_delifee, OrderItemModel::delifeeProperty);
        FXWidgetUtil.connectDecimalColumn(tab_actPay, OrderItemModel::actPayTotalProperty);
        tab_pack.setCellValueFactory(param -> param.getValue().packProperty().asObject());
        FXWidgetUtil.cellMoney(tab_price, tab_num, tab_total, tab_ratetotal, tab_totallWithRate,
                tab_rebate, tab_allrebate, tab_delifee, tab_actPay);
        FXWidgetUtil.cellRate(tab_rate);

        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRateTotal, s_rate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRebateTotal, s_rebate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);

        // 新增明细按钮
        item_add.setOnMouseClicked(e -> {
            addController.setCid(cid.getSelectionModel().getSelectedItem().getId());
            CustomDialog.gen().build(addController).ifPresent(table.getItems()::add);
            FXWidgetUtil.compute(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);
        });

        // 删除明细按钮
        item_del.setOnMouseClicked(event -> table.getItems().remove(table.getSelectionModel().getSelectedIndex()));

        // 删除按钮有效性
        item_del.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        item_add.disableProperty().bind(cid.getSelectionModel().selectedItemProperty().isNull());

        // 设置SID下来文字转换
        cid.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName()));

        // 禁用手动输入
        rdate.getEditor().setDisable(true);

        // 双击原料表进行编辑
        FXWidgetUtil.doubleToEdit(table, () -> addController, (x, y) -> {
            x.update(y);
            FXWidgetUtil.compute(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);
        });

//        tab_ratetotal.visibleProperty().bind(tab_rate.visibleProperty());
//        tab_totallWithRate.visibleProperty().bind(tab_rate.visibleProperty());
//        tab_rebate.visibleProperty().bind(tab_rate.visibleProperty());
//        tab_allrebate.visibleProperty().bind(tab_rate.visibleProperty());
//        tab_delifee.visibleProperty().bind(tab_rate.visibleProperty());
//        tab_actPay.visibleProperty().bind(tab_rate.visibleProperty());
//        item_rate.visibleProperty().bind(tab_rate.visibleProperty());
//        item_rebate.visibleProperty().bind(tab_rate.visibleProperty());
//        item_rate.setVisible(false);
//        item_rebate.setVisible(false);

        item_ok.setOnAction(event -> {
            model.setOrderItemModels(table.getItems());
            model.setOrderdate(Date.valueOf(rdate.getValue()).getTime());
            model.setCid(cid.getSelectionModel().getSelectedItem().getId());
            model.setCust(cid.getSelectionModel().getSelectedItem().getSerial()
                    + "-" + cid.getSelectionModel().getSelectedItem().getName());
            try {
                QSApp.service.getOrderService().update(model);
                QSApp.mainPane.backNav();
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("更新失败，请检查网络是否可用");
            }
        });

        item_cancel.setOnAction(event -> {
            // @TODO 取消时需要消除新建插入的东西
            QSApp.mainPane.backNav();
        });
    }

    private void loadCust() {
        Platform.runLater(() -> {
            try {
                cid.getItems().setAll(QSApp.service.getCustomService().selectAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        cid.getSelectionModel().clearSelection();
        table.getItems().clear();
        // 加载列表
        loadCust();

        Long id = (Long) params.get("id");
        if (id == null) {
            // @TODO 插入一个新的空的
            throw new RuntimeException("id can't be empty!");
        }
        new Thread(() -> {
            try {
                model = QSApp.service.getOrderService().selectById(id);
                serial.setText(model.getSerial());
                Platform.runLater(() -> {
                    for (CustomModel cust : cid.getItems()) {
                        if (cust.getId().equals(model.getCid())) {
                            cid.getSelectionModel().select(cust);
                            break;
                        }
                    }
                });
                rdate.setValue(FXUtils.stampToLocalDate(model.getOrderdate()));
                table.getItems().setAll(model.getOrderItemModels());
            } catch (Exception e) {
                // @TODO 这里需要做出警告
                e.printStackTrace();
            }
        }).start();

        Boolean more = (Boolean) params.getOrDefault("more", false);
//        tab_rate.setVisible(true);
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.spBinding(titlePropert);
    }
}
