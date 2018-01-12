package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

/**
 * 新增产品界面控制器
 * Created by tom on 2017/6/7.
 */
public class OrderAddController implements DialogContent<OrderModelFull> {

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
    private TableColumn<OrderItemModel, String> tab_price;
    @FXML
    private TableColumn<OrderItemModel, String> tab_pack;
    @FXML
    private TableColumn<OrderItemModel, String> tab_num;
    @FXML
    private TableColumn<OrderItemModel, String> tab_total;
    @FXML
    private TableColumn<OrderItemModel, String> tab_rate;
    @FXML
    private TableColumn<OrderItemModel, String> tab_ratetotal;
    @FXML
    private TableColumn<OrderItemModel, String> tab_totallWithRate;
    @FXML
    private TableColumn<OrderItemModel, String> tab_rebate;
    @FXML
    private TableColumn<OrderItemModel, String> tab_allrebate;
    @FXML
    private TableColumn<OrderItemModel, String> tab_delifee;
    @FXML
    private TableColumn<OrderItemModel, String> tab_actPay;
    @FXML
    private Label s_num;
    @FXML
    private Label s_rate;
    @FXML
    private Label s_rebate;
    @FXML
    private Label s_total;

    public static final String NO_SERIAL = "订单号待生成";
    // 子界面
    private OrderItemAddController addController;

    @FXML
    public void initialize() {

        FXUtils.limitLength(serial, 30);

        // 设置为当前时间
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        rdate.setValue(localDate);

        FXWidgetUtil.connect(tab_name, OrderItemModel::nameProperty);
        FXWidgetUtil.connect(tab_serial, OrderItemModel::serialProperty);
        FXWidgetUtil.connect(tab_detail, OrderItemModel::detailProperty);
        FXWidgetUtil.connectDecimalObj(tab_price, OrderItemModel::priceProperty);
        FXWidgetUtil.connectNum(tab_pack, OrderItemModel::packProperty);
        FXWidgetUtil.connectDecimalObj(tab_num, OrderItemModel::numProperty);
        FXWidgetUtil.connectDecimalObj(tab_total, OrderItemModel::takeTotalProperty);
        FXWidgetUtil.connectDecimalObj(tab_rate, OrderItemModel::rateProperty);
        FXWidgetUtil.connectDecimalObj(tab_ratetotal, OrderItemModel::rateProperty);
        FXWidgetUtil.connectDecimalObj(tab_totallWithRate, OrderItemModel::totalWithRateProperty);
        FXWidgetUtil.connectDecimalObj(tab_rebate, OrderItemModel::rebateProperty);
        FXWidgetUtil.connectDecimalObj(tab_allrebate, OrderItemModel::rebateTotalProperty);
        FXWidgetUtil.connectDecimalObj(tab_delifee, OrderItemModel::delifeeProperty);
        FXWidgetUtil.connectDecimalObj(tab_actPay, OrderItemModel::actPayTotalProperty);


        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRateTotal, s_rate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRebateTotal, s_rebate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);

        // 新增明细按钮
        item_add.setOnMouseClicked(e -> {
            addController.setCid(cid.getSelectionModel().getSelectedItem().getId());
            CustomDialog.gen().build(addController).ifPresent(table.getItems()::add);
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
        FXWidgetUtil.doubleToEdit(table, () -> addController, OrderItemModel::update);
    }

    @Override
    public void init() {
        serial.setText(NO_SERIAL);
        cid.getSelectionModel().clearSelection();
        table.getItems().clear();
        // 加载列表
        loadCust();

        // 加载 FXML
        if (addController == null) {
            Platform.runLater(() -> {
                addController = ViewPathUtil.loadViewForController("order_item_add.fxml");

            });
        }
    }

    @Override
    public void fill(OrderModelFull receiptModelFull) {
        serial.setText(receiptModelFull.getSerial());
        Platform.runLater(() -> {
            for (CustomModel cust : cid.getItems()) {
                if (cust.getId().equals(receiptModelFull.getCid())) {
                    cid.getSelectionModel().select(cust);
                    break;
                }
            }
        });
        rdate.setValue(FXUtils.stampToLocalDate(receiptModelFull.getOrderdate()));
        table.getItems().setAll(receiptModelFull.getOrderItemModels());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(serial.textProperty().isNotEmpty()
                .and(cid.getSelectionModel().selectedItemProperty().isNotNull())
        );
        return res;
    }

    @Override
    public OrderModelFull pack() {
        OrderModelFull res = new OrderModelFull();
        res.setSerial(serial.getText().equals(NO_SERIAL) ? null : serial.getText());
        res.setOrderdate(Date.valueOf(rdate.getValue()).getTime());
        res.setCid(cid.getSelectionModel().getSelectedItem().getId());
        res.setOrderItemModels(table.getItems());
        return res;
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
}
