package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.salary.YearStaticDao;
import cn.keepfight.qsmanager.dao.salary.YearStaticDaoWrapper;
import cn.keepfight.qsmanager.model.MaterialModel;
import cn.keepfight.qsmanager.service.SalaryService;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.TableShowGrid;
import cn.keepfight.widget.YearScrollPicker;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
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

    public TableView table_stuff;

    private YearScrollPicker yearScrollPicker;

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

        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getTotal, lab_total::setText);
        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getGiven, lab_given::setText);
        FXWidgetUtil.calculate(table_static.getItems(), YearStaticDaoWrapper::getTarby, lab_tarby::setText);
    }

    private void initUI() {

    }

    private void initAction() {
//        table_static.setRowFactory(tv -> {
//            TableRow<MaterialModel> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (!row.isEmpty())) {
//
//                }
//            });
//            return row;
//        });
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
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资表");
    }
}
