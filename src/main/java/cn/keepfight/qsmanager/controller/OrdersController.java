package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.Properties;
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
    public void showed(Properties params) {
        orderPaneController.showed(params);
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("订单管理");
    }
}
