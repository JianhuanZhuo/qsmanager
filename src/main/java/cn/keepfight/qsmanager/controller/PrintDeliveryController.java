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
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintDeliveryController extends PrintTemplate<DeliveryModelFull> implements Initializable {

    public TextField serial;
    public TextField cust;
    public TextField addr;
    public TextField phone;
    public TextField contract;
    public TextField maker;
    public TextField ddate;

    public TableView<Item> table;
    public TableColumn<Item, Number> id;
    public TableColumn<Item, String> name;
    public TableColumn<Item, String> detail;
    public TableColumn<Item, String> unit;
    public TableColumn<Item, BigDecimal> num;
    public TableColumn<Item, BigDecimal> price;
    public TableColumn<Item, BigDecimal> total;
    public TableColumn<Item, String> note;
    public VBox root;
    public TextField all_total;

    private static final int SIZE_PER_PAGE = 8;
    private DeliveryModelFull datas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(27).divide(SIZE_PER_PAGE));

        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        detail.setCellFactory(TextFieldTableCell.forTableColumn());
        unit.setCellFactory(TextFieldTableCell.forTableColumn());
        price.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        num.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        total.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        note.setCellFactory(TextFieldTableCell.forTableColumn());
        total.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        price.setCellValueFactory(x -> x.getValue().priceProperty());
        num.setCellValueFactory(x -> x.getValue().numProperty());
        total.setCellValueFactory(x -> x.getValue().totalItem);
        name.setCellValueFactory(x -> x.getValue().nameItem);

        FXWidgetUtil.connect(detail, DeliveryItemModel::detailProperty);
        FXWidgetUtil.connect(unit, DeliveryItemModel::unitProperty);
        FXWidgetUtil.connect(note, DeliveryItemModel::noteProperty);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(DeliveryModelFull datas) {
        System.out.println("fill:"+datas.getSerial());
        System.out.println("fill_id:"+datas.getId());
        this.datas = datas;

        serial.setText(datas.getSerial());

        // 填充客户信息
        try {
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            cust.setText(c.getNamefull());
            addr.setText(c.getAddr());
            phone.setText(c.getPhone());
            contract.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 填写其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));
        ddate.setText(FXUtils.stampToDate(System.currentTimeMillis()));
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
                            i * SIZE_PER_PAGE,
                            Math.min((i + 1) * SIZE_PER_PAGE, datas.getDeliveryItemModels().size())
                    )
            );
        } catch (Exception e) {
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
                .peek(x->x.totalItem.set(x.getTotal()))
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

        ObjectProperty<BigDecimal> totalItem = new SimpleObjectProperty<>(new BigDecimal(0));
        StringProperty nameItem = new SimpleStringProperty();

    }
}
