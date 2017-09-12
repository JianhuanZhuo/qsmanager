package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.SupAnnualModelFull;
import cn.keepfight.qsmanager.model.SupAnnualMonModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 支出年度对账表
 * Created by tom on 2017/9/4.
 */
public class OutcomeAnnualController implements ContentCtrl, Initializable {
    public VBox root;

    // 年度对账表部分
    @FXML
    private ChoiceBox<SupplyModel> an_sup_sel;
    @FXML
    private ChoiceBox<Long> an_year_sel;
    @FXML
    private Button an_print;
    @FXML
    private TableView<SupAnnualMonModel> anTable;

    @FXML
    private TableColumn<SupAnnualMonModel, String> mon;
    @FXML
    private TableColumn<SupAnnualMonModel, String> total;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billunit;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billdate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> billtotal;
    @FXML
    private TableColumn<SupAnnualMonModel, BigDecimal> rate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> ratetotal;
    @FXML
    private TableColumn<SupAnnualMonModel, String> remitunit;
    @FXML
    private TableColumn<SupAnnualMonModel, String> pattern;
    @FXML
    private TableColumn<SupAnnualMonModel, String> remitdate;
    @FXML
    private TableColumn<SupAnnualMonModel, String> paytotal;
    @FXML
    private TableColumn<SupAnnualMonModel, String> note;

    @FXML
    private Label an_count_bf;
    @FXML
    private Label an_total_annu;
    @FXML
    private Label an_total_rate;
    @FXML
    private Label an_total_pay;
    @FXML
    private Label an_total_actu;
    @FXML
    private Label an_attach;

    // 子界面
    private SupAnnualAddController supAnnualAddController;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        an_sup_sel.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName(), "全部供应商"));
        an_year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));

        an_print.disableProperty().bind(an_sup_sel.getSelectionModel().selectedItemProperty().isNotNull()
                .and(an_year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .not()
        );



        // 添加表格转换器
        FXWidgetUtil.connectDecimal(total, SupAnnualMonModel::getTotal);
        FXWidgetUtil.connectDecimal(billtotal, SupAnnualMonModel::getBilltotal);
        FXWidgetUtil.connectDecimal(paytotal, SupAnnualMonModel::getPaytotal);
        FXWidgetUtil.connectDecimal(ratetotal, SupAnnualMonModel::getBilltotal);
        mon.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getMon().toString() + "月"));
        billunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getBillunit()));
        billdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getBilldate())));
        remitunit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRemitunit()));
        pattern.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPattern()));
        remitdate.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.stampToDate(cellFeature.getValue().getRemitdate())));
        note.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getNote()));

        rate.setCellValueFactory(cellFeature ->
                new SimpleObjectProperty<>(cellFeature.getValue().getRate()));
        rate.setCellFactory(TextFieldTableCell.forTableColumn(FXUtils.rateConverter()));



        //计算
        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualMonModel::getTotal, an_total_annu::setText);
        FXWidgetUtil.calculate(anTable.getItems(),
                (model -> model.getRate().multiply(model.getBilltotal())),
                an_total_rate::setText);
        FXWidgetUtil.calculate(anTable.getItems(), SupAnnualMonModel::getPaytotal, an_total_pay::setText);

        // 计算实际应付合计
        an_total_actu.textProperty().bind(new ObjectBinding<String>() {
            {
                this.bind(an_count_bf.textProperty(),
                        an_total_annu.textProperty(),
                        an_total_rate.textProperty(),
                        an_total_pay.textProperty());
            }

            @Override
            protected String computeValue() {
                BigDecimal bf = FXUtils.getDecimal(an_count_bf.getText(), new BigDecimal(0L));
                BigDecimal annu = FXUtils.getDecimal(an_total_annu.getText(), new BigDecimal(0L));
                BigDecimal rate = FXUtils.getDecimal(an_total_rate.getText(), new BigDecimal(0L));
                BigDecimal pay = FXUtils.getDecimal(an_total_pay.getText(), new BigDecimal(0L));
                return annu.add(rate).subtract(bf).subtract(pay).toString();
            }
        });


        an_sup_sel.setOnAction(event -> loadAnnu());
        an_year_sel.setOnAction(event -> loadAnnu());

        // 双击原料表进行编辑
        anTable.setRowFactory(tv -> {
            TableRow<SupAnnualMonModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // 防止空双击
                if (an_year_sel.getSelectionModel().isEmpty() || an_sup_sel.getSelectionModel().isEmpty()) {
                    return;
                }
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SupAnnualMonModel rowData = row.getItem();
                    Optional<SupAnnualMonModel> op = CustomDialog.gen().build(supAnnualAddController, rowData);
                    op.ifPresent(model -> {
                        rowData.setTotal(model.getTotal());
                        rowData.setBillunit(model.getBillunit());
                        rowData.setBilldate(model.getBilldate());
                        rowData.setBilltotal(model.getBilltotal());
                        rowData.setRate(model.getRate());
                        rowData.setRemitunit(model.getRemitunit());
                        rowData.setPattern(model.getPattern());
                        rowData.setRemitdate(model.getRemitdate());
                        rowData.setPaytotal(model.getPaytotal());
                        rowData.setNote(model.getNote());
                        try {
                            QSApp.service.getSupAnnualService().updateMon(rowData);
                            loadAnnu();
                        } catch (Exception e) {
                            //@TODO
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
            return row;
        });

        // 打印支持
        an_print.setOnAction(event -> {
            SupplyModel sup = an_sup_sel.getSelectionModel().getSelectedItem();
            Long year = an_year_sel.getSelectionModel().getSelectedItem();

            PrintSource source = new PrintSource();
            Long sid = sup == null ? null : sup.getId();
            source.setSup(sid);
            source.setYear(year);
            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.YEAR_SUP, source));
        });
    }

    @Override
    public void loaded() {

        loadSelection();
        Platform.runLater(() -> {
            try {
                supAnnualAddController = ViewPathUtil.loadViewForController("sup_annual_add.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("年度对账表");
    }



    /**
     * 加载供应商和年份下拉选择的选项
     */
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<Long> years = QSApp.service.getReceiptService().selectYear();
                an_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
                an_year_sel.getItems().setAll(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    /**
     * 所谓事件处理器，加载对账表
     */
    private void loadAnnu() {
        System.out.println("load annu");
        if (an_sup_sel.getSelectionModel().isEmpty() ||
                an_year_sel.getSelectionModel().isEmpty()) {
            return;
        }
        System.out.println("get sid and year");
        Long sid = an_sup_sel.getSelectionModel().getSelectedItem().getId();
        Long year = an_year_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                System.out.println("in runlater!");
                SupAnnualModelFull full = QSApp.service.getSupAnnualService().selectAnnual(sid, year);
                System.out.println("full mon size: " + full.getMons().size());
                anTable.getItems().setAll(
                        FXCollections.observableList(full.getMons()));
                //@TODO 添加对结余的支持和附件
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
