package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDaoWrapper;
import cn.keepfight.qsmanager.dao.salary.StuffTardyDao;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.MonthPicker;
import cn.keepfight.widget.TableShowGrid;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
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
    public TableColumn<SalaryDaoWrapper, BigDecimal> tab_will_give;

    public Button btn_new;
    public Button btn_edit;
    public Button btn_del;
    public Button btn_pay;
    public Button btn_pay_del;

    public Label sum_total;
    public Label sum_given;
    public Label sum_tardy;

    private long data_year = 2017;
    private long data_month = 10;
    private MonthPicker p;

    private List<TableColumn> tabs = new ArrayList<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
        p = FXWidgetUtil.getMonthPicker();
        p.setOnClose(e -> QSApp.mainPane.reload(FXUtils.ps(
                new Pair<>("year", e.getKey()),
                new Pair<>("month", e.getValue()))));
        label_month.setOnMouseClicked(e -> p.show(label_month));
        setLabel_month(data_year, data_month);

        table_salary.setPlaceholder(new Label("无可显示数据，点击橙色年月可切换至其他年月"));

        btn_new.setOnAction(e -> QSApp.mainPane.changeTo(MainPaneList.SALARY_NEW, FXUtils.ps(
                new Pair<>("year", data_year),
                new Pair<>("month", data_month)
        )));

        btn_edit.disableProperty().bind(table_salary.getSelectionModel().selectedItemProperty().isNull());
        btn_edit.setOnAction(e -> QSApp.mainPane.changeTo(MainPaneList.SALARY_EDIT, FXUtils.ps(
                new Pair<>("year", data_year),
                new Pair<>("month", data_month),
                new Pair<>("stuff", table_salary.getSelectionModel().getSelectedItem().getStuffDaoWrapper().getId())
        )));
        btn_del.disableProperty().bind(table_salary.getSelectionModel().selectedItemProperty().isNull());
        btn_del.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("警告确认");
            alert.setHeaderText("删除确认");
            alert.setContentText("该员工的该月数据删除后将无法恢复！");
            Optional<ButtonType> ops = alert.showAndWait();
            if (ops.isPresent()) {
                if (ops.get() == ButtonType.OK) {
                    try {
                        SalaryServices.deleteByMonthAndStuff(table_salary.getSelectionModel().getSelectedItem().get());
                        // 刷新数据
                        QSApp.mainPane.refresh();
                    } catch (Exception exp) {
                        exp.printStackTrace();
                        WarningBuilder.build("删除错误", "删除错误，请检查网络连接或刷新页面重新删除");
                    }
                }
            }
        });
        btn_pay_del.setOnAction(event -> {
            ChoiceDialog<String> dialog = new ChoiceDialog<String>();
            dialog.getItems().setAll(table_salary.getItems().get(0).get().getDetails()
                    .stream()
                    .map(StuffTardyDao::getYm)
                    .collect(Collectors.toList()));
            dialog.setHeaderText("选择工资发放记录日期，删除该记录日期下全部资金发放记录");
            dialog.setContentText("选择需要删除的工资发放记录日期");
            Optional<String> opts = dialog.showAndWait();
            opts.ifPresent(s->{
                System.out.println(s);
                try {
                    SalaryServices.deleteMonthSalaryIncomeByDate(data_year,data_month, s);
                    QSApp.mainPane.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("删除错误", "删除错误，请检查网络连接或刷新页面重新删除");
                }
            });
        });

        FXWidgetUtil.connect(tab_name, e -> e.getStuffDaoWrapper().nameProperty());
        FXWidgetUtil.connect(tab_serial, e -> e.getStuffDaoWrapper().serialProperty());
        FXWidgetUtil.connectDecimalColumn(tab_salary_basic, SalaryDaoWrapper::basicSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_age, SalaryDaoWrapper::ageSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_other, SalaryDaoWrapper::otherSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_total, SalaryDaoWrapper::totalSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_will_give, SalaryDaoWrapper::tardySalaryProperty);

        FXWidgetUtil.cellMoney(tab_salary_basic, tab_salary_age, tab_salary_other,
                tab_salary_total, tab_will_give);

        table_salary.setRowFactory(tv -> {
            TableRow<SalaryDaoWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    QSApp.mainPane.changeTo(MainPaneList.SALARY_EDIT, FXUtils.ps(
                            new Pair<>("year", data_year),
                            new Pair<>("month", data_month),
                            new Pair<>("stuff", table_salary.getSelectionModel().getSelectedItem().getStuffDaoWrapper().getId())
                    ));
                }
            });
            return row;
        });

        FXWidgetUtil.calculate(table_salary.getItems(), SalaryDaoWrapper::getTotalSalary, sum_total::setText);
        FXWidgetUtil.calculate(table_salary.getItems(), SalaryDaoWrapper::getTardySalary, sum_tardy::setText);
        FXWidgetUtil.calculate(table_salary.getItems(), x -> x.getTotalSalary().subtract(x.getTardySalary()), sum_given::setText);

        btn_pay.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.SALARY_PAY, FXUtils.ps(
                new Pair<>("year", data_year),
                new Pair<>("month", data_month)
        )));
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
        data_year = (long) params.getOrDefault("year", (long) now.getYear());
        data_month = (long) params.getOrDefault("month", (long) now.getMonthValue());

        p.set(new Pair<>(data_year, data_month));
        setLabel_month(data_year, data_month);
        table_salary.getColumns().removeAll(tabs);
        tabs.clear();

        // 如果不存在则提示说要不要跳转到新增
        try {
            List<SalaryDaoWrapper> ss = SalaryServices.selectSalarysByMonth(data_year, data_month)
                    .stream()
                    .map(SalaryDaoWrapper::new)
                    .collect(Collectors.toList());
            table_salary.getItems().setAll(ss);
            if (ss.size() > 0) {
                for (StuffTardyDao d : ss.get(0).get().getDetails()) {
                    TableColumn<SalaryDaoWrapper, BigDecimal> column = new TableColumn<>(d.getYm());
                    table_salary.getColumns().add(column);
                    tabs.add(column);
                    FXWidgetUtil.cellMoney(column);
                    FXWidgetUtil.connectDecimalColumn(column, k -> new SimpleObjectProperty<>(k.get().getDetailByYM(d.getYm())));
                }
            }
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
}
