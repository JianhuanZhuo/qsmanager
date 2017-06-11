package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.MaterialModel;
import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;

/**
 * 新增原料界面控制器
 * Created by tom on 2017/6/7.
 */
public class ReceiptItemAddController implements DialogContent<ReceiptDetailModel> {

    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<MaterialModel> serial;
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
    private TextField num;

    // 已选中的供应商ID
    private Long sid;

    @FXML
    public void initialize() {
        FXUtils.limitLength(name, 30);
        FXUtils.limitLength(spec, 60);
        FXUtils.limitLength(unit, 15);
        FXUtils.limitLength(color, 30);

        FXUtils.limitNum(price, 9, 4, true);
        FXUtils.limitNum(num, 9, 0, true);

        // 设置切换恢复默认值
        serial.getSelectionModel().selectedIndexProperty().addListener((x, oldOne, newOne) -> {
            if (newOne.intValue() < 0) {
                return;
            }
            MaterialModel model = serial.getItems().get(newOne.intValue());
            name.setText(model.getName());
            unit.setText(model.getUnit());
            price.setText(model.getPrice().toString());
            color.setText(model.getColor());
            spec.setText(model.getSpec());
        });

        // 设置转换器
        serial.setConverter(new StringConverter<MaterialModel>() {
            @Override
            public String toString(MaterialModel model) {
                return model.getSerial() + "-" + model.getName();
            }

            @Override
            public MaterialModel fromString(String string) {
                return null;
            }
        });
    }

    @Override
    public void init() {
        serial.getSelectionModel().clearSelection();
        name.setText("");
        unit.setText("");
        price.setText("");
        color.setText("");
        spec.setText("");
        num.setText("");

        // 载入原料列表
        loadMaterial();
    }

    @Override
    public void fill(ReceiptDetailModel detailModel) {
        name.setText(detailModel.getName());
        unit.setText(detailModel.getUnit());
        price.setText(detailModel.getPrice().toString());
        color.setText(detailModel.getColor());
        spec.setText(detailModel.getSpec());
        num.setText(detailModel.getNum().toString());

        for (MaterialModel model : serial.getItems()) {
            if (model.getSerial().equals(detailModel.getSerial())) {
                serial.getSelectionModel().select(model);
                break;
            }
        }
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(serial.getSelectionModel().selectedItemProperty().isNotNull()
                .and(num.textProperty().isNotEmpty())
                .and(name.textProperty().isNotEmpty())
                .and(unit.textProperty().isNotEmpty())
                .and(price.textProperty().isNotEmpty())
                .and(color.textProperty().isNotEmpty())
                .and(spec.textProperty().isNotEmpty())
        );
        return res;
    }

    void setSid(Long sid) {
        this.sid = sid;
    }

    @Override
    public ReceiptDetailModel pack() {
        ReceiptDetailModel res = new ReceiptDetailModel();
        res.setName(name.getText());
        res.setSerial(serial.getSelectionModel().getSelectedItem().getSerial());
        res.setSpec(spec.getText());
        res.setUnit(unit.getText());
        res.setPrice(new BigDecimal(price.getText().trim().replace(",", "")));
        res.setColor(color.getText());
        res.setNum(Long.valueOf(num.getText()));
        return res;
    }

    private void loadMaterial() {
        Platform.runLater(() -> {
            try {
                serial.getItems().setAll(QSApp.service.getMaterialService().selectAll(sid));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}