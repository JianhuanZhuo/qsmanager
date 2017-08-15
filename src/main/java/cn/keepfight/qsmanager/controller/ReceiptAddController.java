package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

/**
 * 新增产品界面控制器
 * Created by tom on 2017/6/7.
 */
public class ReceiptAddController implements DialogContent<ReceiptModelFull> {

    @FXML
    private VBox root;

    @FXML
    private TextField serial;
    @FXML
    private DatePicker rdate;
    @FXML
    private ChoiceBox<SupplyModel> sid;

    @FXML
    private Button item_add;
    @FXML
    private Button item_del;

    // 表格部分
    @FXML
    private TableView<ReceiptDetailModel> tabs;
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
    private Label all_total;

    // 子界面
    private ReceiptItemAddController addController;

    @FXML
    public void initialize() {
        FXUtils.limitLength(serial, 30);

        // 设置为当前时间
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        rdate.setValue(localDate);


        FXWidgetUtil.connect(tab_serial, ReceiptDetailModel::serialProperty);
        FXWidgetUtil.connect(tab_name, ReceiptDetailModel::nameProperty);
        FXWidgetUtil.connect(tab_color, ReceiptDetailModel::colorProperty);
        FXWidgetUtil.connect(tab_spec, ReceiptDetailModel::specProperty);
        FXWidgetUtil.connect(tab_unit, ReceiptDetailModel::unitProperty);
        FXWidgetUtil.connectDecimalObj(tab_price, ReceiptDetailModel::priceProperty);
        FXWidgetUtil.connectDecimalObj(tab_num, ReceiptDetailModel::numProperty);
        FXWidgetUtil.connectDecimalObj(tab_total, ReceiptDetailModel::totalProperty);

        // 新增明细按钮
        item_add.setOnMouseClicked(e -> {
            addController.setSid(sid.getSelectionModel().getSelectedItem().getId());
            CustomDialog.gen().build(addController).ifPresent(tabs.getItems()::add);
        });

        // 删除明细按钮
        item_del.setOnMouseClicked(event -> tabs.getItems().remove(tabs.getSelectionModel().getSelectedIndex()));

        // 删除按钮有效性
        item_del.disableProperty().bind(tabs.getSelectionModel().selectedItemProperty().isNull());
        item_add.disableProperty().bind(sid.getSelectionModel().selectedItemProperty().isNull());


        // 设置SID下来文字转换
        sid.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName()));

        // 设置自动计算总金额
        FXWidgetUtil.calculate(tabs.getItems(), i -> i.getPrice().multiply(i.getNum()), all_total::setText);

        // 禁用手动输入
        rdate.getEditor().setDisable(true);

        // 双击原料表进行编辑
        FXWidgetUtil.doubleToEdit(tabs, ()->addController,
                ReceiptDetailModel::update,
                () -> addController.setSid(sid.getSelectionModel().getSelectedItem().getId()));
    }

    @Override
    public void init() {
        serial.setText("");
        sid.getSelectionModel().clearSelection();
        tabs.getItems().clear();
        // 加载列表
        loadSupply();

        // 加载 FXML
        if (addController == null) {
            Platform.runLater(() -> {
                try {
                    addController = ViewPathUtil.loadViewForController("receipt_item_add.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void fill(ReceiptModelFull receiptModelFull) {
        serial.setText(receiptModelFull.getSerial());
        Platform.runLater(() -> {
            for (SupplyModel supplyModel : sid.getItems()) {
                System.out.println("rfullid:" + receiptModelFull.getSid());
                System.out.println("sid:" + supplyModel.getId());
                if (supplyModel.getId().equals(receiptModelFull.getSid())) {
                    sid.getSelectionModel().select(supplyModel);
                    break;
                }
            }
        });
        rdate.setValue(FXUtils.stampToLocalDate(receiptModelFull.getRdate()));
        tabs.getItems().setAll(receiptModelFull.getDetailList());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(serial.textProperty().isNotEmpty()
                .and(sid.getSelectionModel().selectedItemProperty().isNotNull())
        );
        return res;
    }

    @Override
    public ReceiptModelFull pack() {
        ReceiptModelFull res = new ReceiptModelFull();
        res.setSerial(serial.getText());
        res.setRdate(Date.valueOf(rdate.getValue()).getTime());
        res.setSid(sid.getSelectionModel().getSelectedItem().getId());
        res.setDetailList(tabs.getItems());
        return res;
    }


    private void loadSupply() {
        Platform.runLater(() -> {
            try {
                sid.getItems().setAll(QSApp.service.getSupplyService().selectAll());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
