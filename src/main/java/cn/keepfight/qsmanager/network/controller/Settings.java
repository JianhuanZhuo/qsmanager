package cn.keepfight.qsmanager.network.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * 系统设置界面控制器
 * Created by tom on 2017/6/6.
 */
public class Settings implements ContentController {
    @FXML
    private VBox root;
    @FXML
    private TextField port;
    @FXML
    private TextField man;
    @FXML
    private TextField server;
    @FXML
    private Button save;
    @FXML
    private Button reset;

    @Override
    public Node getRoot() {
        return root;
    }
}
