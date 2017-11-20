package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.StaticTotalSellDao;
import cn.keepfight.qsmanager.dao.analysis.UnitMonthDao;
import cn.keepfight.qsmanager.dao.analysis.UnitMonthDaoWrapper;
import cn.keepfight.qsmanager.service.AnalysisServers;
import cn.keepfight.qsmanager.service.StaticTotalServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.YearScrollPicker;
import javafx.beans.binding.StringBinding;
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

public class OutcomeStaticController implements ContentCtrl, Initializable {

    public VBox root;
    public TableView<UnitMonthDaoWrapper> table;
    public TableColumn<UnitMonthDaoWrapper, String> supply;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> count;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month1;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month2;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month3;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month4;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month5;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month6;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month7;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month8;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month9;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month10;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month11;
    public TableColumn<UnitMonthDaoWrapper, BigDecimal> month12;
    public Button btn_year;

    private YearScrollPicker yearScrollPicker;
    private List<UnitMonthDao> dataList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXWidgetUtil.connectStrColumn(supply, UnitMonthDaoWrapper::unameProperty);
        FXWidgetUtil.connectDecimalColumn(count, UnitMonthDaoWrapper::countProperty);
        FXWidgetUtil.connectDecimalColumn(month1, UnitMonthDaoWrapper::mon1Property);
        FXWidgetUtil.connectDecimalColumn(month2, UnitMonthDaoWrapper::mon2Property);
        FXWidgetUtil.connectDecimalColumn(month3, UnitMonthDaoWrapper::mon3Property);
        FXWidgetUtil.connectDecimalColumn(month4, UnitMonthDaoWrapper::mon4Property);
        FXWidgetUtil.connectDecimalColumn(month5, UnitMonthDaoWrapper::mon5Property);
        FXWidgetUtil.connectDecimalColumn(month6, UnitMonthDaoWrapper::mon6Property);
        FXWidgetUtil.connectDecimalColumn(month7, UnitMonthDaoWrapper::mon7Property);
        FXWidgetUtil.connectDecimalColumn(month8, UnitMonthDaoWrapper::mon8Property);
        FXWidgetUtil.connectDecimalColumn(month9, UnitMonthDaoWrapper::mon9Property);
        FXWidgetUtil.connectDecimalColumn(month10, UnitMonthDaoWrapper::mon10Property);
        FXWidgetUtil.connectDecimalColumn(month11, UnitMonthDaoWrapper::mon11Property);
        FXWidgetUtil.connectDecimalColumn(month12, UnitMonthDaoWrapper::mon12Property);
        FXWidgetUtil.cellMoney(count, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12);

        yearScrollPicker = FXWidgetUtil.getYearPicker();
        btn_year.setOnAction(event -> yearScrollPicker.show(btn_year));
        yearScrollPicker.setOnClose(year -> QSApp.mainPane.reload(FXUtils.ps(new Pair<>("year", year))));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void showed(Properties params) {
        Long year = (Long) params.getOrDefault("year", FXUtils.getYearNow());
        btn_year.setText(year+"年");
        try {
            dataList = AnalysisServers.staticTakeTradeByYear(year);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        table.getItems().setAll(dataList.stream().map(UnitMonthDaoWrapper::new).collect(Collectors.toList()));
    }

    @Override
    public void loaded() {
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("采购分析");
    }
}
