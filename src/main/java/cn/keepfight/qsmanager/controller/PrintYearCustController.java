package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustAnnualModelFull;
import cn.keepfight.qsmanager.model.CustAnnualMonModel;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintYearCustController extends PrintTemplate<CustAnnualModelFull> implements Initializable {

    public VBox root;
    public TextField date;
    public TextField addr;
    public TextField serial;
    public TextField phone;
    public TextField name;
    public TextField fax;
    public TextField pvacc;
    public TextField pvbcc;
    public TextField pbacc;
    public TextField pbbcc;
    public TableView<Item> table;
    public TableColumn<Item, String> mon;
    public TableColumn<Item, BigDecimal> total;
    public TableColumn<Item, String> billunit;
    public TableColumn<Item, Long> billdate;
    public TableColumn<Item, BigDecimal> billtotal;
    public TableColumn<Item, BigDecimal> rate;
    public TableColumn<Item, BigDecimal> ratetotal;
    public TableColumn<Item, String> remitunit;
    public TableColumn<Item, String> pattern;
    public TableColumn<Item, Long> remitdate;
    public TableColumn<Item, BigDecimal> paytotal;
    public TableColumn<Item, String> note;

    private static final int SIZE_PER_PAGE = 12;
    public Label resp_name;
    public Label resp_year;
    public Label resp_date;

    public TextField bf_total;
    public TextField an_total;
    public TextField rate_total;
    public TextField pay_total;
    public TextField all_total;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(27).divide(SIZE_PER_PAGE));

        table.setEditable(true);
        total.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        billunit.setCellFactory(TextFieldTableCell.forTableColumn());
        billdate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.timestampConverter()));
        billtotal.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.rateConverter()));
        ratetotal.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        remitunit.setCellFactory(TextFieldTableCell.forTableColumn());
        pattern.setCellFactory(TextFieldTableCell.forTableColumn());
        remitdate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.timestampConverter()));
        paytotal.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.decimalConverter("0")));
        note.setCellFactory(TextFieldTableCell.forTableColumn());

        // 添加表格转换器
        mon.setCellValueFactory(cellFeature ->
                new SimpleStringProperty("" + cellFeature.getValue().getMon() + "月"));
        billunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getBillunit()));
        billdate.setCellValueFactory(cellFeature ->
                new SimpleObjectProperty<>(cellFeature.getValue().getBilldate()));
        remitunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRemitunit()));
        pattern.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPattern()));
        remitdate.setCellValueFactory(cellFeature ->
                new SimpleObjectProperty<>(cellFeature.getValue().getRemitdate()));
        note.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getNote()));

        billtotal.setCellValueFactory(cellFeature -> cellFeature.getValue().billtotalPropert);
        rate.setCellValueFactory(cellFeature -> cellFeature.getValue().ratePropert);
        total.setCellValueFactory(cellFeature -> cellFeature.getValue().totalPropert);
        paytotal.setCellValueFactory(cellFeature -> cellFeature.getValue().paytotalPropert);
        ratetotal.setCellValueFactory(cellFeature -> cellFeature.getValue().ratetotalProperty);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(CustAnnualModelFull datas) {
        // 填充客户信息
        try {
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            addr.setText(c.getAddr());
            serial.setText(c.getSerial());
            phone.setText(c.getPhone());
            name.setText(c.getNamefull());
            fax.setText(c.getFax());
            pvacc.setText(c.getAccpv());
            pvbcc.setText(c.getBccpv());
            pbacc.setText(c.getAccpb());
            pbbcc.setText(c.getBccpb());

            date.setText(datas.getYear() + "年");
            // 底部
            resp_name.setText(c.getNamefull());
            resp_year.setText(datas.getYear() + "年");
            resp_date.setText(FXUtils.stampToDate(System.currentTimeMillis(), "yyyy年MM月"));

            bf_total.setText(FXUtils.decimalStr(datas.getRemainder()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.getItems().setAll(
                datas.getMons().stream().map(Item::new).collect(Collectors.toList()).subList(
                        0,
                        Math.min(SIZE_PER_PAGE, datas.getMons().size())
                )
        );
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public void autoCalculate() {
        table.getItems().forEach(x -> x.ratetotalProperty.setValue(x.getRateTotalProperty()));
        FXWidgetUtil.compute(table.getItems(),
                Item::getTotalPropert,
                an_total::setText);
        FXWidgetUtil.compute(table.getItems(),
                Item::getRatetotalProperty,
                rate_total::setText);
        FXWidgetUtil.compute(table.getItems(),
                Item::getPaytotalPropert,
                pay_total::setText);

        // 总应付金额
        all_total.setText(
                FXUtils.getDecimal(an_total.getText(), new BigDecimal(0))
                        .add(FXUtils.getDecimal(rate_total.getText(), new BigDecimal(0)))
                        .add(FXUtils.getDecimal(bf_total.getText(), new BigDecimal(0)))
                        .subtract(FXUtils.getDecimal(pay_total.getText(), new BigDecimal(0))).toString()
        );
    }

    @Override
    public IntegerBinding pageNum() {
        return new IntegerBinding() {
            {
                bind(table.getItems());
            }

            @Override
            protected int computeValue() {
                return table.getItems().isEmpty() ? 0 : 1;
            }
        };
    }

    @Override
    public void selectPage(int i) {
        // Nothing to do
    }

    private class Item extends CustAnnualMonModel {
        Property<BigDecimal> ratetotalProperty = new SimpleObjectProperty<>();
        Property<BigDecimal> billtotalPropert = new SimpleObjectProperty<>();
        Property<BigDecimal> ratePropert = new SimpleObjectProperty<>();
        Property<BigDecimal> totalPropert = new SimpleObjectProperty<>();
        Property<BigDecimal> paytotalPropert = new SimpleObjectProperty<>();

        Item(CustAnnualMonModel m) {
            super(m);
            setBilltotalPropert(m.getBilltotal());
            setRatePropert(m.getRate());
            setTotalPropert(m.getTotal());
            setPaytotalPropert(m.getPaytotal());
        }

        public BigDecimal getRatetotalProperty() {
            return ratetotalProperty.getValue();
        }

        public Property<BigDecimal> ratetotalPropertyProperty() {
            return ratetotalProperty;
        }

        public void setRatetotalProperty(BigDecimal ratetotalProperty) {
            this.ratetotalProperty.setValue(ratetotalProperty);
        }

        public BigDecimal getBilltotalPropert() {
            return billtotalPropert.getValue();
        }

        public Property<BigDecimal> billtotalPropertProperty() {
            return billtotalPropert;
        }

        public void setBilltotalPropert(BigDecimal billtotalPropert) {
            this.billtotalPropert.setValue(billtotalPropert);
        }

        public BigDecimal getRatePropert() {
            return ratePropert.getValue();
        }

        public Property<BigDecimal> ratePropertProperty() {
            return ratePropert;
        }

        public void setRatePropert(BigDecimal ratePropert) {
            this.ratePropert.setValue(ratePropert);
        }

        public BigDecimal getTotalPropert() {
            return totalPropert.getValue();
        }

        public Property<BigDecimal> totalPropertProperty() {
            return totalPropert;
        }

        public void setTotalPropert(BigDecimal totalPropert) {
            this.totalPropert.setValue(totalPropert);
        }

        public BigDecimal getPaytotalPropert() {
            return paytotalPropert.getValue();
        }

        public Property<BigDecimal> paytotalPropertProperty() {
            return paytotalPropert;
        }

        public void setPaytotalPropert(BigDecimal paytotalPropert) {
            this.paytotalPropert.setValue(paytotalPropert);
        }


        public BigDecimal getRateTotalProperty() {
            try {
                return getRatePropert().multiply(getBilltotalPropert());
            } catch (Exception e) {
                return new BigDecimal(0);
            }
        }
    }
}
