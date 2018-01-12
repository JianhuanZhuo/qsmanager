package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
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
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * 订单记录控制器
 * Created by tom on 2017/6/11.
 */
public class OrderItemController implements ContentCtrl, Initializable {

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
    @FXML
    private TableColumn<OrderItemModel, String> tab_price;
    @FXML
    private TableColumn<OrderItemModel, String> tab_num;
    @FXML
    private TableColumn<OrderItemModel, String> tab_total;
    public TableColumn<OrderItemModel, BigDecimal> tab_delifee;
    public TableColumn<OrderItemModel, BigDecimal> tab_actPay;
    @FXML
    private Label o_serial;
    @FXML
    private Label o_date;
    @FXML
    private Label o_cust;

    @FXML
    private Label state;

    @FXML
    private ImageView o_msg;
    @FXML
    private Label s_num;
    @FXML
    private Label s_total;
    @FXML
    private Button a_del;
    @FXML
    private Button a_print;
    @FXML
    private Button a_alter;

    // 内部表现数据
    private OrderModelFull modelFull;
    private OrderPaneController ordersController;
    private Tooltip msg = new Tooltip();

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {

    }

    @Override
    public void showed(Properties params) {
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("订单详情");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tab_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        tab_serial.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSerial()));
        tab_detail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDetail()));

        FXWidgetUtil.connectDecimalColumn(tab_delifee, OrderItemModel::delifeeProperty);
        FXWidgetUtil.connectDecimalColumn(tab_actPay, OrderItemModel::actPayTotalProperty);

        FXWidgetUtil.connectDecimal(tab_price, OrderItemModel::getPrice);
        FXWidgetUtil.connectDecimal(tab_total, x->x.getNum().multiply(x.getPrice()).multiply(new BigDecimal(x.getPack())));
        tab_pack.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPack() == 1L ? "散装" : "整装(" + param.getValue().getPack() + ")"));
        tab_num.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNum().stripTrailingZeros().toPlainString()
                + (param.getValue().getPack() == 1L ? "个" : param.getValue().getUnit())));

        FXWidgetUtil.cellMoney(tab_delifee, tab_actPay);

        a_del.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("是否要删除这条订单记录？");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ordersController.deleteSelected(modelFull);
            }
        });

//        a_alter.setOnAction(event -> ordersController.updateOrder(modelFull));
        a_alter.setOnAction(event -> {
            Properties ps = new Properties();
            ps.put("id", modelFull.getId());
            ps.put("more", false);
            QSApp.mainPane.changeTo(MainPaneList.ORDER_MAKE, ps);
//            ordersController.updateOrder(modelFull);
        });
        a_print.setOnAction(event -> {
            // 打印
            PrintSource source = new PrintSource();
            source.setCust(modelFull.getCid());
            LocalDate localDate = FXUtils.stampToLocalDate(modelFull.getOrderdate());
            source.setYear((long) localDate.getYear());
            source.setMonth((long) localDate.getMonthValue());
            source.setItem(modelFull.getId());
//            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.DELIVERY, source));
            QSApp.mainPane.changeTo(MainPaneList.PRINT_MANAGER, FXUtils.ps(new Pair<>("selection", new PrintSelection(QSPrintType.DELIVERY, source))));
        });

        FXWidgetUtil.hackTooltipStartTiming(msg);
        Tooltip.install(o_msg, msg);

        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), OrderItemModel::getActualPayTotal, s_total::setText);
    }

    public void fill(OrderModelFull modelFull, OrderPaneController controller) {
        this.modelFull = modelFull;
        this.ordersController = controller;

        // 填充数据
        o_date.setText(FXUtils.stampToDate(modelFull.getOrderdate()));
        o_serial.setText(modelFull.getSerial());
        o_cust.setText(modelFull.getCust());

        state.setText(modelFull.isDeli() ? "已发货" : "未发货");

        if (Objects.nonNull(modelFull.getNote()) && !modelFull.getNote().trim().equals("")) {
            FXUtils.delStyle("hide", o_msg);
            msg.setText(modelFull.getNote());
        } else {
            FXUtils.addStyle("hide", o_msg);
        }
        table.getItems().setAll(modelFull.getOrderItemModels());

        // 默认选择为全部货项
    }
}
