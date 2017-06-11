package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.utils.DialogContent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * 新增供应商信息控制器
 * Created by tom on 2017/6/7.
 */
public class SupplyAddController implements DialogContent<SupplyModel> {
    @FXML
    private VBox root;
    @FXML
    private TextField serial;
    @FXML
    private TextField name;

    @Override
    public void init() {
        name.setText("");
        serial.setText("");
    }

    @Override
    public void fill(SupplyModel model) {

    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty resProperty = new SimpleBooleanProperty();
        resProperty.bind(name.textProperty().isNotEmpty()
                .and(serial.textProperty().isNotEmpty()));
        return resProperty;
    }

    @Override
    public SupplyModel pack() {
        SupplyModel model = new SupplyModel();
        model.setName(name.getText());
        model.setSerial(serial.getText());

        return model;
    }
}
