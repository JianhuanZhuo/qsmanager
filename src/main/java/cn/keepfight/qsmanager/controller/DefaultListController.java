package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.ViewPathUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 默认下拉列表控制器
 * Created by tom on 2017/6/16.
 */
public class DefaultListController extends PopupControl implements Initializable{
    @FXML private Pane root;
    @FXML private ListView list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
