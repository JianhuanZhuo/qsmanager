package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.qsmanager.dao.analysis.SellAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.SellAnalysisDaoWrapper;
import cn.keepfight.qsmanager.service.AnalysisServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.MonthPicker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AnalysisSellController implements ContentCtrl, Initializable {
    public VBox root;
    public TableView<SellAnalysisDaoWrapper> table_detail;
    public TableColumn<SellAnalysisDaoWrapper, String> custom;
    public TableColumn<SellAnalysisDaoWrapper, BigDecimal> sum;
    public Button btn_ym;
    public Button btn_custom;

    private List<TableColumn<SellAnalysisDaoWrapper, BigDecimal>> tabs = new ArrayList<>(12);

    private long data_year = 2017L;
    private long data_month = 9L;
    private MonthPicker monthPicker = FXWidgetUtil.getMonthPicker();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXWidgetUtil.connectStrColumn(custom, SellAnalysisDaoWrapper::cnameProperty);
        FXWidgetUtil.connectDecimalColumn(sum, SellAnalysisDaoWrapper::sumCountProperty);
        FXWidgetUtil.cellMoney(sum);

        monthPicker.setOnClose(ym -> QSApp.mainPane.reload(FXUtils.ps(
                new Pair<>("year", ym.getKey()),
                new Pair<>("month", ym.getValue())
        )));
        btn_ym.setOnAction(event -> monthPicker.show(btn_ym, new Pair<>(data_year, data_month)));

        btn_custom.setOnAction(event -> QSApp.mainPane.changeTo(MainPaneList.analysis$CUSTOM));
    }

    @Override
    public void showed(Properties params) {
        table_detail.getColumns().removeAll(tabs);
        tabs.clear();

        data_year = (long) params.getOrDefault("year", FXUtils.getYearNow());
        data_month = (long) params.getOrDefault("month", FXUtils.getMonthNow());
        btn_ym.setText(data_year + "年" + data_month + "月");

        try {
            List<SellAnalysisDao> list = AnalysisServers.staticSellByMonth(data_year, data_month);
            List<String> columns = list.stream()
                    .map(SellAnalysisDao::getItem_serial)
                    .distinct()
                    .collect(Collectors.toList());

            List<SellAnalysisDaoWrapper> wrappers = list.stream()
                    .collect(Collectors.groupingBy(SellAnalysisDao::getCid))
                    .values().stream()
                    .map(daos -> {
                        SellAnalysisDaoWrapper wrapper = new SellAnalysisDaoWrapper();
                        wrapper.setCname(daos.get(0).getCname());
                        wrapper.setCid(daos.get(0).getCid());
                        wrapper.setSumMap(daos.stream()
                                .collect(Collectors.toMap(SellAnalysisDao::getItem_serial, SellAnalysisDao::getSum))
                        );
                        return wrapper;
                    }).collect(Collectors.toList());

            table_detail.getItems().setAll(wrappers);

            if (columns.size() > 0) {
                for (String s : columns) {
                    TableColumn<SellAnalysisDaoWrapper, BigDecimal> column = new TableColumn<>(s);
                    Platform.runLater(() -> table_detail.getColumns().add(column));
                    tabs.add(column);
                    FXWidgetUtil.cellMoney(column);
                    column.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSumMap().get(s)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("销售情况分析");
    }
}
