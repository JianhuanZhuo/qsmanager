package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.MonthPicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * Created by tom on 2017/6/12.
 */
public class TestListController implements Initializable {

    public Button xx;
    public VBox root;
    public TextField t;
    public Label b;
    private MonthPicker p;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        p = FXWidgetUtil.getMonthPicker();
        p.setOnValueChange(e->{
            t.setText(e.getKey()+"年"+e.getValue()+"月");
        });
        p.setOnClose(e->{
            b.setText(e.getKey()+"年"+e.getValue()+"月");
        });
        xx.setOnAction(event -> {
            p.show(xx);
        });
    }
}
