package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.Properties;

/**
 * 新增产品界面控制器
 * Created by tom on 2017/6/7.
 */
public class OrderEditController implements ContentCtrl {

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
    @FXML
    private Button item_rate;
    @FXML
    private Button item_rebate;

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
    private Label s_num;
    @FXML
    private Label s_total;

    @FXML
    private VBox pay;
    @FXML
    private Button item_edit;

    @FXML
    private Button item_ok;
    @FXML
    private Button item_cancel;

    // 子界面
    private OrderItemAddController addController;

    private StringProperty titlePropert = new SimpleStringProperty("订单信息");

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

        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getTakeTotal, s_total::setText);

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

        pay.managedProperty().bind(pay.visibleProperty());
        item_rate.visibleProperty().bind(pay.visibleProperty());
        item_rebate.visibleProperty().bind(pay.visibleProperty());
    }

//    @Override
//    public void fill(OrderModelFull receiptModelFull) {
//        serial.setText(receiptModelFull.getSerial());
//        Platform.runLater(()->{
//            for (CustomModel cust : cid.getItems()) {
//                if (cust.getId().equals(receiptModelFull.getCid())){
//                    cid.getSelectionModel().select(cust);
//                    break;
//                }
//            }
//        });
//        rdate.setValue(FXUtils.stampToLocalDate(receiptModelFull.getOrderdate()));
//        table.getItems().setAll(receiptModelFull.getOrderItemModels());
//    }

//    @Override
//    public BooleanProperty allValid() {
//        BooleanProperty res = new SimpleBooleanProperty();
//        res.bind(serial.textProperty().isNotEmpty()
//                .and(cid.getSelectionModel().selectedItemProperty().isNotNull())
//        );
//        return res;
//    }

//    @Override
//    public OrderModelFull pack() {
//        OrderModelFull res = new OrderModelFull();
//        res.setSerial(serial.getText().equals(NO_SERIAL)?null:serial.getText());
//        res.setOrderdate(Date.valueOf(rdate.getValue()).getTime());
//        res.setCid(cid.getSelectionModel().getSelectedItem().getId());
//        res.setOrderItemModels(table.getItems());
//        return res;
//    }


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        // 加载 FXML
        if (addController == null) {
            Platform.runLater(() -> addController = ViewPathUtil.loadViewForController("order_item_add.fxml"));
        }

        item_cancel.setOnAction(event -> {
            // @TODO 取消时需要消除新建插入的东西
            QSApp.mainPane.backNav();
        });
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
        } else {
            OrderModelFull model;
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
        }

        // 是否显示支付方式东西
        switch (QSApp.mainPane.getUserModel().getUtype().intValue()) {
            case 0:
            case 1:
                pay.setVisible(true);
                break;
            default:
                pay.setVisible(false);
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.spBinding(titlePropert);
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
