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
 * 支出管理
 * Created by tom on 2017/9/4.
 */
public class OutComeManagerController implements ContentCtrl{
    @FXML
    private HBox root;
    @FXML
    private Button receiptBtn;
    @FXML
    private Button annuBtn;
    @FXML
    private Button staticBtn;
    @FXML
    private Button predBtn;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        receiptBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.RECEIPT_LIST));
        annuBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.OUTCOME_ANNUAL));
        staticBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.OUTCOME));
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("支出管理");
    }
}
