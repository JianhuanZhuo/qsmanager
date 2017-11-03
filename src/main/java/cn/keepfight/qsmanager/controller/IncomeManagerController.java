package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Properties;

/**
 * 收入模块欢迎界面
 * Created by tom on 2017/9/4.
 */
public class IncomeManagerController implements ContentCtrl{

    @FXML
    private Button orderBtn;
    @FXML
    private Button annuBtn;
    @FXML
    private Button predBtn;
    public Button graphBtn;
    public Button profitBtn;
    @FXML
    private HBox root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        Properties ps = new Properties();
        ps.put("mode", OrderPaneController.USING_IN_INCOME);
        orderBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.ORDER_PANE, ps));
        annuBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.INCOME_ANNUAL));
        predBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.analysis$SELL));
        graphBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.analysis$CUSTOM));
        profitBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.analysis$PRODUCTS));
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("收入管理");
    }
}
