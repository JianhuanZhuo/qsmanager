package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.qsmanager.model.OrderModelFull;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintMonCustRateController extends PrintTemplate<List<OrderModelFull>> implements Initializable {

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

    public TableColumn<Item, BigDecimal> tab_rate;
    public TableColumn<Item, BigDecimal> tab_total_rate;
    public TableColumn<Item, BigDecimal> tab_total_with_rate;

    public TableColumn<Item, BigDecimal> tab_rebate;
    public TableColumn<Item, BigDecimal> tab_total_rebate;
    public TableColumn<Item, BigDecimal> tab_delifee;

    public TextField total_mon;
    public TextField total_num;
    public TextField total_rebate;
    public TextField total_delifee;
    public TextField total_act;
    public TextField total_mon_with_rate;

    private static final int SIZE_PER_PAGE = 12;


    private Long cid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(45).divide(SIZE_PER_PAGE));
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

        tab_rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.rateConverter()));
        tab_total_rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_total_with_rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        tab_total_rebate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        tab_delifee.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));

        tab_date.setCellValueFactory(x -> x.getValue().tab_dateProperty().asObject());
        tab_name.setCellValueFactory(x -> x.getValue().nameProperty());
        tab_serial.setCellValueFactory(x -> x.getValue().serialProperty());
        tab_detail.setCellValueFactory(x -> x.getValue().detailProperty());
        tab_price.setCellValueFactory(x -> x.getValue().priceProperty());
        tab_unit.setCellValueFactory(x -> x.getValue().unitProperty());
        tab_pack.setCellValueFactory(x -> x.getValue().packProperty().asObject());
        tab_num.setCellValueFactory(x -> x.getValue().numProperty());
        tab_total.setCellValueFactory(x -> x.getValue().tab_totalProperty());

        tab_rate.setCellValueFactory(x -> x.getValue().rateProperty());
        tab_total_rate.setCellValueFactory(x -> x.getValue().rate_totalProperty());
        tab_total_with_rate.setCellValueFactory(x -> x.getValue().total_with_rateProperty());

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

        total_num.setText("0");
        total_act.setText("0");
        total_rebate.setText("0");
        total_delifee.setText("0");
        total_mon.setText("0");
        total_mon_with_rate.setText("0");

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
            x.setRate_total(x.getRateTotal());
            x.setTotal_with_rate(x.getTotalWithRate());
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
        FXWidgetUtil.compute(table.getItems(),
                Item::getTotal_with_rate,
                total_mon_with_rate::setText);

        // 总应付金额
        total_act.setText(
                FXUtils.getDecimal(total_mon_with_rate.getText(), new BigDecimal(0))
                        .add(FXUtils.getDecimal(total_delifee.getText(), new BigDecimal(0)))
                        .subtract(FXUtils.getDecimal(total_rebate.getText(), new BigDecimal(0))).toString()
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

        private ObjectProperty<BigDecimal> rate_total = new SimpleObjectProperty<>();
        private ObjectProperty<BigDecimal> total_with_rate = new SimpleObjectProperty<>();

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

        public BigDecimal getRate_total() {
            return rate_total.get();
        }

        public ObjectProperty<BigDecimal> rate_totalProperty() {
            return rate_total;
        }

        public void setRate_total(BigDecimal rate_total) {
            this.rate_total.set(rate_total);
        }

        public BigDecimal getTotal_with_rate() {
            return total_with_rate.get();
        }

        public ObjectProperty<BigDecimal> total_with_rateProperty() {
            return total_with_rate;
        }

        public void setTotal_with_rate(BigDecimal total_with_rate) {
            this.total_with_rate.set(total_with_rate);
        }

        public BigDecimal getRateTotal(){
            try {
                return getRate().multiply(getTakeTotal());
            } catch (Exception e) {
                return new BigDecimal(0);
            }
        }

        public BigDecimal getTotalWithRate(){
            try {
                return getRateTotal().add(getTakeTotal());
            } catch (Exception e) {
                return new BigDecimal(0);
            }
        }

        public BigDecimal getRebateTotal() {
            try {
                return getRebate().multiply(getNum());
            } catch (Exception e) {
                return new BigDecimal(0);
            }
        }
    }
}
