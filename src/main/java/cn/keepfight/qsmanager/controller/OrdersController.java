package cn.keepfight.qsmanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 订单界面控制器
 * Created by tom on 2017/6/6.
 */
public class OrdersController implements ContentCtrl,Initializable {
    @FXML
    private VBox root;
    @FXML
    private VBox orderPane;
    @FXML
    private OrderPaneController orderPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderPaneController.config(OrderPaneController.USING_IN_ORDERS);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        orderPaneController.loaded();
    }
    @Override
    public void showed() {
        orderPaneController.showed();
    }
}
