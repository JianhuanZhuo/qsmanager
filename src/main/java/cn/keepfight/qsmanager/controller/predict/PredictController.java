package cn.keepfight.qsmanager.controller.predict;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeGroup;
import cn.keepfight.qsmanager.dao.predict.PredictTradeItemDao;
import cn.keepfight.qsmanager.dao.tax.TaxDao;
import cn.keepfight.qsmanager.service.PredictServers;
import cn.keepfight.qsmanager.service.SalaryServices;
import cn.keepfight.qsmanager.service.TaxServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.utils.function.TripPair;
import cn.keepfight.widget.PredictItem;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXUIFactory;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 预算表
 * Created by tom on 2017/9/23.
 */
public class PredictController implements Initializable, ContentCtrl {
    public VBox root;
    public CheckBox cb_cash_pri;
    public TextField tf_cash_pri;
    public CheckBox cb_cash_pub;
    public TextField tf_cash_pub;
    public Label lab_cash_pri;
    public Label lab_cash_pub;
    public TextField lab_cash_total;
    public GridPane grid_income;
    public TextField lab_income_total;
    public TextField lab_outcome_total;
    public TextField lab_total;
    public GridPane grid_outcome;
    public TextField lab_outcome_sup_total;

    public CheckBox ck_outcome_tax;
    public Label lab_outcome_tax;
    public Button btn_outcome_tax;
    public Label lab_warn_tax;

    public CheckBox ck_outcome_factory;
    public TextField tf_outcome_factory;

    public CheckBox ck_outcome_fee;
    public TextField tf_outcome_fee;

    public CheckBox ck_outcome_water;
    public TextField tf_outcome_water;

    public CheckBox ck_outcome_elect;
    public TextField tf_outcome_elect;

    public CheckBox ck_outcome_eng;
    public TextField tf_outcome_eng;

    public CheckBox ck_outcome_other;
    public TextField tf_outcome_other;

    public CheckBox ck_outcome_salary_left;
    public TextField tf_outcome_salary_left;
    public Label lab_outcome_salary_left_all;
    public Button btn_outcome_salary_left;

    public CheckBox ck_outcome_salary_fix;
    public TextField tf_outcome_salary_fix;
    public Button btn_outcome_salary_fix;

    public Button btn_save;
    public Button btn_list;

    private List<Label> outcomeCountLabs = new ArrayList<>(10);
    private List<Label> incomeCountLabs = new ArrayList<>(10);
    private List<TripPair<CheckBox, Node, Class>> accumulateLabelList = new ArrayList<>(12);

    private PredictHistoryDao dao;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("预算表");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tf_cash_pri.disableProperty().bind(cb_cash_pri.selectedProperty().not());
        tf_cash_pub.disableProperty().bind(cb_cash_pub.selectedProperty().not());
        tf_cash_pri.textProperty().addListener((observable, oldValue, newValue) -> countCash());
        tf_cash_pub.textProperty().addListener((observable, oldValue, newValue) -> countCash());
        cb_cash_pri.selectedProperty().addListener((observable, oldValue, newValue) -> countCash());
        cb_cash_pub.selectedProperty().addListener((observable, oldValue, newValue) -> countCash());

        lab_total.textProperty().bind(new StringBinding() {
            {
                bind(lab_cash_total.textProperty(), lab_income_total.textProperty(), lab_outcome_total.textProperty(), lab_outcome_sup_total.textProperty());
            }

            @Override
            protected String computeValue() {
                BigDecimal cash = FXUtils.getDecimal(lab_cash_total.getText(), new BigDecimal(0));
                BigDecimal income = FXUtils.getDecimal(lab_income_total.getText(), new BigDecimal(0));
                BigDecimal outcome = FXUtils.getDecimal(lab_outcome_total.getText(), new BigDecimal(0));
                BigDecimal outcome_sup = FXUtils.getDecimal(lab_outcome_sup_total.getText(), new BigDecimal(0));
                return FXUtils.deciToMoney(cash.add(income).subtract(outcome).subtract(outcome_sup));
            }
        });

        accumulateLabelList.addAll(
                Arrays.asList(
                        new TripPair<>(ck_outcome_tax, lab_outcome_tax, Label.class),
                        new TripPair<>(ck_outcome_salary_fix, tf_outcome_salary_fix, TextField.class),
                        new TripPair<>(ck_outcome_salary_left, tf_outcome_salary_left, TextField.class),
                        new TripPair<>(ck_outcome_factory, tf_outcome_factory, TextField.class),
                        new TripPair<>(ck_outcome_fee, tf_outcome_fee, TextField.class),
                        new TripPair<>(ck_outcome_water, tf_outcome_water, TextField.class),
                        new TripPair<>(ck_outcome_elect, tf_outcome_elect, TextField.class),
                        new TripPair<>(ck_outcome_eng, tf_outcome_eng, TextField.class),
                        new TripPair<>(ck_outcome_other, tf_outcome_other, TextField.class)
                ));

        lab_outcome_total.textProperty().bind(new StringBinding() {
            {
                accumulateLabelList.forEach(trip -> {
                    bind(trip.getA().selectedProperty());
                    if (trip.getC().equals(Label.class)) {
                        bind(((Label) trip.getB()).textProperty());
                    } else if (trip.getC().equals(TextField.class)) {
                        bind(((TextField) trip.getB()).textProperty());
                    }
                });
            }

            @Override
            protected String computeValue() {
                BigDecimal res = new BigDecimal(0);
                for (TripPair<CheckBox, Node, Class> trip : accumulateLabelList) {
                    if (trip.getA().isSelected()) {
                        if (trip.getC().equals(Label.class)) {
                            res = res.add(FXUtils.getDecimal((((Label) trip.getB()).getText())));
                        } else if (trip.getC().equals(TextField.class)) {
                            res = res.add(FXUtils.getDecimal((((TextField) trip.getB()).getText())));
                        }
                    }
                }
                return FXUtils.deciToMoney(res);
            }
        });

        btn_list.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.PREDICT_LIST));
        btn_save.setOnAction(event -> {
            // 记录填写记录
            FXWidgetUtil.addDefaultList(
                    new Pair<>("prefer.predict.tf_cash_pri", tf_cash_pri.getText()),
                    new Pair<>("prefer.predict.tf_cash_pub", tf_cash_pub.getText()),

                    new Pair<>("prefer.predict.tf_outcome_factory", tf_outcome_factory.getText()),
                    new Pair<>("prefer.predict.tf_outcome_fee", tf_outcome_fee.getText()),
                    new Pair<>("prefer.predict.tf_outcome_water", tf_outcome_water.getText()),
                    new Pair<>("prefer.predict.tf_outcome_elect", tf_outcome_elect.getText()),
                    new Pair<>("prefer.predict.tf_outcome_eng", tf_outcome_eng.getText()),
                    new Pair<>("prefer.predict.tf_outcome_other", tf_outcome_other.getText())
            );

            System.out.println("?");
            saveParam();
        });

        btn_outcome_salary_left.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.SALARY));
        btn_outcome_tax.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.tax$TAX));


    }

    private void countCash() {
        BigDecimal z = new BigDecimal(0);
        try {
            if (cb_cash_pri.isSelected() && !tf_cash_pri.getText().equals("")) {
                z = z.add(new BigDecimal(tf_cash_pri.getText()));
                lab_cash_pri.setVisible(false);
            }
        } catch (Exception e) {
            lab_cash_pri.setVisible(true);
        }
        try {
            if (cb_cash_pub.isSelected() && !tf_cash_pub.getText().equals("")) {
                z = z.add(new BigDecimal(tf_cash_pub.getText()));
                lab_cash_pri.setVisible(false);
            }
            lab_cash_pub.setVisible(false);
        } catch (Exception e) {
            lab_cash_pub.setVisible(true);
        }
        lab_cash_total.setText(FXUtils.deciToMoney(z));
    }

    @Override
    public void showed(Properties params) {
        try {
            dao = PredictServers.selectPredictHistory(FXUtils.getYearNow(), FXUtils.getMonthNow());
            if (dao==null){
                dao = new PredictHistoryDao();
                dao.setYear(FXUtils.getYearNow());
                dao.setMonth(FXUtils.getMonthNow());
            }
        } catch (Exception e) {
            e.printStackTrace();
            dao = new PredictHistoryDao();
            dao.setYear(FXUtils.getYearNow());
            dao.setMonth(FXUtils.getMonthNow());
        }

        clearRow(grid_outcome);
        clearRow(grid_income);
        try {
            List<PredictTradeDao> outcomes = PredictServers.selectOutcomePredictLeft();
            groupAndSetTrade(outcomes, x -> outcomeAddRow(x, outcomeCountLabs::add));
            FXWidgetUtil.calculateListForStr(
                    lab_outcome_sup_total.textProperty(),
                    outcomeCountLabs,
                    Labeled::textProperty,
                    x -> new BigDecimal(x.getText().replace(",", ""))
            );
            List<PredictTradeDao> income = PredictServers.selectIncomePredictLeft();
            groupAndSetTrade(income, x -> incomeAddRow(x, incomeCountLabs::add));
            FXWidgetUtil.calculateListForStr(
                    lab_income_total.textProperty(),
                    incomeCountLabs,
                    Labeled::textProperty,
                    x -> new BigDecimal(x.getText().replace(",", ""))
            );

            lab_outcome_salary_left_all.setText(FXUtils.deciToMoney(SalaryServices.staticTardyAllInOneNumber()));
            tf_outcome_salary_left.setText(lab_outcome_salary_left_all.getText());

            try{
                lab_outcome_tax.setText(FXUtils.deciToMoney(getTaxTotal()));
                lab_warn_tax.setText("");
            }catch (Exception e){
                lab_outcome_tax.setText("0");
                lab_warn_tax.setText("税金获取失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(tf_outcome_factory, "prefer.predict.tf_outcome_factory"),
                new Pair<>(tf_outcome_fee, "prefer.predict.tf_outcome_fee"),
                new Pair<>(tf_outcome_water, "prefer.predict.tf_outcome_water"),
                new Pair<>(tf_outcome_elect, "prefer.predict.tf_outcome_elect"),
                new Pair<>(tf_outcome_eng, "prefer.predict.tf_outcome_eng"),
                new Pair<>(tf_outcome_other, "prefer.predict.tf_outcome_other"),

                new Pair<>(tf_cash_pri, "prefer.predict.tf_cash_pri"),
                new Pair<>(tf_cash_pub, "prefer.predict.tf_cash_pub")
        );

    }

    private void groupAndSetTrade(List<PredictTradeDao> list, Consumer<PredictTradeGroup> add) {
        list.stream()
                .collect(Collectors.groupingBy(
                        PredictTradeDao::getUid)
                ).values()
                .stream()
                .map(ss -> {
                    PredictTradeGroup res = new PredictTradeGroup();
                    res.setName(ss.get(0).getName());
                    res.setUid(ss.get(0).getUid());
                    res.setLefts(ss.stream()
                            .map(s -> new PredictTradeItemDao(s.getYear(), s.getMonth(), s.getLeftsum()))
                            .collect(Collectors.toList()));
                    return res;
                }).forEach(add);
    }

    private void incomeAddRow(PredictTradeGroup dao, Consumer<Label> counterConsumer) {
        AddRow(grid_income, dao, counterConsumer);
    }

    private void outcomeAddRow(PredictTradeGroup dao, Consumer<Label> counterConsumer) {
        AddRow(grid_outcome, dao, counterConsumer);
    }

    private void AddRow(GridPane grid, PredictTradeGroup dao, Consumer<Label> counterConsumer) {
        List<PredictItem> items = new ArrayList<>(10);
        VBox vBox = new VBox(3.0);
        vBox.setPadding(new Insets(3.0));
        dao.getLefts().forEach(x -> {
            PredictItem item = FXWidgetUtil.getPredictItem();
            item.set(new PredictItem.PredictItemObj(
                    false,
                    new BigDecimal(0),
                    x.getTotal(),
                    x.getYear(),
                    x.getMonth()
            ));
            vBox.getChildren().add(item.getRoot());
            items.add(item);
        });
        RowConstraints rowConstraints = new RowConstraints();
        grid.getRowConstraints().add(rowConstraints);
        Label lab_count = new Label("");
        FXWidgetUtil.calculateListForStr(
                lab_count.textProperty(),
                items,
                PredictItem::getAccumulateProperty,
                x -> x.getAccumulateProperty().get()
        );

        grid.addRow(grid.getRowConstraints().size() - 1,
                new Label(dao.getName()),
                vBox,
                lab_count);

        counterConsumer.accept(lab_count);
    }

    private void clearRow(GridPane grid) {
        Set<Node> deleteNodes = new HashSet<>();
        for (Node child : grid.getChildren()) {
            if (!(child instanceof Label) && !(child instanceof VBox)) {
                continue;
            }
            deleteNodes.add(child);
        }
        grid.getChildren().removeAll(deleteNodes);
    }

    private BigDecimal getTaxTotal() throws Exception {
        LocalDate now = FXUtils.stampToLocalDate();
        Long year = (long) now.getYear();
        Long month = (long) now.getMonthValue();
        return TaxServers.selectByMonth(year, month).getTotal();
    }

    private void saveParam(){
        new Thread(()->{
            dao.setOut_pri(FXUtils.getDecimal(tf_cash_pri));
            dao.setOut_pub(FXUtils.getDecimal(tf_cash_pub));
            dao.setIncome(FXUtils.getDecimal(lab_income_total));
            dao.setOutcome(FXUtils.getDecimal(lab_outcome_sup_total));
            dao.setOutcome_sup(FXUtils.getDecimal(lab_outcome_total));
            dao.setOut_tax(FXUtils.getDecimal(lab_outcome_tax.getText()));
            dao.setOut_fix(FXUtils.getDecimal(tf_outcome_salary_fix));
            dao.setOut_salary(FXUtils.getDecimal(tf_outcome_salary_left));
            dao.setOut_salary_lef(FXUtils.getDecimal(lab_outcome_salary_left_all.getText()));
            dao.setOut_factory(FXUtils.getDecimal(tf_outcome_factory));
            dao.setOut_fee(FXUtils.getDecimal(tf_outcome_fee));
            dao.setOut_water(FXUtils.getDecimal(tf_outcome_water));
            dao.setOut_elect(FXUtils.getDecimal(tf_outcome_elect));
            dao.setOut_eng(FXUtils.getDecimal(tf_outcome_eng));
            dao.setOut_other(FXUtils.getDecimal(tf_outcome_other));
            try {
                PredictServers.replaceHistory(dao);
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("保存失败，请检查网络是否可用");
            }
        }).start();
    }
}
