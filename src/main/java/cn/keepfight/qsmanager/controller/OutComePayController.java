package cn.keepfight.qsmanager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

/**
 * 支出界面的付款统计部分
 * Created by tom on 2017/6/10.
 */
public class OutComePayController {

    //年度付款部分
    @FXML
    private ChoiceBox ap_year_sel;
    @FXML
    private TableView ap_table;
    @FXML
    private Label ap_total;
}
