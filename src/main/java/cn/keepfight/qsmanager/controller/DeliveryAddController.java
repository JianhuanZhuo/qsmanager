package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
public class DeliveryAddController implements DialogContent<DeliveryModelFull> {

    @FXML
    private VBox root;

    @FXML
    private TextField dserial;
    @FXML
    private DatePicker ddate;
    @FXML
    private ChoiceBox<CustomModel> cid;
    @FXML
    private ChoiceBox<OrderModel> oserial;

    @FXML
    private Button item_add;
    @FXML
    private Button item_del;

    // 表格部分
    @FXML
    private TableView<DeliveryItemModel> table;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_name;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_serial;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_detail;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_price;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_unit;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_pack;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_num;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_total;
    @FXML
    private TableColumn<DeliveryItemModel, String> tab_note;
    @FXML
    private Label s_num;
    @FXML
    private Label s_total;


    private static final String NO_SERIAL = "订单号待生成";
    // 子界面
    private DeliveryItemAddController addController;

    @FXML
    public void initialize() {

        FXUtils.limitLength(dserial, 30);

        // 设置为当前时间
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ddate.setValue(localDate);

        FXWidgetUtil.connect(tab_name, DeliveryItemModel::nameProperty);
        FXWidgetUtil.connect(tab_serial, DeliveryItemModel::serialProperty);
        FXWidgetUtil.connect(tab_detail, DeliveryItemModel::detailProperty);
        FXWidgetUtil.connectObj(tab_price, DeliveryItemModel::priceProperty);
        FXWidgetUtil.connectNum(tab_pack, DeliveryItemModel::packProperty);
        FXWidgetUtil.connect(tab_unit, DeliveryItemModel::unitProperty);
        FXWidgetUtil.connectObj(tab_num, DeliveryItemModel::numProperty);
        FXWidgetUtil.connectObj(tab_total, DeliveryItemModel::totalProperty);
        FXWidgetUtil.connect(tab_note, DeliveryItemModel::noteProperty);

        FXWidgetUtil.calculate(table.getItems(), DeliveryItemModel::getNum, s_num::setText);
        FXWidgetUtil.calculate(table.getItems(), DeliveryItemModel::getTotal, s_total::setText);

        // 新增明细按钮
        item_add.setOnMouseClicked(e -> {
            addController.setOid(oserial.getSelectionModel().getSelectedItem().getId());
            CustomDialog.gen().build(addController).ifPresent(table.getItems()::add);
        });

        // 删除明细按钮
        item_del.setOnMouseClicked(event -> table.getItems().remove(table.getSelectionModel().getSelectedIndex()));

        // 删除按钮有效性
        item_del.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        item_add.disableProperty().bind(oserial.getSelectionModel().selectedItemProperty().isNull());

        oserial.disableProperty().bind(cid.getSelectionModel().selectedItemProperty().isNull());
        cid.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==null){
                return;
            }
            loadOrders();
        });

        // 设置SID下来文字转换
        cid.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName()));
        oserial.setConverter(FXUtils.converter(OrderModel::getSerial));

        // 禁用手动输入
        ddate.getEditor().setDisable(true);

        // 双击原料表进行编辑
        table.setRowFactory(tv -> {
            TableRow<DeliveryItemModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    OrderModel s = oserial.getSelectionModel().getSelectedItem();
                    if (s==null){
                        return;
                    }
                    DeliveryItemModel rowData = row.getItem();
                    addController.setOid(s.getId());
                    Optional<DeliveryItemModel> op = CustomDialog.gen().build(addController, rowData);
                    op.ifPresent(rowData::update);
                    QSUtil.refreshItems(table.getItems(), rowData);
                }
            });
            return row;
        });
    }

    @Override
    public void init() {
        dserial.setText(NO_SERIAL);
        cid.getSelectionModel().clearSelection();
        oserial.getSelectionModel().clearSelection();
        table.getItems().clear();
        // 加载列表
        loadCust();

        // 加载 FXML
        if(addController==null){
            Platform.runLater(() -> {
                try {
                    addController = ViewPathUtil.loadViewForController("delivery_item_add.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void fill(DeliveryModelFull model) {
        if (model.getSerial()!=null){
            dserial.setText(model.getSerial());
        }
        if (model.getDdate()!=null){
            ddate.setValue(FXUtils.stampToLocalDate(model.getDdate()));
        }
        if (model.getDeliveryItemModels()!=null){
            table.getItems().setAll(model.getDeliveryItemModels());
        }
        Platform.runLater(()->{
            if (model.getCid()!=null){
                for (CustomModel cust : cid.getItems()) {
                    if (cust.getId().equals(model.getCid())){
                        cid.getSelectionModel().select(cust);
                        break;
                    }
                }
            }
            if (model.getOrder_serial()!=null){
                Platform.runLater(()->{
                    for (OrderModel o : oserial.getItems()) {
                        if (o.getSerial().equals(model.getOrder_serial())){
                            oserial.getSelectionModel().select(o);
                            break;
                        }
                    }
                });
            }
        });
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(dserial.textProperty().isNotEmpty()
                .and(cid.getSelectionModel().selectedItemProperty().isNotNull())
                .and(oserial.getSelectionModel().selectedItemProperty().isNotNull())
        );
        return res;
    }

    @Override
    public DeliveryModelFull pack() {
        DeliveryModelFull res = new DeliveryModelFull();
        res.setSerial(dserial.getText().equals(NO_SERIAL)?null: dserial.getText());
        res.setOrder_serial(oserial.getValue().getSerial());
        res.setDdate(Date.valueOf(ddate.getValue()).getTime());
        CustomModel c = cid.getSelectionModel().getSelectedItem();
        res.setCid(c.getId());
        res.setCust(c.getSerial()+"-"+c.getName());
        res.setDeliveryItemModels(table.getItems());
        return res;
    }


    private void loadCust() {
        Platform.runLater(() -> {
            try {
                cid.getItems().setAll(QSApp.service.getCustomService().selectAll());
                cid.getItems().forEach(x-> System.out.println("哇哈咧：：："+x.getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadOrders(){
        Platform.runLater(() -> {
            try {
                oserial.getItems().setAll(QSApp.service.getOrderService().selectByCid(cid.getSelectionModel().getSelectedItem().getId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
