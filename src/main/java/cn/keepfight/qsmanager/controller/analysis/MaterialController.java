package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.analysis.MaterialAnalysisDaoWrapper;
import cn.keepfight.qsmanager.dao.analysis.MaterialAnalysisDao_i;
import cn.keepfight.qsmanager.service.StaticTotalServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.YearScrollPicker;
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
import java.util.*;
import java.util.stream.Collectors;

public class MaterialController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_year;
    public TableView<MaterialAnalysisDaoWrapper> table;
    public TableColumn<MaterialAnalysisDaoWrapper, String> tab_month;
    public TableColumn<MaterialAnalysisDaoWrapper, BigDecimal> tab_sum;

    private YearScrollPicker yearScrollPicker;
    private Long year;
    private List<MaterialAnalysisDaoWrapper> dataList = new ArrayList<>(1);
    private List<MaterialAnalysisDao_i> rawList = new ArrayList<>(1);

    private List<TableColumn> tabs = new ArrayList<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearScrollPicker = FXWidgetUtil.getYearPicker();
        btn_year.setOnAction(event -> yearScrollPicker.show(btn_year, year));
        yearScrollPicker.setOnClose(year -> QSApp.mainPane.reload(FXUtils.ps(new Pair<>("year", year))));

        FXWidgetUtil.connectStrColumn(tab_month, MaterialAnalysisDaoWrapper::monthProperty);
        FXWidgetUtil.connectDecimalColumn(tab_sum, x -> new SimpleObjectProperty<>(x.getUnitSumCount()));
        FXWidgetUtil.cellMoney(tab_sum);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void showed(Properties params) {
        year = (Long) params.getOrDefault("year", FXUtils.getYearNow());
        btn_year.setText(year + "年");

        try {
            rawList = StaticTotalServers.staticMaterialByYearRaw(year);
            dataList = StaticTotalServers.staticMaterialByYearRawToDao(rawList).stream()
                    .map(MaterialAnalysisDaoWrapper::new)
                    .sorted(Comparator.comparing(x->Integer.valueOf(x.getMonth())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        table.getColumns().removeAll(tabs);
        tabs.clear();

        rawList.stream()
                .map(MaterialAnalysisDao_i::getName)
                .distinct()
                .map(TableColumn<MaterialAnalysisDaoWrapper, BigDecimal>::new)
                .peek(tabs::add)
                .peek(FXWidgetUtil::cellMoney)
                .peek(column -> FXWidgetUtil.connectDecimalColumn(column, k -> new SimpleObjectProperty<>(k.getUnitMapSumByUnit(column.getText()))))
                .forEach(table.getColumns()::add);

        table.getItems().setAll(dataList);
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("原料采购表");
    }
}
