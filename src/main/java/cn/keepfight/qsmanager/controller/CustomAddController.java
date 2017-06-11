package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.utils.DialogContent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * 新增客户信息控制器
 * Created by tom on 2017/6/7.
 */
public class CustomAddController implements DialogContent<CustomModel> {
    @FXML
    private VBox root;
    @FXML
    private TextField name;
    @FXML
    private TextField serial;
    @FXML
    private TextField acc;
    @FXML
    private TextField psw;

    @Override
    public void init() {
        name.setText("");
        serial.setText("");
        acc.setText("");
        psw.setText("");
    }

    @Override
    public void fill(CustomModel model) {
        name.setText(model.getName());
        serial.setText(model.getSerial());
        acc.setText(model.getAcc());
        psw.setText(model.getPsw());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty resProperty = new SimpleBooleanProperty();
        resProperty.bind(name.textProperty().isNotEmpty()
                .and(serial.textProperty().isNotEmpty())
                .and(acc.textProperty().isNotEmpty())
                .and(psw.textProperty().isNotEmpty()));
        return resProperty;
    }

    @Override
    public CustomModel pack() {
        CustomModel custom = new CustomModel();
        custom.setName(name.getText());
        custom.setSerial(serial.getText());
        custom.setAcc(acc.getText());
        custom.setPsw(psw.getText());

        //默认的值也要上
        custom.setUtype(2L);


        return custom;
    }
}
