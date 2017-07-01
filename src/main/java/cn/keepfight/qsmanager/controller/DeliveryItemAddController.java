package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.DeliveryItemModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 新增原料界面控制器
 * Created by tom on 2017/6/7.
 */
public class DeliveryItemAddController implements DialogContent<DeliveryItemModel> {

    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<OrderItemModel> serial_c;

    @FXML
    private TextField tab_serial;
    @FXML
    private TextField tab_name;
    @FXML
    private TextField tab_detail;
    @FXML
    private TextField tab_price;
    @FXML
    private TextField tab_pack;
    @FXML
    private TextField tab_num;
    @FXML
    private TextField tab_unit;
    @FXML
    private TextField tab_total;
    @FXML
    private TextField tab_note;

    private boolean serialChangeEnable = true;

    // 已选中的订单 ID
    private Long oid;


    @FXML
    public void initialize() {
        // 设置切换恢复默认值
        serial_c.getSelectionModel().selectedIndexProperty().addListener((x, oldOne, newOne) -> {
            if (newOne.intValue() < 0 || !serialChangeEnable) {
                return;
            }

            OrderItemModel model = serial_c.getItems().get(newOne.intValue());
            if (model == null) {
                tab_serial.setText("");
                tab_name.setText("");
                tab_detail.setText("");
                tab_price.setText("0");
                tab_unit.setText("箱");
                tab_pack.setText("0");
                tab_num.setText("0");
                tab_note.setText("");
            } else {
                tab_serial.setText(model.getSerial());
                tab_name.setText(model.getName());
                tab_detail.setText(model.getDetail());
                tab_price.setText("" + model.getPrice());
                tab_unit.setText(model.getUnit());
                tab_pack.setText("" + model.getPack());
                tab_num.setText("" + model.getNum());
            }
        });

        // 设置转换器
        serial_c.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "选择模板"));

        FXWidgetUtil.simpleTriOper(tab_total,
                BigDecimal::multiply,
                BigDecimal::multiply,
                tab_price, tab_num, tab_pack);

        FXUtils.limitLength(tab_serial, 30);
        FXUtils.limitLength(tab_name, 30);
        FXUtils.limitLength(tab_detail, 30);
        FXUtils.limitLength(tab_note, 60);
        FXUtils.limitNum(tab_pack, 9, 4, true);
        FXUtils.limitNum(tab_num, 9, 4, true);

    }

    @Override
    public void init() {
        tab_serial.setText("");
        tab_name.setText("");
        tab_detail.setText("");
        tab_price.setText("0");
        tab_pack.setText("0");
        tab_num.setText("0");
        tab_note.setText("");
        // 加载产品列表
        loadProducts();
    }

    @Override
    public void fill(DeliveryItemModel model) {
        tab_serial.setText(model.getSerial());
        tab_name.setText(model.getName());
        tab_detail.setText(model.getDetail());
        tab_price.setText("" + model.getPrice());
        tab_unit.setText(model.getUnit());
        tab_pack.setText("" + model.getPack());
        tab_num.setText("" + model.getNum());
        tab_note.setText("" + model.getNote());
    }

    void setOid(Long oid) {
        this.oid = oid;
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(tab_serial.textProperty().isNotEmpty()
                .and(tab_name.textProperty().isNotEmpty())
                .and(tab_price.textProperty().isNotEmpty())
                .and(tab_pack.textProperty().isNotEmpty())
                .and(tab_num.textProperty().isNotEmpty())
                .and(tab_total.textProperty().isNotEmpty())
        );
        return res;
    }

    @Override
    public DeliveryItemModel pack() {
        DeliveryItemModel res = new DeliveryItemModel();
        res.setName(tab_name.getText());
        res.setSerial(tab_serial.getText());
        res.setDetail(tab_detail.getText());
        res.setPrice(FXUtils.getDecimal(tab_price));
        res.setPack(FXUtils.getLong(tab_pack.getText(), 1));
        res.setNum(FXUtils.getDecimal(tab_num));
        res.setUnit(tab_unit.getText());
        res.setNote(tab_note.getText());

        // 清空选择器
        serial_c.getSelectionModel().clearSelection();
        return res;
    }

    /**
     * 加载产品列表
     */
    private void loadProducts() {
        Platform.runLater(() -> {
            try {
                serialChangeEnable = false;
                serial_c.getItems().setAll(QSApp.service.getOrderService().selectAllByOid(oid));
                serial_c.getItems().add(null);
                serialChangeEnable = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
