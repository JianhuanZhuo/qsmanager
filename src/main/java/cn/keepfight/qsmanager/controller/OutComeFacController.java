package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 厂支出界面控制器
 * Created by tom on 2017/6/10.
 */
public class OutComeFacController implements Initializable, OutComeSub{

    @FXML
    private ChoiceBox<Long> year_select;

    @FXML private TableView tab;

    @FXML private TableColumn detail;
    @FXML private TableColumn mon1;
    @FXML private TableColumn mon2;
    @FXML private TableColumn mon3;
    @FXML private TableColumn mon4;
    @FXML private TableColumn mon5;
    @FXML private TableColumn mon6;
    @FXML private TableColumn mon7;
    @FXML private TableColumn mon8;
    @FXML private TableColumn mon9;
    @FXML private TableColumn mon10;
    @FXML private TableColumn mon11;
    @FXML private TableColumn mon12;
    @FXML private TableColumn year;

    //厂支出部分
    @FXML
    private Label actual_total;
    @FXML
    private Button fac_add;
    @FXML
    private Button fac_del;
    @FXML
    private Label fac_attach;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        year_select.setConverter(FXUtils.converter(x->x+"年", "全部年份"));
    }

    @Override
    public void setYearSelection(List<Long> ls) {
        year_select.getItems().setAll(ls);
    }
}
