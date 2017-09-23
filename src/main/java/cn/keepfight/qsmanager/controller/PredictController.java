package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 预算表
 * Created by tom on 2017/9/23.
 */
public class PredictController implements Initializable, ContentCtrl {
    public VBox root;

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
        return FXWidgetUtil.sBinding("预算表");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
