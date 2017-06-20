package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.qsmanager.model.OrderSelection;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 订单界面控制器
 * Created by tom on 2017/6/6.
 */
public class OrdersController implements ContentController,Initializable {
    @FXML
    private ChoiceBox<CustomModel> cust_sel;
    @FXML
    private ChoiceBox<Long> year_sel;
    @FXML
    private ChoiceBox<Long> month_sel;
    @FXML
    private ChoiceBox<OrderSelection.State> state_sel;
    @FXML
    private Button load;

    @FXML
    private Button add_order;
    @FXML
    private Button refresh;
    @FXML
    private ListView<OrderModelFull> orderList;
    @FXML
    private VBox root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadSelection();
    }
    @Override
    public void showed() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderList.setCellFactory(param->new OrderListCell(this));

        // 设置客户下拉转换器、年转换器
        cust_sel.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName(), "全部客户"));
        year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));

        month_sel.setConverter(FXUtils.converter(x->x+"月", "全部月份"));
        month_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        month_sel.getItems().add(null);

        state_sel.setConverter(FXUtils.converter(OrderSelection.State::getDisplayText, "全部订单"));
        state_sel.getItems().setAll(new ArrayList<>(Arrays.asList(OrderSelection.State.values())));
        state_sel.getItems().add(null);

        load.setOnAction(e-> loadOrders());
        refresh.setOnAction(e->{
            orderList.getItems().clear();
            loadSelection();
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
        OrderSelection.State state = state_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                orderList.getItems().setAll(QSApp.service.getOrderService().selectAll(new OrderSelection(cid, year, month, state)));
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
        OrdersController ordersController;

        OrderListCell(OrdersController ordersController) {
            this.ordersController = ordersController;
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
                controller.fill(item, ordersController);
            }
        }
    }
}
