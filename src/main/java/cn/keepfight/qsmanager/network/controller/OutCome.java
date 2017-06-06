package cn.keepfight.qsmanager.network.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * 支出管理控制器
 * Created by tom on 2017/6/6.
 */
public class OutCome implements ContentController {
    @FXML
    private VBox root;

    // 送货列表部分
    @FXML
    private ChoiceBox rec_sup_sel;
    @FXML
    private ChoiceBox rec_year_sel;
    @FXML
    private ChoiceBox rec_mon_sel;
    @FXML
    private Button rec_add;
    @FXML
    private VBox receiptList;

    // 年度对账表部分
    @FXML
    private ChoiceBox an_sup_sel;
    @FXML
    private ChoiceBox an_year_sel;
    @FXML
    private Button an_print;
    @FXML
    private TableView anTable;
    @FXML
    private Label an_count_bf;
    @FXML
    private Label an_total_annu;
    @FXML
    private Label an_total_rate;
    @FXML
    private Label an_total_pay;
    @FXML
    private Label an_total_actu;
    @FXML
    private Label an_attach;

    //年度采购部分
    @FXML
    private ChoiceBox at_year_sel;
    @FXML
    private TableView at_table;
    @FXML
    private Label at_total;

    //年度付款部分
    @FXML
    private ChoiceBox ap_year_sel;
    @FXML
    private TableView ap_table;
    @FXML
    private Label ap_total;

    //厂支出部分
    @FXML
    private Label fac_no_pay_total;
    @FXML
    private Button fac_add;
    @FXML
    private Button fac_del;
    @FXML
    private Label fac_attach;

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

    @Override
    public Node getRoot() {
        return root;
    }
}
