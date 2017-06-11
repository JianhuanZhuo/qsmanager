package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by tom on 2017/6/11.
 */
public class ReceiptController implements ContentController {

    @FXML
    private VBox root;

    @FXML
    private Label rdate;
    @FXML
    private Label serial;
    @FXML
    private Label supply;

    //
    @FXML
    private TableView<ReceiptDetailModel> table;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_serial;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_name;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_color;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_spec;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_unit;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_price;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_num;
    @FXML
    private TableColumn<ReceiptDetailModel, String> tab_total;

    @FXML
    private Label total;

    @FXML
    private Button del;
    @FXML
    private Button attach;

    // 静态
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void refresh() {
    }

    @FXML
    public void initialize() {
        tab_serial.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSerial()));
        tab_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        tab_color.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getColor()));
        tab_spec.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSpec()));
        tab_unit.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUnit()));
        tab_price.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPrice().toString()));
        tab_num.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNum().toString()));
        tab_total.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPrice().multiply(param.getValue().getNum()).toString()));
    }

    public void fill(ReceiptModelFull modelFull) {
        rdate.setText(formatter.format(new Date(modelFull.getRdate())));

        serial.setText(modelFull.getSerial());
        supply.setText(modelFull.getSid().toString());

        table.getItems().setAll(modelFull.getDetailList());

        Optional t = table.getItems().stream()
                .map(i -> i.getPrice().multiply(i.getNum()))
                .reduce(BigDecimal::add);
        String text = "";
        if (t.isPresent()) {
            text += t.get().toString();
        }
        total.setText(text);
    }
}
