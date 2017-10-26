package cn.keepfight.qsmanager.controller.annual;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.annual.InvoiceDaoWrapper;
import cn.keepfight.qsmanager.dao.annual.RemitDaoWrapper;
import cn.keepfight.qsmanager.service.CustInvoiceServers;
import cn.keepfight.qsmanager.service.CustRemitServers;
import cn.keepfight.qsmanager.service.SupInvoiceServers;
import cn.keepfight.qsmanager.service.SupRemitServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SupAnnualEditController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_invoice_add;
    public Button btn_invoice_del;

    public TableView<InvoiceDaoWrapper> table_bill;
    @FXML
    private TableColumn<InvoiceDaoWrapper, String> billunit;
    @FXML
    private TableColumn<InvoiceDaoWrapper, String> billdate;
    @FXML
    private TableColumn<InvoiceDaoWrapper, BigDecimal> billtotal;
    @FXML
    private TableColumn<InvoiceDaoWrapper, BigDecimal> rate;
    @FXML
    private TableColumn<InvoiceDaoWrapper, BigDecimal> ratetotal;

    public Label sum_invoice;
    public Label sum_rate;

    public Button btn_remit_add;
    public Button btn_remit_del;

    public TableView<RemitDaoWrapper> table_remit;
    @FXML
    private TableColumn<RemitDaoWrapper, String> remitunit;
    @FXML
    private TableColumn<RemitDaoWrapper, String> pattern;
    @FXML
    private TableColumn<RemitDaoWrapper, String> remitdate;
    @FXML
    private TableColumn<RemitDaoWrapper, BigDecimal> paytotal;
    @FXML
    private TableColumn<RemitDaoWrapper, String> note;

    public Label sum_remit;
    public Button ok;

    private Long year;
    private Long month;
    private Long sid;
    private Boolean sup_view = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXWidgetUtil.connect(billunit, InvoiceDaoWrapper::unitProperty);
        FXWidgetUtil.connectObj(billdate, InvoiceDaoWrapper::dateProperty);
        FXWidgetUtil.connectDecimalColumn(billtotal, InvoiceDaoWrapper::totalProperty);
        FXWidgetUtil.connectDecimalColumn(rate, InvoiceDaoWrapper::rateProperty);
        FXWidgetUtil.connectDecimalColumn(ratetotal, InvoiceDaoWrapper::rateTotalProperty);

        FXWidgetUtil.connect(remitunit, RemitDaoWrapper::unitProperty);
        FXWidgetUtil.connect(pattern, RemitDaoWrapper::modeProperty);
        FXWidgetUtil.connectObj(remitdate, RemitDaoWrapper::dateProperty);
        FXWidgetUtil.connectDecimalColumn(paytotal, RemitDaoWrapper::totalProperty);
        FXWidgetUtil.connect(note, RemitDaoWrapper::noteProperty);

        FXWidgetUtil.cellMoney(billtotal, ratetotal);
        FXWidgetUtil.cellMoney(paytotal);
        FXWidgetUtil.cellRate(rate);

        FXWidgetUtil.calculate(table_bill.getItems(), InvoiceDaoWrapper::getTotal, sum_invoice::setText);
        FXWidgetUtil.calculate(table_bill.getItems(), InvoiceDaoWrapper::getRate, sum_rate::setText);
        FXWidgetUtil.calculate(table_remit.getItems(), RemitDaoWrapper::getTotal, sum_remit::setText);

        btn_invoice_add.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_INVOICE,
                FXUtils.ps(
                        new Pair<>("year", year),
                        new Pair<>("month", month),
                        new Pair<>("sup_id", sid),
                        new Pair<>("sup_view", sup_view)
                )
        ));
        btn_invoice_del.setOnAction(event -> {
            InvoiceDaoWrapper x = table_bill.getSelectionModel().getSelectedItem();
            try {
                if (sup_view) {
                    SupInvoiceServers.deleteInvoiceByID(x.getId());
                }else {
                    CustInvoiceServers.deleteInvoiceByID(x.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("删除失败，请检查网络是否可用");
            }
            QSApp.mainPane.refresh();
        });

        btn_remit_add.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.annual$SUP_ADD_REMIT,
                FXUtils.ps(
                        new Pair<>("year", year),
                        new Pair<>("month", month),
                        new Pair<>("sup_id", sid),
                        new Pair<>("sup_view", sup_view)
                )
        ));
        btn_remit_del.setOnAction(event -> {
            RemitDaoWrapper x = table_remit.getSelectionModel().getSelectedItem();
            try {
                if (sup_view) {
                    SupRemitServers.deleteRemitByID(x.getId());
                }else {
                    CustRemitServers.deleteRemitByID(x.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("删除失败，请检查网络是否可用");
            }
            QSApp.mainPane.refresh();
        });


        ok.setOnAction(event -> QSApp.mainPane.backNav());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public void showed(Properties params) {
        year = (Long) params.get("year");
        month = (Long) params.get("month");
        sid = (Long) params.get("sid");
        if (params.containsKey("sup_view")){
            sup_view = (boolean)params.get("sup_view");
        }
        try {
            if (sup_view) {
                table_bill.getItems().setAll(
                        SupInvoiceServers.selectInvoiceByMonthAndSup(sid, year, month)
                                .stream()
                                .map(InvoiceDaoWrapper::new)
                                .collect(Collectors.toList())
                );
            } else {
                table_bill.getItems().setAll(
                        CustInvoiceServers.selectInvoiceByMonthAndSup(sid, year, month)
                                .stream()
                                .map(InvoiceDaoWrapper::new)
                                .collect(Collectors.toList())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (sup_view) {
                table_remit.getItems().setAll(
                        SupRemitServers.selectRemitByMonthAndSup(sid, year, month)
                                .stream()
                                .map(RemitDaoWrapper::new)
                                .collect(Collectors.toList())
                );
            } else {
                table_remit.getItems().setAll(
                        CustRemitServers.selectRemitByMonthAndSup(sid, year, month)
                                .stream()
                                .map(RemitDaoWrapper::new)
                                .collect(Collectors.toList())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("对账表编辑");
    }
}
