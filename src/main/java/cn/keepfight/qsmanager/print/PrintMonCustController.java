package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.ConfigUtil;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.Bindings;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintMonCustController extends PrintTemplate<List<OrderModelFull>> implements Initializable {

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

    public TableColumn<Item, BigDecimal> tab_rebate;
    public TableColumn<Item, BigDecimal> tab_total_rebate;
    public TableColumn<Item, BigDecimal> tab_delifee;

    public TextField total_mon;
    public TextField total_num;
    public TextField total_rebate;
    public TextField total_delifee;
    public TextField total_act;
    private static final int SIZE_PER_PAGE = 12;

    private Long cid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(52).divide(SIZE_PER_PAGE));
        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        FXWidgetUtil.cellDecimal(tab_price, tab_num, tab_total, tab_delifee, tab_total_rebate);
        FXWidgetUtil.cellLong(tab_pack);
        FXWidgetUtil.cellStr(tab_name, tab_serial, tab_detail, tab_unit);
        tab_date.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.timestampConverter()));

        tab_date.setCellValueFactory(x -> x.getValue().tab_dateProperty().asObject());
        tab_name.setCellValueFactory(x -> x.getValue().nameProperty());
        tab_serial.setCellValueFactory(x -> x.getValue().serialProperty());
        tab_detail.setCellValueFactory(x -> x.getValue().detailProperty());
        tab_price.setCellValueFactory(x -> x.getValue().priceProperty());
        tab_unit.setCellValueFactory(x -> x.getValue().unitProperty());
        tab_pack.setCellValueFactory(x -> x.getValue().packProperty().asObject());
        tab_num.setCellValueFactory(x -> x.getValue().numProperty());
        tab_total.setCellValueFactory(x -> x.getValue().tab_totalProperty());
        tab_rebate.setCellValueFactory(x -> x.getValue().rebateProperty());
        tab_total_rebate.setCellValueFactory(x -> x.getValue().total_rebateProperty());
        tab_delifee.setCellValueFactory(x -> x.getValue().delifeeProperty());

        tab_rebate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(List<OrderModelFull> datas) {
        List<Item> items = datas.stream()
                .flatMap(x -> x.getOrderItemModels().stream()
                        .map(m -> new Pair<>(x.getOrderdate(), m)))
                .map(x -> {
                    Item item = new Item(x.getValue());
                    item.setTab_date(x.getKey());
                    return item;
                })
                .collect(Collectors.toList());
        this.cid = null;
        if (!datas.isEmpty()) {
            this.cid = datas.get(0).getCid();
            // 填充客户信息
            try {
                CustomModel c = QSApp.service.getCustomService().selectAllByID(this.cid);
                cust.setText(c.getNamefull());
                addr.setText(c.getAddr());
                phone.setText(c.getPhone());
                contract.setText("");

                // 加载默认记忆选项并添加默认下拉
                FXWidgetUtil.defaultList(
                        new Pair<>(addr, "custom.info.addr."+c.getSerial())
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));
        mdate.setText(FXUtils.stampToDate(System.currentTimeMillis()));

        total_num.setText("0");
        total_act.setText("0");
        total_rebate.setText("0");
        total_delifee.setText("0");
        total_mon.setText("0");

        // 初始化数据显示
        table.getItems().setAll(items);
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
            x.setTab_total(x.getTakeTotal());
            x.setTotal_rebate(x.getRebateTotal());
        });

        FXWidgetUtil.compute(table.getItems(),
                Item::getTakeTotal,
                total_mon::setText);
        FXWidgetUtil.compute(table.getItems(),
                Item::getRebateTotal,
                total_rebate::setText);
        FXWidgetUtil.compute(table.getItems(),
                Item::getNum,
                total_num::setText);
        FXWidgetUtil.compute(table.getItems(),
                Item::getDelifee,
                total_delifee::setText);

        // 总应付金额
        total_act.setText(
                FXUtils.getDecimal(total_mon.getText(), new BigDecimal(0))
                        .add(FXUtils.getDecimal(total_delifee.getText(), new BigDecimal(0)))
                        .subtract(FXUtils.getDecimal(total_rebate.getText(), new BigDecimal(0))).stripTrailingZeros().toPlainString()
        );
    }

    @Override
    public void printBefore() {
        // 保存信息
        try {
            ConfigUtil.alter("fxapp.properties", "print.maker", maker.getText());
            CustomModel c = QSApp.service.getCustomService().selectAllByID(this.cid );
            c.setNamefull(cust.getText());
            c.setPhone(phone.getText());
            c.setAddr(addr.getText());
            QSApp.service.getCustomService().update(c);

            FXWidgetUtil.addDefaultList(
                    new Pair<>("custom.info.addr."+c.getSerial(), addr.getText())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Item extends OrderItemModel {
        LongProperty tab_date = new SimpleLongProperty();
        private ObjectProperty<BigDecimal> tab_total = new SimpleObjectProperty<>();
        private ObjectProperty<BigDecimal> total_rebate = new SimpleObjectProperty<>();

        Item(OrderItemModel m) {
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

        public BigDecimal getTotal_rebate() {
            return total_rebate.get();
        }

        public ObjectProperty<BigDecimal> total_rebateProperty() {
            return total_rebate;
        }

        public void setTotal_rebate(BigDecimal total_rebate) {
            this.total_rebate.set(total_rebate);
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
