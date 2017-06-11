package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.MaterialModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

/**
 * 新增原料界面控制器
 * Created by tom on 2017/6/7.
 */
public class MaterialAddController implements DialogContent<MaterialModel> {

    @FXML
    private VBox root;

    @FXML
    private TextField serial;
    @FXML
    private TextField name;
    @FXML
    private TextField color;
    @FXML
    private TextField spec;
    @FXML
    private TextField price;
    @FXML
    private TextField unit;

    @FXML
    public void initialize() {
        FXUtils.limitLength(serial, 30);
        FXUtils.limitLength(name, 30);
        FXUtils.limitLength(spec, 60);
        FXUtils.limitLength(unit, 15);
        FXUtils.limitLength(color, 30);

        FXUtils.limitNum(price, 9, 4, true);
    }

    @Override
    public void init() {
        serial.setText("");
        name.setText("");
        unit.setText("");
        price.setText("");
        color.setText("");
        spec.setText("");
    }

    @Override
    public void fill(MaterialModel materialModel) {
        serial.setText(materialModel.getSerial());
        name.setText(materialModel.getName());
        unit.setText(materialModel.getUnit());
        price.setText(materialModel.getPrice().toString());
        color.setText(materialModel.getColor());
        spec.setText(materialModel.getSpec());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(serial.textProperty().isNotEmpty()
                .and(name.textProperty().isNotEmpty())
                .and(unit.textProperty().isNotEmpty())
                .and(price.textProperty().isNotEmpty())
        );
        return res;
    }

    @Override
    public MaterialModel pack() {
        MaterialModel res = new MaterialModel();
        res.setSerial(serial.getText());
        res.setName(name.getText());
        res.setSpec(spec.getText());
        res.setUnit(unit.getText());
        res.setPrice(new BigDecimal(price.getText().trim().replace(",", "")));
        res.setColor(color.getText());
        return res;
    }
}
