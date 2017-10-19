package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 工资结算
 * Created by tom on 2017/10/19.
 */
public class SalaryClearController implements Initializable, ContentCtrl{

    public VBox root;
    public HBox monlist;
    public TableView table_stuff;
    public TableColumn tab_f_name;
    public TableColumn tab_total;
    public TableColumn tab_clear;
    public TableColumn tab_left;
    public Label lab_total;
    public Label lab_pay;
    public Label lab_left;
    public Button ok;
    public Button cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        return FXWidgetUtil.sBinding("工资结算");
    }
}
