package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.salary.SalaryTardyDaoWrapper;
import cn.keepfight.qsmanager.dao.salary.StuffTardyDao;
import cn.keepfight.qsmanager.dao.salary.YearStaticDaoWrapper;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.TableShowGrid;
import cn.keepfight.widget.YearScrollPicker;
import javafx.application.Platform;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 工资表模块控制器
 * Created by tom on 2017/7/19.
 */
public class SalaryController implements ContentCtrl, Initializable {
    @FXML
    private VBox root;
    @FXML
    private Label label_year;

    public TableView<YearStaticDaoWrapper> table_static;
    public TableColumn<YearStaticDaoWrapper, String> tab_s_month;
    public TableColumn<YearStaticDaoWrapper, BigDecimal> tab_s_total;
    public TableColumn<YearStaticDaoWrapper, BigDecimal> tab_s_given;
    public TableColumn<YearStaticDaoWrapper, BigDecimal> tab_s_tarby;
    public Label lab_total;
    public Label lab_given;
    public Label lab_tarby;
    public Label lab_stuff_tardy;


    public Button btn_clear;
    public TableView<SalaryTardyDaoWrapper> table_stuff;
    public TableColumn<SalaryTardyDaoWrapper, String> tab_f_name;
    public TableColumn<SalaryTardyDaoWrapper, BigDecimal> tab_f_total;

    private YearScrollPicker yearScrollPicker;

    private List<TableColumn> tabs = new ArrayList<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
        yearScrollPicker = FXWidgetUtil.getYearPicker();
        label_year.setOnMouseClicked(e -> yearScrollPicker.show(label_year));
        yearScrollPicker.setOnClose(year -> {
            Properties ps = new Properties();
            ps.put("year", year);
            QSApp.mainPane.reload(ps);
        });

        FXWidgetUtil.connect(tab_s_month, YearStaticDaoWrapper::monthProperty);
        FXWidgetUtil.connectDecimalColumn(tab_s_total, YearStaticDaoWrapper::totalProperty);
        FXWidgetUtil.connectDecimalColumn(tab_s_given, YearStaticDaoWrapper::givenProperty);
        FXWidgetUtil.connectDecimalColumn(tab_s_tarby, YearStaticDaoWrapper::tarbyProperty);
        FXWidgetUtil.cellMoney(tab_s_total, tab_s_given, tab_s_tarby);

        FXWidgetUtil.connect(tab_f_name, SalaryTardyDaoWrapper::nameProperty);
        FXWidgetUtil.connectDecimalColumn(tab_f_total, SalaryTardyDaoWrapper::sumProperty);
        FXWidgetUtil.cellMoney(tab_f_total);

        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getTotal, lab_total::setText);
        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getGiven, lab_given::setText);
        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getTarby, lab_tarby::setText);

        FXWidgetUtil.calculate(table_stuff.getItems(), SalaryTardyDaoWrapper::getSum, lab_stuff_tardy::setText);


        table_static.setRowFactory(tv -> {
            TableRow<YearStaticDaoWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    long month = Long.valueOf(row.getTableView().getItems().get(row.getIndex()).getMonth());
                    long year = yearScrollPicker.get();
                    QSApp.mainPane.changeTo(MainPaneList.SALARY_MONTH, FXUtils.ps(
                            new Pair<>("year", year),
                            new Pair<>("month", month)
                    ));
                }
            });
            return row;
        });

        btn_clear.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.SALARY_CLEAR));
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
        long year = (long) params.getOrDefault("year", (long) FXUtils.stampToLocalDate().getYear());
        label_year.setText("" + year);
        yearScrollPicker.set(year);

        try {
            table_static.getItems().setAll(
                    SalaryServices.staticSalaryByYear(year).stream()
                            .map(YearStaticDaoWrapper::new)
                            .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        table_stuff.getColumns().removeAll(tabs);
        tabs.clear();
        new Thread(()->{
            try {
                List<SalaryTardyDaoWrapper> ss = SalaryServices.selectStuffSalaryTardy().stream()
                        .map(SalaryTardyDaoWrapper::new)
                        .collect(Collectors.toList());
                table_stuff.getItems().setAll(ss);

                if (ss.size() > 0) {
                    for (StuffTardyDao d : ss.get(0).get().getDetails()) {
                        TableColumn<SalaryTardyDaoWrapper, BigDecimal> column = new TableColumn<>(d.getYm());
                        Platform.runLater(()->table_stuff.getColumns().add(column));
                        tabs.add(column);
                        FXWidgetUtil.cellMoney(column);
                        FXWidgetUtil.connectDecimalColumn(column, k -> new SimpleObjectProperty<>(k.get().getDetailByYM(d.getYm())));
                    }
                }
//                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资表");
    }
}
