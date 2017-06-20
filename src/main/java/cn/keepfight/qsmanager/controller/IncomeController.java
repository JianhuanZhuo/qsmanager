package cn.keepfight.qsmanager.controller;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * 收入管理控制器类
 * Created by tom on 2017/6/6.
 */
public class IncomeController implements ContentController {
    public ChoiceBox cust_sel;
    public ChoiceBox year_sel;
    public ChoiceBox mon_sel;
    public RadioButton sepra;
    public RadioButton aggra;
    public ImageView print;
    public VBox deliveryList;
    public ChoiceBox cust_sel_an;
    public ChoiceBox year_sel_an;
    public ImageView print_an;
    public TableView anTable;
    public Label count_bf;
    public Label total_annu;
    public Label total_rate;
    public Label total_pay;
    public Label total_actu;
    public Label attach;
    public VBox root;

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {
    }

    @Override
    public void showed() {

    }
}
