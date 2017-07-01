package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.DeliveryItemModel;
import cn.keepfight.qsmanager.model.DeliveryModelFull;
import cn.keepfight.qsmanager.model.SupAnnualMonModel;
import cn.keepfight.utils.ConfigUtil;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintMonCustController extends PrintTemplate<List<DeliveryModelFull>> implements Initializable {

    public VBox root;

    public TextField cust;
    public TextField addr;
    public TextField phone;
    public TextField contract;
    public TextField maker;
    public TextField mdate;
    public TableView<Item> table;
    public TableColumn<Item, Number> id;
    public TableColumn<Item, Long> tab_date;
    public TableColumn<Item, String> tab_name;
    public TableColumn<Item, String> tab_serial;
    public TableColumn<Item, String> tab_detail;
    public TableColumn<Item, BigDecimal> tab_price;
    public TableColumn<Item, String> tab_unit;
    public TableColumn<Item, Long> tab_pack;
    public TableColumn<Item, BigDecimal> tab_num;
    public TableColumn<Item, BigDecimal> tab_total;
    public TableColumn<Item, BigDecimal> rebate;
    public TableColumn<Item, BigDecimal> total_rebate;
    public TableColumn<Item, BigDecimal> delifee;

    public TextField total_mon;
    private static final int SIZE_PER_PAGE = 12;

    private List<Item> items;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(52).divide(SIZE_PER_PAGE));
        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        tab_date.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.timestampConverter()));
        tab_name.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_serial.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_detail.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_price.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_unit.setCellFactory(TextFieldTableCell.forTableColumn());
        tab_pack.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.longConverter("0")));
        tab_num.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_total.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        total_rebate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        delifee.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        tab_date.setCellValueFactory(x -> x.getValue().tab_dateProperty().asObject());
        tab_name.setCellValueFactory(x -> x.getValue().nameProperty());
        tab_serial.setCellValueFactory(x -> x.getValue().serialProperty());
        tab_detail.setCellValueFactory(x -> x.getValue().detailProperty());
        tab_price.setCellValueFactory(x -> x.getValue().priceProperty());
        tab_unit.setCellValueFactory(x -> x.getValue().unitProperty());
        tab_pack.setCellValueFactory(x -> x.getValue().packProperty().asObject());
        tab_num.setCellValueFactory(x -> x.getValue().numProperty());
        tab_total.setCellValueFactory(x -> x.getValue().tab_totalProperty());
        rebate.setCellValueFactory(x -> x.getValue().rebateProperty());
        total_rebate.setCellValueFactory(x -> x.getValue().total_rebateProperty());
        delifee.setCellValueFactory(x -> x.getValue().delifeeProperty());

        rebate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public IntegerBinding pageNum() {
        return FXWidgetUtil.pageNumBind(()->items, List::size, SIZE_PER_PAGE, table.getItems());
    }

    @Override
    public void fill(List<DeliveryModelFull> datas) {
        items = datas.stream()
                .flatMap(x -> x.getDeliveryItemModels().stream()
                        .map(m -> new Pair<>(x.getDdate(), m)))
                .map(x -> {
                    Item item = new Item(x.getValue());
                    item.setTab_date(x.getKey());
                    return item;
                })
                .collect(Collectors.toList());

        if (!datas.isEmpty()){
            // 填充客户信息
            try {
                CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.get(0).getCid());
                cust.setText(c.getNamefull());
                addr.setText(c.getAddr());
                phone.setText(c.getPhone());
                contract.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));
        mdate.setText(FXUtils.stampToDate(System.currentTimeMillis()));

        // 初始化数据显示
        selectPage(0);
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
        c.bind(Bindings.size(table.getItems()).isEqualTo(0));
        return c;
    }

    @Override
    public void autoCalculate() {
        Optional t = table.getItems().stream()
                .peek(x->x.setTab_total(x.getTotal()))
                .peek(x->{
                    BigDecimal d = new BigDecimal(0);
                    try {
                        d = x.getRebate().multiply(x.getNum());
                    }catch (Exception e){
                        // nothing to do
                    }
                    x.setTotal_rebate(d);
                })
                .map(DeliveryItemModel::getTotal)
                .reduce(BigDecimal::add);
        String text = "0";
        if (t.isPresent()) {
            text = t.get().toString();
        }
        total_mon.setText(text);
    }

    private static class Item extends DeliveryItemModel {
        LongProperty tab_date = new SimpleLongProperty();
        private ObjectProperty<BigDecimal> tab_total = new SimpleObjectProperty<>();
        private ObjectProperty<BigDecimal> rebate = new SimpleObjectProperty<>();
        private ObjectProperty<BigDecimal> total_rebate = new SimpleObjectProperty<>();
        private ObjectProperty<BigDecimal> delifee = new SimpleObjectProperty<>();

        Item(DeliveryItemModel m) {
            super(m);
        }

        public void setTab_date(long tab_date) {
            this.tab_date.set(tab_date);
        }

        public long getTab_date() {
            return tab_date.get();
        }

        public LongProperty tab_dateProperty() {
            return tab_date;
        }

        public BigDecimal getRebate() {
            return rebate.get();
        }

        public ObjectProperty<BigDecimal> rebateProperty() {
            return rebate;
        }

        public void setRebate(BigDecimal rebate) {
            this.rebate.set(rebate);
        }

        public BigDecimal getTotal_rebate() {
            return total_rebate.get();
        }

        public ObjectProperty<BigDecimal> total_rebateProperty() {
            return total_rebate;
        }

        public void setTotal_rebate(BigDecimal total_rebate) {
            this.total_rebate.set(total_rebate);
        }

        public BigDecimal getDelifee() {
            return delifee.get();
        }

        public ObjectProperty<BigDecimal> delifeeProperty() {
            return delifee;
        }

        public void setDelifee(BigDecimal delifee) {
            this.delifee.set(delifee);
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
