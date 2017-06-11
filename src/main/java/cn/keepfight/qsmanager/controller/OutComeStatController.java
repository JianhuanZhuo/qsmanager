package cn.keepfight.qsmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * 购付统计界面控制器
 * Created by tom on 2017/6/10.
 */
public class OutComeStatController {
    //购付统计部分
    @FXML
    private ChoiceBox stat_year_sel;
    @FXML
    private TableView stat_takePay_table;
    @FXML
    private Label tp_total_take;
    @FXML
    private Label tp_total_pay;
    @FXML
    private Label tp_total_un_pay;
    @FXML
    private Label tp_total_spend;
}
