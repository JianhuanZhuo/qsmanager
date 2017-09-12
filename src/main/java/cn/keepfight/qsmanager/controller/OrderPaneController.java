package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 订单面板
 * Created by tom on 2017/7/28.
 */
public class OrderPaneController implements ContentCtrl, Initializable{
    @FXML
    private VBox root;
    @FXML
    private ChoiceBox<CustomModel> cust_sel;
    @FXML
    private ChoiceBox<Long> year_sel;
    @FXML
    private ChoiceBox<Long> month_sel;
    @FXML
    private ChoiceBox<Long> day_sel;
    @FXML
    private Button load;

    @FXML
    private Label label;
    @FXML
    private Button add_order;
    @FXML
    private ListView<OrderModelFull> orderList;


//    private OrderAddController orderAddController;

    public final static String USING_IN_ORDERS = "USING_IN_ORDERS";
    public final static String USING_IN_INCOME = "USING_IN_INCOME";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderList.setCellFactory(param->new OrderListCell(this));

        // 设置客户下拉转换器、年转换器
        cust_sel.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName(), "全部客户"));
        year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));

        month_sel.setConverter(FXUtils.converter(x->x+"月", "全部月份"));
        month_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        month_sel.getItems().add(null);

        day_sel.setConverter(FXUtils.converter(x->x+"号", "当月全部"));
        day_sel.setItems(FXCollections.observableList(LongStream.range(1, 32).boxed().collect(Collectors.toList())));
        day_sel.getItems().add(null);

        load.setOnAction(e-> loadOrders());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadSelection();
//        Platform.runLater(() -> {
//            try {
//                orderAddController = ViewPathUtil.loadViewForController("order_add.fxml");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    @Override
    public void showed(Properties params) {
        String mode = params.getProperty("mode");
        if (USING_IN_ORDERS.equals(mode)){
            label.setText("(下单请先选择客户)");
            add_order.setText("我要下单");
            add_order.disableProperty().bind(cust_sel.getSelectionModel().selectedItemProperty().isNull());
            add_order.setOnAction(event -> {
                // 添加新的订单
                OrderModelFull orderModel = new OrderModelFull();
                orderModel.setCid(cust_sel.getSelectionModel().getSelectedItem().getId());
                orderModel.setOrderdate(System.currentTimeMillis());
                // 插入
                try {
                    QSApp.service.getOrderService().insert(orderModel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("新增订单失败", "新增订单失败，请检查网络是否通畅");
                    return;
                }
                PrintSource source = new PrintSource();
                source.setCust(orderModel.getCid());
                LocalDate localDate = FXUtils.stampToLocalDate(orderModel.getOrderdate());
                source.setYear((long) localDate.getYear());
                source.setMonth((long) localDate.getMonthValue());
                source.setItem(orderModel.getId());
                QSPrintType t;
                if (cust_sel.getSelectionModel().getSelectedItem().getName()!=null
                        && cust_sel.getSelectionModel().getSelectedItem().getName().contains("安利")){
                    t = QSPrintType.DELIVERY_ANLI;
                }else {
                    t = QSPrintType.DELIVERY;
//                    QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.DELIVERY, source));
                }
                QSApp.mainPane.changeTo(MainPaneList.PRINT_MANAGER, FXUtils.ps(new Pair<>("selection", new PrintSelection(t, source))));
//                loadOrders();
            });
        }else if (USING_IN_INCOME.equals(mode)){
            label.setText("(月账请先选择客户、年份和月份)");
            add_order.setText("打印月账");
            add_order.disableProperty().bind(cust_sel.getSelectionModel().selectedItemProperty().isNull()
                    .or(year_sel.getSelectionModel().selectedItemProperty().isNull())
                    .or(month_sel.getSelectionModel().selectedItemProperty().isNull()));
            add_order.setOnAction(event -> {
                PrintSource source = new PrintSource();
                source.setCust(cust_sel.getSelectionModel().getSelectedItem().getId());
                source.setYear(year_sel.getSelectionModel().getSelectedItem());
                source.setMonth(month_sel.getSelectionModel().getSelectedItem());
                source.setItem(null);
                QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.MON_CUST, source));
            });
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("订单列表");
    }

//    void updateOrder(OrderModelFull modelFull) {
//        Optional<OrderModelFull> op = CustomDialog.gen().build(orderAddController, modelFull);
//        op.ifPresent(model -> {
//            try {
//                model.setId(modelFull.getId());
//                QSApp.service.getOrderService().update(model);
//                loadOrders();
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                WarningBuilder.build("新增供应送货失败", "新增供应送货失败，请检查网络是否通畅");
//            }
//        });
//    }


    /**
     * 从供应送货列表中删除指定的对象
     */
    void deleteSelected(OrderModelFull modelFull) {
        Platform.runLater(() -> {
            try {
                QSApp.service.getOrderService().delete(modelFull.get());
                orderList.getItems().remove(modelFull);
            } catch (Exception e) {
                WarningBuilder.build("删除失败，请检查网络是否通畅！");
            }
        });
    }
    /**
     * 所谓事件处理器，加载对账表
     */
    private void loadOrders() {
        CustomModel selCust = cust_sel.getSelectionModel().getSelectedItem();
        Long cid = selCust==null?null:selCust.getId();
        Long year = year_sel.getSelectionModel().getSelectedItem();
        Long month = month_sel.getSelectionModel().getSelectedItem();
        Long day = day_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                orderList.getItems().setAll(QSApp.service.getOrderService().selectAll(new OrderSelection(cid, year, month, day)));
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
                custs.add(null);
                cust_sel.getItems().setAll(FXCollections.observableList(custs));

                List<Long> years = QSApp.service.getOrderService().selectYear();
                years.add(null);
                year_sel.getItems().setAll(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 产品列表条目
     */
    private static class OrderListCell extends ListCell<OrderModelFull> {
        OrderItemController controller;
        OrderPaneController c;

        OrderListCell(OrderPaneController c) {
            this.c = c;
            try {
                controller = ViewPathUtil.loadViewForController("orderform.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateItem(OrderModelFull item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                setGraphic(controller.getRoot());
                controller.fill(item, c);
            }
        }
    }
}
