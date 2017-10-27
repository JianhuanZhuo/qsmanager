package cn.keepfight.qsmanager.controller.tax;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.tax.TaxInvoiceDaoWrapper;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class TaxController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_ym;
    public TextField tf_p1;
    public TextField tf_p2;
    public TextField tf_p3;
    public TextField tf_p4;
    public TextField tf_p5;
    public TextField tf_p6;

    public TextField tf_a;
    public TextField tf_b;
    public TextField tf_c;
    public TextField tf_d;
    public TextField tf_e;
    public TextField tf_f;
    public TextField tf_g;
    public TextField tf_h;
    public TextField tf_k;
    public TextField tf_j;
    public TableView<TaxInvoiceDaoWrapper> table_out;
    public TableColumn<TaxInvoiceDaoWrapper, Number> tab_out_serial;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_out_unit;
    public TableColumn<TaxInvoiceDaoWrapper, BigDecimal> tab_out_total;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_out_note;
    public TableView<TaxInvoiceDaoWrapper> table_in;
    public TableColumn<TaxInvoiceDaoWrapper, Number> tab_in_serial;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_in_unit;
    public TableColumn<TaxInvoiceDaoWrapper, BigDecimal> tab_in_total;
    public TableColumn<TaxInvoiceDaoWrapper, String> tab_in_note;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("发票抵扣计算");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tab_out_serial.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));
        FXWidgetUtil.connect(tab_out_unit, TaxInvoiceDaoWrapper::unitProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_total, TaxInvoiceDaoWrapper::totalProperty);
        FXWidgetUtil.connect(tab_out_note, TaxInvoiceDaoWrapper::noteProperty);

        tab_in_serial.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));
        FXWidgetUtil.connect(tab_in_unit, TaxInvoiceDaoWrapper::unitProperty);
        FXWidgetUtil.connectDecimalColumn(tab_in_total, TaxInvoiceDaoWrapper::totalProperty);
        FXWidgetUtil.connect(tab_in_note, TaxInvoiceDaoWrapper::noteProperty);
    }
}
