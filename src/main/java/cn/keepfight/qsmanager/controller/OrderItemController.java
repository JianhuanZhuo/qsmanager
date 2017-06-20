package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 订单记录控制器
 * Created by tom on 2017/6/11.
 */
public class OrderItemController implements ContentController, Initializable {

    @FXML
    private ToggleGroup goods;
    @FXML
    private RadioButton all;
    @FXML
    private RadioButton left;
    @FXML
    private VBox root;

    @FXML
    private TableView<OrderItemModel> table;
    @FXML
    private TableColumn<OrderItemModel, String> tab_serial;
    @FXML
    private TableColumn<OrderItemModel, String> tab_name;
    @FXML
    private TableColumn<OrderItemModel, String> tab_detail;
    @FXML
    private TableColumn<OrderItemModel, String> tab_pack;
    //    @FXML private TableColumn<OrderItemModel, String> tab_unit;
    @FXML
    private TableColumn<OrderItemModel, String> tab_price;
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
    public TableColumn<OrderItemModel, String> tab_actPay;
    @FXML
    private Label o_serial;
    @FXML
    private Label o_date;
    @FXML
    private Label o_cust;
    @FXML
    private ImageView o_msg;
    @FXML
    private Label s_num;
    @FXML
    private Label s_rate;
    @FXML
    private Label s_rebate;
    @FXML
    private Label s_total;
    @FXML
    private Button a_del;
    @FXML
    private Button a_delivery;
    @FXML
    private Button a_history;
    @FXML
    private Button a_alter;

    // 内部表现数据
    private OrderModelFull modelFull;
    private OrdersController ordersController;
    private Tooltip msg = new Tooltip();

    // 静态
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {

    }

    @Override
    public void showed() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        tab_serial.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSerial()));
        tab_detail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDetail()));
        tab_price.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPrice().toString()));
        tab_pack.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPack() == 1L ? "散装" : "整装(" + param.getValue().getPack() + ")"));
//        tab_unit.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUnit()));
        tab_num.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNum().toString()
                + (param.getValue().getPack() == 1L ? "个" : param.getValue().getUnit())));
        tab_total.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPrice()
                        .multiply(param.getValue().getNum()
                                .multiply(new BigDecimal(param.getValue().getPack()))).toString()));
        tab_rate.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getRate()));
        tab_ratetotal.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getRateTotal()));
        tab_totallWithRate.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getTotalWithRate()));
        tab_rebate.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getRebate()));
        tab_allrebate.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getRebateTotal()));
        tab_delifee.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getDelifee()));
        tab_actPay.setCellValueFactory(param ->new SimpleStringProperty(""+param.getValue().getActualPayTotal()));


        a_del.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("是否要删除这条订单记录？");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
//                outComeController.deleteSelected(modelFull);
            }
        });

//        update.setOnAction(event -> outComeController.updateReceipt(modelFull));
        FXWidgetUtil.hackTooltipStartTiming(msg);
        Tooltip.install(o_msg, msg);

        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRateTotal, s_rate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getRebateTotal, s_rebate::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);
    }

    public void fill(OrderModelFull modelFull, OrdersController controller) {
        this.modelFull = modelFull;
        this.ordersController = controller;

        // 填充数据
        o_date.setText(formatter.format(new Date(modelFull.getOrderdate())));
        o_serial.setText(modelFull.getSerial());
        o_cust.setText(modelFull.getCust());
        if (Objects.nonNull(modelFull.getNote()) && !modelFull.getNote().trim().equals("")) {
            FXUtils.delStyle("hide", o_msg);
            msg.setText(modelFull.getNote());
        } else {
            FXUtils.addStyle("hide", o_msg);
        }
        table.getItems().setAll(modelFull.getOrderItemModels());

        // 默认选择为全部货项
        all.fire();
    }

}
