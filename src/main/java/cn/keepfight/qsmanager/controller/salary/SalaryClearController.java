package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.salary.SalaryTardyDaoWrapper;
import cn.keepfight.qsmanager.dao.salary.StuffTardyDao;
import cn.keepfight.qsmanager.dao.salary.YearStaticDaoWrapper;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 工资结算
 * Created by tom on 2017/10/19.
 */
public class SalaryClearController implements Initializable, ContentCtrl{

    public VBox root;
    public HBox monlist;
    public TableView<SalaryTardyDaoWrapper>  table_stuff;
    public TableColumn<SalaryTardyDaoWrapper, String> tab_f_name;
    public TableColumn<SalaryTardyDaoWrapper, BigDecimal> tab_total;
    public TableColumn tab_clear;
    public TableColumn tab_left;
    public Label lab_total;
    public Label lab_pay;
    public Label lab_left;
    public Button ok;
    public Button cancel;

    private List<TableColumn> tabs = new ArrayList<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXWidgetUtil.connect(tab_f_name, SalaryTardyDaoWrapper::nameProperty);
        FXWidgetUtil.connectDecimalColumn(tab_total, SalaryTardyDaoWrapper::sumProperty);
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

        table_stuff.getColumns().removeAll(tabs);
        tabs.clear();
        try {
            List<SalaryTardyDaoWrapper> kk = SalaryServices.selectStuffSalaryTardy().stream()
                    .map(SalaryTardyDaoWrapper::new)
                    .collect(Collectors.toList());
            table_stuff.getItems().setAll(kk);

            if (kk.size() > 0) {
                for (StuffTardyDao d : kk.get(0).get().getDetails()) {
                    TableColumn<SalaryTardyDaoWrapper, BigDecimal> column = new TableColumn<>(d.getYm());
                    table_stuff.getColumns().add(column);
                    tabs.add(column);
                    FXWidgetUtil.cellMoney(column);
                    FXWidgetUtil.connectDecimalColumn(column, k -> new SimpleObjectProperty<>(k.get().getDetailByYM(d.getYm())));

                    // 关联 checkbox
                    CheckBox ck = new CheckBox(d.getYm());
                    column.visibleProperty().bind(ck.selectedProperty());
                    monlist.getChildren().add(ck);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资结算");
    }
}
