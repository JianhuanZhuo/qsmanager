package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.TupleDao;
import cn.keepfight.qsmanager.service.AnalysisServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.YearScrollPicker;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PieAnalysisController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_year;
    public VBox container;
    public TableView<TupleDao> table;
    public TableColumn<TupleDao, String> tab_custom;
    public TableColumn<TupleDao, BigDecimal> tab_trade;

    private YearScrollPicker yearScrollPicker;
    private int k = 0;
    private List<TupleDao> dataList = new ArrayList<>(1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearScrollPicker = FXWidgetUtil.getYearPicker();
        yearScrollPicker.setOnClose(year -> QSApp.mainPane.reload(FXUtils.ps(new Pair<>("year", year))));
        btn_year.setOnAction(event -> yearScrollPicker.show(btn_year));

        FXWidgetUtil.connect(tab_custom, x->new SimpleStringProperty(x.getK()));
        FXWidgetUtil.connectDecimalColumn(tab_trade, x->new SimpleObjectProperty<>(FXUtils.getDecimal(x.getV())));
        FXWidgetUtil.cellMoney(tab_trade);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void showed(Properties params) {
        container.getChildren().removeAll(container.getChildren().stream()
                .filter(x -> x.getClass().equals(PieChart.class))
                .collect(Collectors.toList()));
        try {
            dataList = AnalysisServers.staticTradeByYear(yearScrollPicker.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        k++;

        PieChart chart = new PieChart();

        chart.setData(FXCollections.observableArrayList(
                dataList.stream()
                        .map(kv -> new PieChart.Data(kv.getK(), Double.valueOf(kv.getV())))
                        .collect(Collectors.toList())
        ));

        table.getItems().setAll(dataList);

        container.getChildren().add(chart);
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("占款比例示意图");
    }
}
