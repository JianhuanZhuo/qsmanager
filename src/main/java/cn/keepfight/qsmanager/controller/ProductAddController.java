package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * 新增产品界面控制器
 * Created by tom on 2017/6/7.
 */
public class ProductAddController implements DialogContent<ProductModel> {
    @FXML
    private VBox root;
    @FXML
    private TextField serial;
    @FXML
    private TextField name;
    @FXML
    private TextField detail;
    @FXML
    private TextField unit;
    @FXML
    private TextField price;
    @FXML
    private TextField packNum;
    @FXML
    private TextField picurl;
    @FXML
    private Button pickBtn;

    @FXML
    private ImageView imageLoader;


    // 子界面控制器
    private PicMakerController addController;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                addController = ViewPathUtil.loadViewForController("pic_maker.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pickBtn.setOnAction(event -> {
            Optional<String> op = CustomDialog.gen().build(addController, picurl.getText());
            op.ifPresent(this::setPicurl);
        });

        FXUtils.limitLength(serial, 30);
        FXUtils.limitLength(name, 30);
        FXUtils.limitLength(detail, 30);
        FXUtils.limitLength(unit, 15);

        FXUtils.limitNum(packNum, 8, 0, true);
        FXUtils.limitNum(price, 9, 4, true);
    }

    @Override
    public void init() {
        serial.setText("");
        name.setText("");
        detail.setText("");
        unit.setText("");
        price.setText("");
        packNum.setText("");
        picurl.setText("");
        ImageLoadUtil.bindDefault(imageLoader);
    }

    @Override
    public void fill(ProductModel productModel) {
        serial.setText(productModel.getSerial());
        name.setText(productModel.getName());
        detail.setText(productModel.getDetail());
        unit.setText(productModel.getUnit());
        price.setText(productModel.getPrice().toString());
        packNum.setText(productModel.getPack().toString());
        setPicurl(productModel.getPicurl());
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
    public ProductModel pack() {
        ProductModel res = new ProductModel();
        res.setSerial(serial.getText());
        res.setName(name.getText());
        res.setDetail(detail.getText());
        res.setUnit(unit.getText());
        res.setPrice(new BigDecimal(price.getText().trim().replace(",", "")));
        res.setPicurl(picurl.getText());
        res.setPack(Long.valueOf(packNum.getText()));
        return res;
    }

    private void setPicurl(String absolutePath) {
        if (absolutePath != null && !absolutePath.equals("")) {
            picurl.setText(absolutePath);
            ImageLoadUtil.bindImageDirectly(imageLoader, absolutePath);
        }
    }
}
