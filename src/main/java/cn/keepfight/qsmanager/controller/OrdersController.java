package cn.keepfight.qsmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

/**
 * 订单界面控制器
 * Created by tom on 2017/6/6.
 */
public class OrdersController implements ContentController {
    @FXML
    private ChoiceBox cust_sel;
    @FXML
    private ChoiceBox state_sel;
    @FXML
    private Button add_order;
    @FXML
    private Button refresh;
    @FXML
    private VBox orderList;
    @FXML
    private VBox root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void refresh() {

    }
}
