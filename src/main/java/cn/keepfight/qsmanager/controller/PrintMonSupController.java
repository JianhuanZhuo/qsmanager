package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintMonSupController extends PrintTemplate<List<ReceiptModelFull>> implements Initializable {

    public VBox root;

    public TextField date;
    public TextField addr;
    public TextField serial;
    public TextField phone;
    public TextField name;
    public TextField fax;

    public TableView<Item> table;
    public TableColumn<Item, Number> id;
    public TableColumn<Item, Long> tab_date;
    public TableColumn<Item, String> tab_serial;
    public TableColumn<Item, String> tab_name;
    public TableColumn<Item, String> tab_color;
    public TableColumn<Item, String> tab_spec;
    public TableColumn<Item, BigDecimal> tab_num;
    public TableColumn<Item, String> tab_unit;
    public TableColumn<Item, BigDecimal> tab_price;
    public TableColumn<Item, BigDecimal> tab_total;

    public TextField total_mon;

    public Label resp_name;
    public Label resp_year;
    public Label resp_date;

    private int currentPage = 0;

    private static final int SIZE_PER_PAGE = 12;


    private List<Item> items;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(27).divide(SIZE_PER_PAGE));
        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        tab_date.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.timestampConverter()));
        tab_serial.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_name.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_color.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_spec.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_price.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_unit.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_num.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_total.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        tab_date.setCellValueFactory(x -> x.getValue().tab_dateProperty().asObject());
        tab_serial.setCellValueFactory(x -> x.getValue().serialProperty());
        tab_name.setCellValueFactory(x -> x.getValue().nameProperty());
        tab_color.setCellValueFactory(x -> x.getValue().colorProperty());
        tab_spec.setCellValueFactory(x -> x.getValue().specProperty());
        tab_price.setCellValueFactory(x -> x.getValue().priceProperty());
        tab_unit.setCellValueFactory(x -> x.getValue().unitProperty());
        tab_num.setCellValueFactory(x -> x.getValue().numProperty());
        tab_total.setCellValueFactory(x -> x.getValue().tab_totalProperty());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(List<ReceiptModelFull> datas) {
        items = datas.stream()
                .flatMap(x -> x.getDetailList().stream()
                        .map(m -> new Pair<>(new Pair<>(x.getRdate(), x.getSerial()), m)))
                .map(x -> {
                    Item item = new Item(x.getValue());
                    item.setTab_date(x.getKey().getKey());
                    item.setReceipt_serial(x.getKey().getValue());
                    return item;
                })
                .collect(Collectors.toList());


        if (!datas.isEmpty()) {
            // 填充客户信息
            try {
                SupplyModel s = QSApp.service.getSupplyService().selectByID(datas.get(0).getSid());
                name.setText(s.getNamefull());
                addr.setText(s.getAddr());
                phone.setText(s.getPhone());
                fax.setText(s.getFax());
                serial.setText(s.getSerial());

                // 底部信息
                resp_name.setText(s.getNamefull());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 填充客户信息
            try {
                date.setText(FXUtils.stampToDate(datas.get(0).getRdate(), "yyyy年MM月"));
                resp_year.setText(FXUtils.stampToDate(datas.get(0).getRdate(), "yyyy年"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 底部
        resp_date.setText(FXUtils.stampToDate(System.currentTimeMillis(), "yyyy年MM月"));

        total_mon.setText("0");

        // 初始化数据显示
        selectPage(0);
    }

    @Override
    public IntegerBinding pageNum() {
        return FXWidgetUtil.pageNumBind(() -> items, List::size, SIZE_PER_PAGE, table.getItems());
    }

    @Override
    public void selectPage(int i) {
        try {
            table.getItems().setAll(
                    items.subList(
                            i * SIZE_PER_PAGE,
                            Math.min((i + 1) * SIZE_PER_PAGE, items.size())
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        BooleanProperty c = new SimpleBooleanProperty();
        c.bind(Bindings.size(table.getItems()).isNotEqualTo(0));
        return c;
    }

    @Override
    public void autoCalculate() {
        table.getItems().forEach(x->{
            x.setTab_total(x.getTotal());
        });

        FXWidgetUtil.compute(table.getItems(),
                Item::getTotal,
                total_mon::setText);
    }

    private static class Item extends ReceiptDetailModel {
        LongProperty tab_date = new SimpleLongProperty();
        StringProperty receipt_serial = new SimpleStringProperty();
        private ObjectProperty<BigDecimal> tab_total = new SimpleObjectProperty<>();

        Item(ReceiptDetailModel m) {
            super(m);
        }

        public long getTab_date() {
            return tab_date.get();
        }

        public LongProperty tab_dateProperty() {
            return tab_date;
        }

        public void setTab_date(long tab_date) {
            this.tab_date.set(tab_date);
        }

        public String getReceipt_serial() {
            return receipt_serial.get();
        }

        public StringProperty receipt_serialProperty() {
            return receipt_serial;
        }

        public void setReceipt_serial(String receipt_serial) {
            this.receipt_serial.set(receipt_serial);
        }

        public BigDecimal getTab_total() {
            return tab_total.get();
        }

        public ObjectProperty<BigDecimal> tab_totalProperty() {
            return tab_total;
        }

        public void setTab_total(BigDecimal tab_total) {
            this.tab_total.set(tab_total);
        }
    }
}
