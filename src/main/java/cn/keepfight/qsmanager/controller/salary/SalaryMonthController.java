package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.salary.SalaryDaoWrapper;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    public Button filldate;

    public Button btn_new;
    public Button btn_edit;
    public Button btn_del;

    public Label sum_total;
    public Label sum_given;
    public Label sum_will;

    private long data_year = 2017;
    private long data_month = 10;
    private MonthPicker p;

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

        FXWidgetUtil.connect(tab_name, e -> e.getStuffDaoWrapper().nameProperty());
        FXWidgetUtil.connect(tab_serial, e -> e.getStuffDaoWrapper().serialProperty());
        FXWidgetUtil.connectDecimalColumn(tab_salary_basic, SalaryDaoWrapper::basicSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_age, SalaryDaoWrapper::ageSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_other, SalaryDaoWrapper::otherSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_total, SalaryDaoWrapper::totalSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_salary_basic_given, SalaryDaoWrapper::fixSalaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_will_give, SalaryDaoWrapper::willSalaryProperty);
        FXWidgetUtil.connectObj(tab_given_date, SalaryDaoWrapper::dateProperty);

        FXWidgetUtil.cellMoney(tab_salary_basic, tab_salary_age, tab_salary_other,
                tab_salary_total, tab_salary_basic_given, tab_will_give);

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
        FXWidgetUtil.calculate(table_salary.getItems(), SalaryDaoWrapper::getFixSalary, sum_given::setText);
        FXWidgetUtil.calculate(table_salary.getItems(), x -> x.getTotalSalary().subtract(x.getFixSalary()), sum_will::setText);

        filldate.setOnAction(event -> {
            Dialog<Date> dialog = new Dialog<>();
            dialog.setTitle("发放日期填充");
            dialog.setHeaderText("请选择一个日期来统一填写全部发放日期");
            ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText("点击选择日期");
            datePicker.getEditor().setDisable(true);
            dialog.getDialogPane().lookupButton(okButtonType).disableProperty().bind(datePicker.valueProperty().isNull());
            dialog.getDialogPane().setContent(new BorderPane(datePicker));
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return Date.valueOf(datePicker.getValue());
                }
                return null;
            });

            Optional<Date> result = dialog.showAndWait();

            result.ifPresent(date -> {
                try {
                    SalaryServices.updateSalarysDateByMonth(data_year, data_month, date);
                    QSApp.mainPane.refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("更新失败，请检查网络链接是否可用");
                }
            });
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
        data_year = (long) params.getOrDefault("year", (long) now.getYear());
        data_month = (long) params.getOrDefault("month", (long) now.getMonthValue());

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
