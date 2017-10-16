package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.salary.SalaryDaoWrapper;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.MonthPicker;
import cn.keepfight.widget.TableShowGrid;
import javafx.beans.binding.Binding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 工资表模块控制器
 * Created by tom on 2017/7/19.
 */
public class SalaryMonthController implements ContentCtrl, Initializable {
    @FXML
    private Button label_month;
    @FXML
    private VBox root;
    public TableView<SalaryDaoWrapper> table_salary;
    public TableColumn<SalaryDaoWrapper, String> tab_name;
    public TableColumn<SalaryDaoWrapper, String> tab_serial;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_salary_basic;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_salary_age;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_salary_other;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_salary_total;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_salary_basic_given;
    public TableColumn<SalaryDaoWrapper, String> tab_given_date;
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_will_give;

    public Button btn_new;

    private long data_year = 2017;
    private long data_month = 10;
    private MonthPicker p;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
        p = FXWidgetUtil.getMonthPicker();
        p.setOnClose(e ->QSApp.mainPane.reload(FXUtils.ps(
                                new Pair<>("year", e.getKey()),
                                new Pair<>("month", e.getValue()))));
        label_month.setOnMouseClicked(e -> p.show(label_month));
        setLabel_month(data_year, data_month);

        table_salary.setPlaceholder(new Label("无可显示数据，点击橙色年月可切换至其他年月"));

        btn_new.setOnAction(e-> QSApp.mainPane.changeTo(MainPaneList.SALARY_NEW, FXUtils.ps(
                new Pair<>("year", data_year),
                new Pair<>("month", data_month)
        )));

        FXWidgetUtil.connect(tab_name, e->e.getStuffDaoWrapper().nameProperty());
        FXWidgetUtil.connect(tab_serial, e->e.getStuffDaoWrapper().serialProperty());
        FXWidgetUtil.connectDecimalColumn(tab_salary_basic, SalaryDaoWrapper::basicSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_age, SalaryDaoWrapper::ageSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_other, SalaryDaoWrapper::otherSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_total, SalaryDaoWrapper::totalSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_basic_given, SalaryDaoWrapper::basicSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_will_give, SalaryDaoWrapper::willSalaryProperty);
        FXWidgetUtil.connectObj(tab_given_date, SalaryDaoWrapper::dateProperty);

        FXWidgetUtil.cellMoney(tab_salary_basic, tab_salary_age, tab_salary_other,
                tab_salary_total, tab_salary_basic_given, tab_will_give);


        table_salary.setRowFactory(tv -> {
            TableRow<SalaryDaoWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {

                }
            });
            return row;
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
        // 当前时间
        LocalDate now = FXUtils.stampToLocalDate(System.currentTimeMillis());
        data_year = (long) params.getOrDefault("year", (long)now.getYear());
        data_month = (long) params.getOrDefault("month", (long)now.getMonthValue());

        p.set(new Pair<>(data_year, data_month));
        setLabel_month(data_year, data_month);

        // 如果不存在则提示说要不要跳转到新增
        try {
            List<SalaryDaoWrapper> ss = SalaryServices.selectSalarysByMonth(data_year, data_month)
                    .stream()
                    .map(SalaryDaoWrapper::new)
                    .collect(Collectors.toList());
            table_salary.getItems().setAll(ss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资表");
    }

    private void setLabel_month(long year, long month) {
        data_year = year;
        data_month = month;
        label_month.setText(year + "年" + month + "月");
    }

    private void setLabel_month(Pair<Long, Long> monthPair) {
        setLabel_month(monthPair.getKey(), monthPair.getValue());
    }
}
