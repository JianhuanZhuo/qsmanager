package cn.keepfight.qsmanager.controller.predict;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.service.PredictServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.MonthPicker;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class PredictAddController implements Initializable, ContentCtrl {
    public VBox root;
    public Button btn_ym;

    public TextField tf_outcome_salary_fix;
    public TextField tf_outcome_tax;
    public TextField tf_outcome_salary_left;
    public TextField tf_outcome_factory;
    public TextField tf_outcome_fee;
    public TextField tf_outcome_water;
    public TextField tf_outcome_elect;
    public TextField tf_outcome_eng;
    public TextField tf_outcome_other;
    public TextField tf_income;
    public TextField tf_outcome_sup;
    public TextField tf_pub;
    public TextField tf_pri;

    public TextField tf_outcome;

    public Button btn_ok;
    public Button btn_cancel;

    private MonthPicker monthPicker = FXWidgetUtil.getMonthPicker();
    private Long year = 2017L;
    private Long month = 5L;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("新增预算记录历史");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_cancel.setOnAction(event -> QSApp.mainPane.backNav());
        btn_ym.setOnAction(event -> monthPicker.show(btn_ym, new Pair<>(year, month)));
        monthPicker.setOnClose(ym -> {
            year = ym.getKey();
            month = ym.getValue();
            btn_ym.setText(year + "年" + month + "月");
        });

        tf_outcome.textProperty().bind(new StringBinding() {
            {
                bind(
                        tf_outcome_salary_fix.textProperty(),
                        tf_outcome_tax.textProperty(),
                        tf_outcome_salary_left.textProperty(),
                        tf_outcome_factory.textProperty(),
                        tf_outcome_fee.textProperty(),
                        tf_outcome_water.textProperty(),
                        tf_outcome_elect.textProperty(),
                        tf_outcome_eng.textProperty(),
                        tf_outcome_other.textProperty());
            }

            @Override
            protected String computeValue() {
                return FXUtils.deciToMoney(
                        FXUtils.getDecimal(tf_outcome_salary_fix.getText())
                                .add(FXUtils.getDecimal(tf_outcome_tax.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_salary_left.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_factory.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_fee.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_water.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_elect.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_eng.getText()))
                                .add(FXUtils.getDecimal(tf_outcome_other.getText()))
                );
            }
        });

        btn_ok.setOnAction(event -> {
            PredictHistoryDao dao = new PredictHistoryDao();
            dao.setMonth(month);
            dao.setYear(year);
            dao.setOut_pri(FXUtils.getDecimal(tf_pri));
            dao.setOut_pub(FXUtils.getDecimal(tf_pub));
            dao.setIncome(FXUtils.getDecimal(tf_income));
            dao.setOutcome(FXUtils.getDecimal(tf_outcome));
            dao.setOutcome_sup(FXUtils.getDecimal(tf_outcome_sup));
            dao.setOut_tax(FXUtils.getDecimal(tf_outcome_tax.getText()));
            dao.setOut_fix(FXUtils.getDecimal(tf_outcome_salary_fix));
            dao.setOut_salary(FXUtils.getDecimal(tf_outcome_salary_left));
            dao.setOut_salary_lef(FXUtils.getDecimal("0"));
            dao.setOut_factory(FXUtils.getDecimal(tf_outcome_factory));
            dao.setOut_fee(FXUtils.getDecimal(tf_outcome_fee));
            dao.setOut_water(FXUtils.getDecimal(tf_outcome_water));
            dao.setOut_elect(FXUtils.getDecimal(tf_outcome_elect));
            dao.setOut_eng(FXUtils.getDecimal(tf_outcome_eng));
            dao.setOut_other(FXUtils.getDecimal(tf_outcome_other));
            try {
                PredictServers.replaceHistory(dao);
                QSApp.mainPane.backNav();
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("保存失败，请检查网络是否可用");
            }
        });
    }
}
