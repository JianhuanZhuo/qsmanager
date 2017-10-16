package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.salary.SalaryDao;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by tom on 2017/10/16.
 */
public class SalaryEditController implements ContentCtrl, Initializable {
    public VBox root;

    public Button ok;
    public Button cancel;
    public TextField stuff_name;
    public TextField stuff_serial;
    public TextField salary_basic;
    public TextField salary_age;
    public TextField salary_other;
    public TextField salary_total;
    public DatePicker date_picker;
    public TextField salary_fix;

    private long year;
    private long month;
    private long stuffID;
    private SalaryDao dao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 禁用手动输入
        date_picker.getEditor().setDisable(true);

        FXWidgetUtil.simpleTriOper(salary_other, BigDecimal::subtract, BigDecimal::subtract, salary_total, salary_basic, salary_age);

        cancel.setOnAction(e -> QSApp.mainPane.backNav());

        ok.setOnAction(e -> {
            dao.setTotalSalary(FXUtils.getDecimal(salary_total.getText()));
            dao.setBasicSalary(FXUtils.getDecimal(salary_basic.getText()));
            dao.setAgeSalary(FXUtils.getDecimal(salary_age.getText()));
            dao.setFixSalary(FXUtils.getDecimal(salary_fix.getText()));
            try {
                dao.setDate(Date.valueOf(date_picker.getValue()));
            }catch (Exception exp){
                dao.setDate(null);
            }
            if (dao.getFixSalary().compareTo(dao.getTotalSalary())>=0){
                dao.setClear(1);
            }
            try {
                SalaryServices.updateSalarysByMonthAndStuff(dao);
                QSApp.mainPane.backNav();
            } catch (Exception e1) {
                e1.printStackTrace();
                WarningBuilder.build("保存错误","保存错误，请检查网络连接或设置的工资数据是否合理");
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
    public void showedAfter(Properties params) {
        if (!params.containsKey("stuff")) {
            WarningBuilder.build("员工编号错误或不存在！");
            QSApp.mainPane.backNav();
        }

        // 当前时间
        LocalDate now = FXUtils.stampToLocalDate(System.currentTimeMillis());
        year = (long) params.getOrDefault("year", (long) now.getYear());
        month = (long) params.getOrDefault("month", (long) now.getMonthValue());

        stuffID = (long) params.get("stuff");

        try {
            dao = SalaryServices.selectSalarysByMonthAndStuff(year, month, stuffID);
            stuff_name.setText(dao.getStuffDao().getName());
            stuff_serial.setText(dao.getStuffDao().getSerial());
            salary_basic.setText(FXUtils.deciToMoney(dao.getBasicSalary()));
            salary_age.setText(FXUtils.deciToMoney(dao.getAgeSalary()));
            salary_total.setText(FXUtils.deciToMoney(dao.getTotalSalary()));
            salary_fix.setText(FXUtils.deciToMoney(dao.getFixSalary()));
            if (dao.getDate() != null) {
                date_picker.setValue(dao.getDate().toLocalDate());
            } else {
                date_picker.setValue(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("编辑员工工资");
    }
}
