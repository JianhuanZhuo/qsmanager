package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.ConfigUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 系统设置界面控制器
 * Created by tom on 2017/6/6.
 */
public class SettingsController implements ContentCtrl, Initializable {
    @FXML
    private VBox root;
    @FXML
    private TextField port;
    @FXML
    private TextField server;
    @FXML
    private TextField man;
    @FXML
    private Button save;
    @FXML
    private Button reset;

    /**
     * 属性存储的默认属性文件
     */
    private static final String PROPERTIES_FILE = "fxapp.properties";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset.setOnAction(event -> readProperties());
        save.setOnAction(event -> saveSetting());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        readProperties();
    }
    @Override
    public void showed() {
    }

    private void readProperties(){
        Properties ps = ConfigUtil.load(PROPERTIES_FILE);
        man.setText(ps.getProperty("print.maker"));
        server.setText(ps.getProperty("system.server.ip"));
        port.setText(ps.getProperty("system.server.port"));
    }

    private void saveSetting(){
        ConfigUtil.alter(PROPERTIES_FILE,
                new Pair<>("print.maker", man.getText()),
                new Pair<>("system.server.ip", server.getText()),
                new Pair<>("system.server.port", port.getText())
        );
    }
}
