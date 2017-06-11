package cn.keepfight.qsmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * 采购统计界面控制器
 * Created by tom on 2017/6/10.
 */
public class OutComeTakeController {

    //年度采购部分
    @FXML
    private ChoiceBox at_year_sel;
    @FXML
    private TableView at_table;
    @FXML
    private Label at_total;
}
