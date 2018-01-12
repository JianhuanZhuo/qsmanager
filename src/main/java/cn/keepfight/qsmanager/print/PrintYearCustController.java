package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.dao.annual.AnnualDaoWrapper;
import cn.keepfight.qsmanager.dao.annual.InvoiceDao;
import cn.keepfight.qsmanager.dao.annual.RemitDao;
import cn.keepfight.qsmanager.model.CustAnnualModelFull;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.service.CustAnnualServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.List;
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
    @FXML
    private TableView<AnnualDaoWrapper> anTable;
    @FXML
    private TableColumn<AnnualDaoWrapper, String> mon;
    @FXML
    private TableColumn<AnnualDaoWrapper, BigDecimal> total;

    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> billunit;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<Date>> billdate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> billtotal;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> rate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> ratetotal;

    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> remitunit;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> pattern;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<Date>> remitdate;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<BigDecimal>> paytotal;
    @FXML
    private TableColumn<AnnualDaoWrapper, List<String>> note;


    private static final int SIZE_PER_PAGE = 12;
    public Label resp_name;
    public Label resp_year;
    public Label resp_date;

    public TextField bf_total;
    public TextField an_total;
    public TextField rate_total;
    public TextField pay_total;
    public TextField all_total;

    private CustAnnualModelFull datas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        anTable.fixedCellSizeProperty().bind(anTable.heightProperty().subtract(27).divide(SIZE_PER_PAGE));

        anTable.setEditable(true);

        // 添加表格转换器
        FXWidgetUtil.connectDecimalColumn(total, AnnualDaoWrapper::tradeTotalProperty);
        FXWidgetUtil.cellMoney(total);
        FXWidgetUtil.connect(mon, x -> x.monthProperty().asString().concat("月"));

        billunit.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getUnit).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billunit, x -> x);

        billdate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getDate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billdate, x -> FXUtils.stampToDate(x.getTime()));

        billtotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(billtotal, FXUtils::deciToMoney);

        rate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getRate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(rate, FXUtils::decimalRateStr);

        ratetotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getInvoice().stream().map(
                        InvoiceDao::getRateTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(ratetotal, FXUtils::deciToMoney);

        /////////////////////////////////////////////////////////////////////////////
        // 汇款
        remitunit.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getUnit).collect(Collectors.toList())));
        FXWidgetUtil.cellList(remitunit, x -> x);

        pattern.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getMode).collect(Collectors.toList())));
        FXWidgetUtil.cellList(pattern, x -> x);

        note.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getNote).collect(Collectors.toList())));
        FXWidgetUtil.cellList(note, x -> x);

        remitdate.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getDate).collect(Collectors.toList())));
        FXWidgetUtil.cellList(remitdate, x -> FXUtils.stampToDate(x.getTime()));

        paytotal.setCellValueFactory(param -> new SimpleObjectProperty<>(
                param.getValue().getRemit().stream().map(
                        RemitDao::getTotal).collect(Collectors.toList())));
        FXWidgetUtil.cellList(paytotal, FXUtils::deciToMoney);


        //计算
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getTradeTotal, an_total::setText);
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getInvoicesRateTotal, rate_total::setText);
        FXWidgetUtil.calculate(anTable.getItems(), AnnualDaoWrapper::getRemitTotal, pay_total::setText);

        // 计算实际应付合计
        all_total.textProperty().bind(new ObjectBinding<String>() {
            {
                this.bind(bf_total.textProperty(),
                        an_total.textProperty(),
                        rate_total.textProperty(),
                        pay_total.textProperty());
            }

            @Override
            protected String computeValue() {
                BigDecimal bf = FXUtils.getDecimal(bf_total.getText(), new BigDecimal(0L));
                BigDecimal annu = FXUtils.getDecimal(an_total.getText(), new BigDecimal(0L));
                BigDecimal rate = FXUtils.getDecimal(rate_total.getText(), new BigDecimal(0L));
                BigDecimal pay = FXUtils.getDecimal(pay_total.getText(), new BigDecimal(0L));
                return FXUtils.deciToMoney(annu.add(rate).add(bf).subtract(pay));
            }
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(CustAnnualModelFull datas) {
        this.datas = datas;
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

            FXWidgetUtil.addDefaultList(
                    new Pair<>("custom.info.addr."+c.getSerial(), addr.getText())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bf_total.setText(FXUtils.deciToMoney(CustAnnualServers.staticAnnualLeft(datas.getCid(), datas.getYear())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            anTable.getItems().setAll(
                    CustAnnualServers.staticAnnualMonByMonAndSup(datas.getCid(), datas.getYear())
                            .stream().map(AnnualDaoWrapper::new)
                            .collect(Collectors.toList()));
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

//        // 总应付金额
//        all_total.setText(
//                FXUtils.getDecimal(an_total.getText(), new BigDecimal(0))
//                        .add(FXUtils.getDecimal(rate_total.getText(), new BigDecimal(0)))
//                        .add(FXUtils.getDecimal(bf_total.getText(), new BigDecimal(0)))
//                        .subtract(FXUtils.getDecimal(pay_total.getText(), new BigDecimal(0))).toString()
//        );
    }

    @Override
    public void printBefore() {
        // 保存信息
        try {
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            c.setNamefull(name.getText());
            c.setAddr(addr.getText());
            c.setPhone(phone.getText());
            c.setFax(fax.getText());
            c.setAccpv(pvacc.getText());
            c.setBccpv(pvbcc.getText());
            c.setAccpb(pbacc.getText());
            c.setBccpb(pbbcc.getText());
            QSApp.service.getCustomService().update(c);

            FXWidgetUtil.addDefaultList(
                    new Pair<>("custom.info.addr."+c.getSerial(), addr.getText())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    private class Item extends CustAnnualMonModel {
//        Property<BigDecimal> ratetotalProperty = new SimpleObjectProperty<>();
//        Property<BigDecimal> billtotalPropert = new SimpleObjectProperty<>();
//        Property<BigDecimal> ratePropert = new SimpleObjectProperty<>();
//        Property<BigDecimal> totalPropert = new SimpleObjectProperty<>();
//        Property<BigDecimal> paytotalPropert = new SimpleObjectProperty<>();
//
//        Item(CustAnnualMonModel m) {
//            super(m);
//            setBilltotalPropert(m.getBilltotal());
//            setRatePropert(m.getRate());
//            setTotalPropert(m.getTotal());
//            setPaytotalPropert(m.getPaytotal());
//        }
//
//        public BigDecimal getRatetotalProperty() {
//            return ratetotalProperty.getValue();
//        }
//
//        public Property<BigDecimal> ratetotalPropertyProperty() {
//            return ratetotalProperty;
//        }
//
//        public void setRatetotalProperty(BigDecimal ratetotalProperty) {
//            this.ratetotalProperty.setValue(ratetotalProperty);
//        }
//
//        public BigDecimal getBilltotalPropert() {
//            return billtotalPropert.getValue();
//        }
//
//        public Property<BigDecimal> billtotalPropertProperty() {
//            return billtotalPropert;
//        }
//
//        public void setBilltotalPropert(BigDecimal billtotalPropert) {
//            this.billtotalPropert.setValue(billtotalPropert);
//        }
//
//        public BigDecimal getRatePropert() {
//            return ratePropert.getValue();
//        }
//
//        public Property<BigDecimal> ratePropertProperty() {
//            return ratePropert;
//        }
//
//        public void setRatePropert(BigDecimal ratePropert) {
//            this.ratePropert.setValue(ratePropert);
//        }
//
//        public BigDecimal getTotalPropert() {
//            return totalPropert.getValue();
//        }
//
//        public Property<BigDecimal> totalPropertProperty() {
//            return totalPropert;
//        }
//
//        public void setTotalPropert(BigDecimal totalPropert) {
//            this.totalPropert.setValue(totalPropert);
//        }
//
//        public BigDecimal getPaytotalPropert() {
//            return paytotalPropert.getValue();
//        }
//
//        public Property<BigDecimal> paytotalPropertProperty() {
//            return paytotalPropert;
//        }
//
//        public void setPaytotalPropert(BigDecimal paytotalPropert) {
//            this.paytotalPropert.setValue(paytotalPropert);
//        }
//
//
//        public BigDecimal getRateTotalProperty() {
//            try {
//                return getRatePropert().multiply(getBilltotalPropert());
//            } catch (Exception e) {
//                return new BigDecimal(0);
//            }
//        }
//    }
}
