package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.dao.salary.SalaryPayDao;
import cn.keepfight.qsmanager.dao.salary.SalaryTardyDao;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.AddSalaryItem;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 工资发放界面
 * Created by tom on 2017/10/19.
 */
public class SalaryPayController implements ContentCtrl, Initializable {

    public VBox root;
    public Label yearMonth;
    public DatePicker date_picker;
    public VBox stuffList;

    public TextField tf_fix;
    public Button btn_fill_fix;
    public Button btn_fill_left;

    public Button ok;
    public Button cancel;

    private Long year;
    private Long month;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ok.disableProperty().bind(new SimpleListProperty<>(stuffList.getChildren()).emptyProperty()
                .or(date_picker.valueProperty().isNull()));
        date_picker.getEditor().setDisable(true);
        date_picker.setValue(FXUtils.stampToLocalDate());

        btn_fill_fix.setOnAction(event -> {
            BigDecimal f;
            try {
                f = new BigDecimal(tf_fix.getText().replaceAll(",", "").trim());
            } catch (Exception e) {
                WarningBuilder.build("填充数字填写错误！");
                return;
            }
            stuffList.getChildren()
                    .forEach(x -> {
                        TextField textField = (TextField) ((HBox) x).getChildren().get(1);
                        textField.setText(FXUtils.deciToMoney(f));
                    });
        });
        btn_fill_left.setOnAction(event -> stuffList.getChildren()
                .forEach(x -> {
                    SalaryDao s = (SalaryDao) x.getUserData();
                    TextField textField = (TextField) ((HBox) x).getChildren().get(1);
                    textField.setText(FXUtils.deciToMoney(s.getTardySalary()));
                }));
        cancel.setOnAction(event -> QSApp.mainPane.backNav());
        ok.setOnAction(event -> {
            if (date_picker.getValue() == null) {
                WarningBuilder.build("发放日期填写错误！");
            }
            Date date = Date.valueOf(date_picker.getValue());
            List<SalaryPayDao> res = stuffList.getChildren().stream()
                    .map(x -> (HBox) x)
                    .filter(x -> ((CheckBox) (x.getChildren().get(0))).isSelected())
                    .map(x -> {
                        SalaryPayDao dao = new SalaryPayDao();
                        dao.setIncome(new BigDecimal(((TextField) (x.getChildren().get(1))).getText().replaceAll(",", "")));
                        dao.setDate(date);
                        SalaryDao s = (SalaryDao) x.getUserData();
                        dao.setSalary_id(s.getId());
                        return dao;
                    }).collect(Collectors.toList());
            try {
                SalaryServices.insertIntoSalaryIncome(res);
                QSApp.mainPane.backNav();
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("插入失败，请检查网络连接是否成功或者数据填写是否正确");
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

        // 当前时间
        LocalDate now = FXUtils.stampToLocalDate(System.currentTimeMillis());
        year = (long) params.getOrDefault("year", (long) now.getYear());
        month = (long) params.getOrDefault("month", (long) now.getMonthValue());

        stuffList.getChildren().clear();
        yearMonth.setText(year + "年" + month + "月");
        date_picker.setValue(null);

        try {
            List<SalaryDao> ss = SalaryServices.selectSalarysByMonth(year, month);
            for (SalaryDao s : ss) {
                CheckBox ck = new CheckBox(s.getStuffDao().getSerial() + "-" + s.getStuffDao().getName());
                ck.setSelected(true);
                ck.setPrefWidth(120);
                TextField tf = new TextField();
                tf.setPromptText("请填入工资发放金额");
                tf.disableProperty().bind(ck.selectedProperty().not());
                HBox box = new HBox(ck, tf, new Label("/" + FXUtils.deciToMoney(s.getTardySalary())));
                box.setUserData(s);
                stuffList.getChildren().add(box);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资发放");
    }
}
