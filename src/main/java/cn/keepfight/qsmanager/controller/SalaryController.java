package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.TableShowGrid;
import cn.keepfight.widget.YearPicker;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 工资表模块控制器
 * Created by tom on 2017/7/19.
 */
public class SalaryController implements ContentCtrl, Initializable{
    @FXML private HBox title;
    @FXML private VBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
        title.getChildren().add(0, new YearPicker(2017));
    }

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
        return FXWidgetUtil.sBinding("工资表");
    }
}
