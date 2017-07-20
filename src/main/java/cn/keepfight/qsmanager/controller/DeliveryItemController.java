package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.qsmanager.model.DeliveryItemModel;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 订单记录控制器
 * Created by tom on 2017/6/11.
 */
public class DeliveryItemController implements ContentController, Initializable {

    @FXML
    private VBox root;
    @FXML
    private TableView<DeliveryItemModel> table;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_serial;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_name;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_detail;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_pack;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_price;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_unit;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_num;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_total;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_note;
    @FXML
    private Label o_date;
    @FXML
    private Label o_dserial;
    @FXML
    private Label o_serial;
    @FXML
    private Label o_cust;

    @FXML
    private Label s_num;
    @FXML
    private Label s_total;
    @FXML
    private Button a_del;
    @FXML
    private Button a_attach;
    @FXML
    private Button a_print;
    @FXML
    private Button a_alter;

    // 内部表现数据
    private DeliveryModelFull modelFull;
    private IncomeController controller;

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void loaded() {

    }

    @Override
    public void showed() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_name.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        tab_serial.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSerial()));
        tab_detail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDetail()));
        tab_price.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPrice().toString()));
        tab_pack.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPack() == 1L ? "散装" : "整装(" + param.getValue().getPack() + ")"));
        tab_unit.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUnit()));
        tab_num.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getNum().toString()
                + (param.getValue().getPack() == 1L ? "个" : param.getValue().getUnit())));
        tab_total.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTotal().toString()));
        tab_note.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getNote()));

        a_del.setOnAction(event -> WarningBuilder.delSimpleConfirm(()->controller.deleteSelected(modelFull)));
        a_alter.setOnAction(event -> controller.updateDelivery(modelFull));

        FXWidgetUtil.calculate(table.getItems(), DeliveryItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), DeliveryItemModel::getTotal, s_total::setText);


        a_print.setOnAction(event->{
            PrintSource source = new PrintSource();
            source.setCust(modelFull.getCid());
            LocalDate localDate = FXUtils.stampToLocalDate(modelFull.getDdate());
            source.setYear((long) localDate.getYear());
            source.setMonth((long) localDate.getMonthValue());
            source.setItem(modelFull.getId());
            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.DELIVERY, source));
        });
    }

    public void fill(DeliveryModelFull modelFull, IncomeController controller) {
        this.modelFull = modelFull;
        this.controller = controller;

        // 填充数据
        o_date.setText(FXUtils.stampToDate(modelFull.getDdate()));
        o_serial.setText(modelFull.getOrder_serial());
        o_dserial.setText(modelFull.getSerial());
        o_cust.setText(modelFull.getCust());

        table.getItems().setAll(modelFull.getDeliveryItemModels());
    }
}
