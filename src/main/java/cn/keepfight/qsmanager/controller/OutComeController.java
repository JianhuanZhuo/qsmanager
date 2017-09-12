package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Properties;

/**
 * 支出管理控制器
 * Created by tom on 2017/6/6.
 */
public class OutComeController implements ContentCtrl {

    @FXML
    private VBox root;

    // 子控制器
    @FXML
    private VBox take;
    @FXML
    private OutComeTakeController takeController;
    @FXML
    private VBox pay;
    @FXML
    private OutComePayController payController;
    @FXML
    private VBox fac;
    @FXML
    private OutComeFacController facController;
    @FXML
    private HBox stat;
    @FXML
    private OutComeStatController statController;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadSelection();
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("支出管理");
    }

    @FXML
    public void initialize() {
    }


    /**
     * 加载供应商和年份下拉选择的选项
     */
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<Long> years = QSApp.service.getReceiptService().selectYear();
                facController.setYearSelection(years);
                payController.setYearSelection(years);
                statController.setYearSelection(years);
                takeController.setYearSelection(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
