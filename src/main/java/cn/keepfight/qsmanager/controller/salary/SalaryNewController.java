package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.AddSalaryItem;
import cn.keepfight.widget.MonthPicker;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 发现自己并没有任何的艺术天赋、歌舞才艺、运动强项、爱好特长。活生生的咸鱼一条。
 * Created by tom on 2017/10/14.
 */
public class SalaryNewController implements ContentCtrl, Initializable {

    public VBox root;
    public VBox stuffList;
    public Label spaceholder;
    public Button yearMonth;

    public Button ok;
    public Button cancel;

    private Long year;
    private Long month;
    private List<AddSalaryItem> items;
    private MonthPicker p;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spaceholder
                .visibleProperty()
                .bind(new SimpleListProperty<>(stuffList.getChildren()).emptyProperty());
        ok.disableProperty().bind(spaceholder.visibleProperty());

        p = FXWidgetUtil.getMonthPicker();
        p.setOnClose(e -> QSApp.mainPane.reload(FXUtils.ps(
                new Pair<>("year", e.getKey()),
                new Pair<>("month", e.getValue()))));
        yearMonth.setOnMouseClicked(e -> p.show(yearMonth));

        ok.setOnAction(e -> {
            // @TODO tianjia
            items.stream()
                    .filter(i -> i.get().getSelect())
                    .map(i -> {
                        SalaryDao d = new SalaryDao();
                        d.setYear(year);
                        d.setMonth(month);
                        d.setStuffDao(i.get().getStuff());
                        d.setTotalSalary(i.get().getTotal());
                        d.setBasicSalary(i.get().getStuff().getSalary_basic());
                        d.setAgeSalary(i.get().getStuff().getSalary_annual());
                        return d;
                    }).forEach(d -> {
                try {
                    SalaryServices.addNewSalary(d);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            QSApp.mainPane.backNav();
        });
        cancel.setOnAction(e -> QSApp.mainPane.backNav());
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
        year = (long) params.getOrDefault("year", (long)now.getYear());
        month = (long) params.getOrDefault("month", (long)now.getMonthValue());

        stuffList.getChildren().clear();

        items = new ArrayList<>();
        p.set(new Pair<>(year, month));
        yearMonth.setText(year + "年" + month + "月");

        try {
            List<StuffDao> ss = SalaryServices.selectStuffCanAddByMonth(year, month);
            for (StuffDao s : ss) {
                AddSalaryItem item = FXWidgetUtil.getSalaryNewItem();
                items.add(item);
                stuffList.getChildren().add(item.setStuff(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("新增员工工资");
    }
}
