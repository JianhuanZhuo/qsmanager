package cn.keepfight.qsmanager.controller.predict;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDao;
import cn.keepfight.qsmanager.dao.predict.PredictHistoryDaoWrapper;
import cn.keepfight.qsmanager.service.PredictServers;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by 卓建欢 on 2017/10/31.
 */
public class PredictListController implements ContentCtrl, Initializable {
    public VBox root;
    public TableView<PredictHistoryDaoWrapper> table;
    public TableColumn<PredictHistoryDaoWrapper, String> tab_ym;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_pri;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_pub;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_income;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_outcome;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_outcome_sup;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_tax;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_fix;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_salary;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_salary_lef;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_factory;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_fee;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_water;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_elect;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_eng;
    public TableColumn<PredictHistoryDaoWrapper, BigDecimal> tab_out_other;
    public LineChart<String, BigDecimal> chart;
    public HBox chart_container;
    public Button btn_del;
    public Button btn_add;

    private XYChart.Series<String, BigDecimal> incomeSeries = new XYChart.Series<>();
    private XYChart.Series<String, BigDecimal> outcomeSeries = new XYChart.Series<>();
    private XYChart.Series<String, BigDecimal> outcome_supSeries = new XYChart.Series<>();
    private XYChart.Series<String, BigDecimal> salary_lefSeries = new XYChart.Series<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXWidgetUtil.connectDecimalColumn(tab_out_pri, PredictHistoryDaoWrapper::out_priProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_pub, PredictHistoryDaoWrapper::out_pubProperty);
        FXWidgetUtil.connectDecimalColumn(tab_income, PredictHistoryDaoWrapper::incomeProperty);
        FXWidgetUtil.connectDecimalColumn(tab_outcome, PredictHistoryDaoWrapper::outcomeProperty);
        FXWidgetUtil.connectDecimalColumn(tab_outcome_sup, PredictHistoryDaoWrapper::outcome_supProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_tax, PredictHistoryDaoWrapper::out_taxProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_fix, PredictHistoryDaoWrapper::out_fixProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_salary, PredictHistoryDaoWrapper::out_salaryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_salary_lef, PredictHistoryDaoWrapper::out_salary_lefProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_factory, PredictHistoryDaoWrapper::out_factoryProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_fee, PredictHistoryDaoWrapper::out_feeProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_water, PredictHistoryDaoWrapper::out_waterProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_elect, PredictHistoryDaoWrapper::out_electProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_eng, PredictHistoryDaoWrapper::out_engProperty);
        FXWidgetUtil.connectDecimalColumn(tab_out_other, PredictHistoryDaoWrapper::out_otherProperty);

        tab_ym.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getYear() + "-" + param.getValue().getMonth()));

        FXWidgetUtil.cellMoney(tab_out_pri,
                tab_out_pub,
                tab_income,
                tab_outcome,
                tab_outcome_sup,
                tab_out_tax,
                tab_out_fix,
                tab_out_salary,
                tab_out_salary_lef,
                tab_out_factory,
                tab_out_fee,
                tab_out_water,
                tab_out_elect,
                tab_out_eng,
                tab_out_other);

        btn_del.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        btn_del.setOnAction(event -> {
            PredictHistoryDaoWrapper sel = table.getSelectionModel().getSelectedItem();
            try {
                PredictServers.delPredictHistory(sel.getYear(), sel.getMonth());
                QSApp.mainPane.refresh();
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("删除失败，请检查网络是否可用，刷新再试");
            }
        });
        btn_add.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.PREDICT_ADD));

        incomeSeries.setName("销售收入");
        outcomeSeries.setName("其他支出");
        outcome_supSeries.setName("采购支出");
        salary_lefSeries.setName("剩余工资");
        ObservableList<XYChart.Series<String, BigDecimal>> dataList = chart.getData();
        dataList.add(incomeSeries);
        dataList.add(outcomeSeries);
        dataList.add(outcome_supSeries);
//        dataList.add(salary_lefSeries);
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
        try {
            List<PredictHistoryDao> datas = PredictServers.selectAllPredictHistory();
            List<PredictHistoryDaoWrapper> list = datas.stream()
                    .map(PredictHistoryDaoWrapper::new)
                    .collect(Collectors.toList());
            table.getItems().setAll(list);

            incomeSeries.getData().clear();
            outcomeSeries.getData().clear();
            salary_lefSeries.getData().clear();
            datas.forEach(d->{
                incomeSeries.getData().add(new XYChart.Data<>(d.getYear() + "-" + d.getMonth(), d.getIncome()));
                outcomeSeries.getData().add(new XYChart.Data<>(d.getYear() + "-" + d.getMonth(), d.getOutcome()));
                outcome_supSeries.getData().add(new XYChart.Data<>(d.getYear() + "-" + d.getMonth(), d.getOutcome_sup()));
                salary_lefSeries.getData().add(new XYChart.Data<>(d.getYear() + "-" + d.getMonth(), d.getOut_salary_lef()));
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("预算历史记录");
    }
}
