package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.salary.SalaryPayDao;
import cn.keepfight.qsmanager.dao.salary.SalaryTardyDaoWrapper;
import cn.keepfight.qsmanager.dao.salary.StuffTardyDao;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 工资结算
 * Created by tom on 2017/10/19.
 */
public class SalaryClearController implements Initializable, ContentCtrl {

    public VBox root;
    public HBox monlist;
    public TableView<SalaryTardyDaoWrapper> table_stuff;
    public TableColumn<SalaryTardyDaoWrapper, String> tab_f_name;
    public TableColumn<SalaryTardyDaoWrapper, BigDecimal> tab_total;
    public TableColumn<SalaryTardyDaoWrapper, BigDecimal> tab_clear;
    public TableColumn<SalaryTardyDaoWrapper, BigDecimal> tab_left;
    public Label lab_total;
    public Label lab_pay;
    public Label lab_left;
    public Button ok;
    public Button cancel;
    public DatePicker date_picker;

    private StringProperty selectStatue = new SimpleStringProperty("");

    private List<TableColumn> tabs = new ArrayList<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        date_picker.getEditor().setDisable(true);

        FXWidgetUtil.connect(tab_f_name, SalaryTardyDaoWrapper::nameProperty);
        FXWidgetUtil.connectDecimalColumn(tab_total, SalaryTardyDaoWrapper::sumProperty);

        tab_clear.setCellValueFactory(param -> new ObjectBinding<BigDecimal>() {
            {
                bind(selectStatue);
            }

            @Override
            protected BigDecimal computeValue() {
                return param.getValue().countByStatue(selectStatue.get());
            }
        });
        tab_left.setCellValueFactory(param -> new ObjectBinding<BigDecimal>() {
            {
                bind(selectStatue);
            }

            @Override
            protected BigDecimal computeValue() {
                return param.getValue().getSum().subtract(param.getValue().countByStatue(selectStatue.get()));
            }
        });

        FXWidgetUtil.cellMoney(tab_total, tab_clear, tab_left);
        FXWidgetUtil.calculate(table_stuff.getItems(), SalaryTardyDaoWrapper::getSum, lab_total::setText);
        selectStatue.addListener(observable -> {
            FXWidgetUtil.compute(table_stuff.getItems(),
                    m -> m.countByStatue(selectStatue.get())
                    , lab_pay::setText, BigDecimal::add);
            FXWidgetUtil.compute(table_stuff.getItems(),
                    m -> m.getSum().subtract(m.countByStatue(selectStatue.get()))
                    , lab_left::setText, BigDecimal::add);
        });
        table_stuff.getItems().addListener((ListChangeListener<SalaryTardyDaoWrapper>) c -> {
            FXWidgetUtil.compute(table_stuff.getItems(),
                    m -> m.countByStatue(selectStatue.get())
                    , lab_pay::setText, BigDecimal::add);
            FXWidgetUtil.compute(table_stuff.getItems(),
                    m -> m.getSum().subtract(m.countByStatue(selectStatue.get()))
                    , lab_left::setText, BigDecimal::add);
        });

        cancel.setOnAction(event -> QSApp.mainPane.backNav());
        ok.setOnAction(event -> {
            if (date_picker.getValue() == null) {
                WarningBuilder.build("发放日期填写错误！");
                return;
            }
            Date date = Date.valueOf(date_picker.getValue());
            String status = selectStatue.get() == null ? "" : selectStatue.get();

            List<SalaryPayDao> incomes = table_stuff.getItems().stream()
                    .map(SalaryTardyDaoWrapper::get)
                    .flatMap(dao -> dao.getDetails().stream()
                            .filter(d -> d.getSalary_id() != null && !d.getSum().equals(new BigDecimal(0)))
                            .filter(d -> status.contains(d.getYm()))
                            .map(d -> {
                                SalaryPayDao res = new SalaryPayDao();
                                res.setDate(date);
                                res.setIncome(d.getSum());
                                res.setSalary_id(d.getSalary_id());
                                System.out.println("id:"+d.getSalary_id());
                                return res;
                            }))
                    .collect(Collectors.toList());
            if (incomes.size()==0){
                WarningBuilder.build("无可更新的值");
                return;
            }
            try {
                SalaryServices.insertIntoSalaryIncome(incomes);
                QSApp.mainPane.backNav();
            } catch (Exception e) {
                WarningBuilder.build("插入失败，请检查网络连接是否成功或者数据填写是否正确");
                e.printStackTrace();
            }
        });
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

        date_picker.setValue(null);
        table_stuff.getColumns().removeAll(tabs);
        tabs.clear();
        monlist.getChildren().clear();
        selectStatue.setValue("");
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
                    ck.setSelected(true);
                    monlist.getChildren().add(ck);
                    ck.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        column.setVisible(newValue);
                        selectStatue.set(monlist.getChildren().stream()
                                .map(x -> ((CheckBox) x))
                                .filter(CheckBox::isSelected)
                                .map(CheckBox::getText)
                                .collect(Collectors.joining("~")));
                    });
                }
                // 触发一波
                selectStatue.set(selectStatue.getValue() + " ");
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
