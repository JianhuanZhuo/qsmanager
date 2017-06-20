package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 提交确认界面控制器
 * Created by tom on 2017/6/19.
 */
public class SumupController implements DialogContent<OrderModelFull>, Initializable {

    @FXML
    private VBox root;
    // 表格
    @FXML
    private TableView<OrderItemModel> table;
    @FXML
    private TableColumn<OrderItemModel, String> tab_serial;
    @FXML
    private TableColumn<OrderItemModel, String> tab_name;
    @FXML
    private TableColumn<OrderItemModel, String> tab_detail;
    @FXML
    private TableColumn<OrderItemModel, String> tab_price;
    @FXML
    private TableColumn<OrderItemModel, String> tab_pack;
    @FXML
    private TableColumn<OrderItemModel, String> tab_num;
    @FXML
    private TableColumn<OrderItemModel, String> tab_total;

    @FXML
    private TextField msg;
    @FXML
    private Label total;
    @FXML
    private ChoiceBox<CustomModel> cust;


    // 内容模型
    private OrderModelFull modelFull;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_serial.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getSerial()));
        tab_name.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        tab_detail.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDetail()));
        tab_price.setCellValueFactory(e -> new SimpleStringProperty("￥" + e.getValue().getPrice()));
        tab_pack.setCellValueFactory(e -> {
            if (e.getValue().getPack() == 1L) {
                return new SimpleStringProperty("散装");
            } else {
                return new SimpleStringProperty("整装(" + e.getValue().getPack() + ")");
            }
        });
        tab_num.setCellValueFactory(e -> new SimpleStringProperty("" + (e.getValue().getNum())
                + ((e.getValue().getPack() == 1L) ? "个" : e.getValue().getUnit())));
        tab_total.setCellValueFactory(e -> new SimpleStringProperty("" + e.getValue().getNum()
                .multiply(e.getValue().getPrice())
                .multiply(new BigDecimal(e.getValue().getPack()))));

        FXWidgetUtil.calculate(table.getItems(),
                e -> e.getNum().multiply(new BigDecimal(e.getPack())).multiply(e.getPrice()),
                total::setText);


        cust.setConverter(new StringConverter<CustomModel>() {
            @Override
            public String toString(CustomModel object) {
                return object.getSerial()+"-"+object.getName();
            }

            @Override
            public CustomModel fromString(String string) {
                return null;
            }
        });

        loadCustom();
    }

    @Override
    public void init() {
        msg.setText("");
    }

    @Override
    public void fill(OrderModelFull model) {
        this.modelFull = model;
        table.getItems().setAll(model.getOrderItemModels());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public OrderModelFull pack() {
        //@TODO 生成订单号？？？
        Long cid = cust.getSelectionModel().getSelectedItem().getId();
        modelFull.setCid(cid);
        modelFull.setOrderdate(System.currentTimeMillis());
        modelFull.setNote(msg.getText());
        return modelFull;
    }


    // 加载客户列表
    private void loadCustom() {
        Platform.runLater(() -> {
            try {
                cust.getItems().setAll(FXCollections.observableList(QSApp.service.getCustomService().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
