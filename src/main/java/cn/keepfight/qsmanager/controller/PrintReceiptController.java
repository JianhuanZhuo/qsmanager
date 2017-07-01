package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.DeliveryItemModel;
import cn.keepfight.qsmanager.model.DeliveryModelFull;
import cn.keepfight.utils.ConfigUtil;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 收据单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintReceiptController extends PrintTemplate<DeliveryModelFull> implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private TextField serial;
    @FXML
    private TextField cust;
    @FXML
    private TextField addr;
    @FXML
    private TextField phone;
    @FXML
    private TextField maker;
    @FXML
    private TextField mdate;

    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, Number> id;
    @FXML
    private TableColumn<Item, String> name;
    @FXML
    private TableColumn<Item, String> detail;
    @FXML
    private TableColumn<Item, String> unit;
    @FXML
    private TableColumn<Item, BigDecimal> num;
    @FXML
    private TableColumn<Item, BigDecimal> price;
    @FXML
    private TableColumn<Item, Integer> yuan_u4;
    @FXML
    private TableColumn<Item, Integer> yuan_u3;
    @FXML
    private TableColumn<Item, Integer> yuan_u2;
    @FXML
    private TableColumn<Item, Integer> yuan_u1;
    @FXML
    private TableColumn<Item, Integer> yuan;
    @FXML
    private TableColumn<Item, Integer> yuan_d1;
    @FXML
    private TableColumn<Item, Integer> yuan_d2;
    @FXML
    private TextField all_total;

    private DeliveryModelFull datas;
    private static final int SIZE_PER_PAGE = 8;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(51).divide(SIZE_PER_PAGE));

        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        name.setCellValueFactory(x -> x.getValue().nameItem);
        FXWidgetUtil.connect(detail, Item::detailProperty);
        FXWidgetUtil.connect(unit, Item::unitProperty);
        num.setCellValueFactory(x->x.getValue().numProperty());
        price.setCellValueFactory(x->x.getValue().priceProperty());
        yuan_u4.setCellValueFactory(x->x.getValue().yuan_u4.asObject());
        yuan_u3.setCellValueFactory(x->x.getValue().yuan_u3.asObject());
        yuan_u2.setCellValueFactory(x->x.getValue().yuan_u2.asObject());
        yuan_u1.setCellValueFactory(x->x.getValue().yuan_u1.asObject());
        yuan.setCellValueFactory(x->x.getValue().yuan.asObject());
        yuan_d1.setCellValueFactory(x->x.getValue().yuan_d1.asObject());
        yuan_d2.setCellValueFactory(x->x.getValue().yuan_d2.asObject());

        table.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        detail.setCellFactory(TextFieldTableCell.forTableColumn());
        price.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        num.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        unit.setCellFactory(TextFieldTableCell.forTableColumn());
        yuan_u4.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan_u3.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan_u2.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan_u1.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan_d1.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
        yuan_d2.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.integerConverter("0")));
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void fill(DeliveryModelFull datas) {
        this.datas = datas;

        serial.setText(datas.getSerial());

        // 填充客户信息
        try {
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            cust.setText(c.getNamefull());
            addr.setText(c.getAddr());
            phone.setText(c.getPhone());

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 填写其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));
        mdate.setText(FXUtils.stampToDate(System.currentTimeMillis()));
        selectPage(0);
    }

    @Override
    public IntegerBinding pageNum() {
        return new IntegerBinding() {
            {
                bind(table.getItems());
            }
            @Override
            protected int computeValue() {
                if (datas == null) {
                    return 0;
                } else {
                    return (int) Math.ceil( datas.getDeliveryItemModels().size() / (double)SIZE_PER_PAGE);
                }
            }
        };
    }

    @Override
    public void selectPage(int i) {
        try {
            table.getItems().setAll(
                    datas.getDeliveryItemModels().stream().map(Item::new).collect(Collectors.toList()).subList(
                            i*SIZE_PER_PAGE,
                            Math.min((i+1)*SIZE_PER_PAGE, datas.getDeliveryItemModels().size())
                    )
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public void autoCalculate() {
        Optional t = table.getItems().stream()
                .peek(x->{
                    BigDecimal d = x.getTotal();
                    //@TODO 搞完回来弄
                    x.yuan_u4.set(FXUtils.getNumAt(d, 5, true));
                    x.yuan_u3.set(FXUtils.getNumAt(d, 4, true));
                    x.yuan_u2.set(FXUtils.getNumAt(d, 3, true));
                    x.yuan_u1.set(FXUtils.getNumAt(d, 2, true));
                    x.yuan.set(FXUtils.getNumAt(d, 1, true));
                    x.yuan_d1.set(FXUtils.getNumAt(d, 1, false));
                    x.yuan_d2.set(FXUtils.getNumAt(d, 2, false));
                })
                .map(DeliveryItemModel::getTotal)
                .reduce(BigDecimal::add);
        String text = "0";
        if (t.isPresent()) {
            text = t.get().toString();
        }
        all_total.setText(text);
    }

    private class Item extends DeliveryItemModel {
        Item(DeliveryItemModel m) {
            setNote(m.getNote());
            setDetail(m.getDetail());
            setDid(m.getDid());
            setId(m.getId());
            setName(m.getName());
            setNum(m.getNum());
            setPack(m.getPack());
            setPrice(m.getPrice());
            setSerial(m.getSerial());
            setUnit(m.getUnit());
            nameItem.set(getSerial()+"-"+getName());
        }
        IntegerProperty yuan_u4 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u3 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u2 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u1 = new SimpleIntegerProperty(0);
        IntegerProperty yuan = new SimpleIntegerProperty(0);
        IntegerProperty yuan_d1 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_d2 = new SimpleIntegerProperty(0);

        StringProperty nameItem = new SimpleStringProperty();

    }
}
