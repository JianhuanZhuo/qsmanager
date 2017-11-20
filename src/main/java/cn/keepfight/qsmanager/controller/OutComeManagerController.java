package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Properties;

/**
 * 支出管理
 * Created by tom on 2017/9/4.
 */
public class OutComeManagerController implements ContentCtrl {
    @FXML
    private VBox root;
    @FXML
    private Button receiptBtn;
    @FXML
    private Button annuBtn;
    @FXML
    private Button staticBtn;
    @FXML
    private Button predBtn;
    @FXML
    private Button salaryBtn;
    @FXML
    private Button rayBtn;
    public Button takeBtn;
    public Button materialBtn;
    public Button payTotalBtn;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        receiptBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.RECEIPT_LIST));
        annuBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.OUTCOME_ANNUAL));
        staticBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.OUTCOME));
        salaryBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.SALARY));
        rayBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.tax$TAX));
        predBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.PREDICT));
        takeBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.analysis$OUTCOME_STATIC));
        materialBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.STATIC_MATERIAL));
        payTotalBtn.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.STATIC_TOTAL_PAY));
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("支出管理");
    }
}
