package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 供应商供应送货单控制器
 * Created by tom on 2017/6/11.
 */
public class ReceiptController implements ContentCtrl, Initializable{

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

    @FXML
    private Button update;

    // 内部表现数据
    private ReceiptModelFull modelFull;
    private ReceiptListController outComeController;

    // 静态
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {

    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("供应页面");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_serial.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSerial()));
        tab_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        tab_color.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getColor()));
        tab_spec.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSpec()));
        tab_unit.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUnit()));
        FXWidgetUtil.connectDecimal(tab_price, ReceiptDetailModel::getPrice);
        FXWidgetUtil.connectDecimal(tab_num, ReceiptDetailModel::getNum);
        FXWidgetUtil.connectDecimal(tab_total, x->x.getPrice().multiply(x.getNum()));

        del.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("是否要删除这条记录？");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                outComeController.deleteSelected(modelFull);
            }
        });

        update.setOnAction(event -> outComeController.updateReceipt(modelFull));
    }

    public void fill(ReceiptModelFull modelFull, ReceiptListController outComeController) {
        this.modelFull = modelFull;
        this.outComeController = outComeController;

        // 填充数据
        rdate.setText(formatter.format(new Date(modelFull.getRdate())));
        serial.setText(modelFull.getSerial());
        supply.setText(modelFull.getSupply());
        table.getItems().setAll(modelFull.getDetailList());

        // 计算总额
        FXWidgetUtil.compute(table.getItems(), i -> i.getPrice().multiply(i.getNum()), total::setText);
    }

}
