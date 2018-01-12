package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.analysis.PriceAnalysisDao;
import cn.keepfight.qsmanager.dao.analysis.PriceAnalysisDaoWrapper;
import cn.keepfight.qsmanager.service.AnalysisServers;
import cn.keepfight.utils.ConfigUtil;
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
import java.util.*;
import java.util.stream.Collectors;

public class AnalysisProductController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_year;
    public TableView<PriceAnalysisDaoWrapper> table_detail;
    public TableColumn<PriceAnalysisDaoWrapper, String> product;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> cost;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> price;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> profit;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> profitRate;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> totalProfit;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> sum;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon01;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon02;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon03;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon04;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon05;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon06;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon07;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon08;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon09;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon10;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon11;
    public TableColumn<PriceAnalysisDaoWrapper, BigDecimal> mon12;

    private YearScrollPicker yearScrollPicker = FXWidgetUtil.getYearPicker();
    private long data_year;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yearScrollPicker.setOnClose(year -> QSApp.mainPane.reload(FXUtils.ps(new Pair<>("year", year))));
        btn_year.setOnAction(event -> yearScrollPicker.show(btn_year, data_year));

        FXWidgetUtil.connectStrColumn(product, PriceAnalysisDaoWrapper::serialProperty);
        FXWidgetUtil.connectDecimalColumn(sum, PriceAnalysisDaoWrapper::totalProperty);
        FXWidgetUtil.connectDecimalColumn(cost, PriceAnalysisDaoWrapper::costProperty);
        FXWidgetUtil.connectDecimalColumn(price, PriceAnalysisDaoWrapper::priceProperty);
        FXWidgetUtil.connectDecimalColumn(profit, PriceAnalysisDaoWrapper::profitProperty);
        FXWidgetUtil.connectDecimalColumn(profitRate, PriceAnalysisDaoWrapper::profitRateProperty);
        FXWidgetUtil.connectDecimalColumn(totalProfit, PriceAnalysisDaoWrapper::totalProfitProperty);

        FXWidgetUtil.connectDecimalColumn(mon01, p -> p.getPropertyByYM(1));
        FXWidgetUtil.connectDecimalColumn(mon02, p -> p.getPropertyByYM(2));
        FXWidgetUtil.connectDecimalColumn(mon03, p -> p.getPropertyByYM(3));
        FXWidgetUtil.connectDecimalColumn(mon04, p -> p.getPropertyByYM(4));
        FXWidgetUtil.connectDecimalColumn(mon05, p -> p.getPropertyByYM(5));
        FXWidgetUtil.connectDecimalColumn(mon06, p -> p.getPropertyByYM(6));
        FXWidgetUtil.connectDecimalColumn(mon07, p -> p.getPropertyByYM(7));
        FXWidgetUtil.connectDecimalColumn(mon08, p -> p.getPropertyByYM(8));
        FXWidgetUtil.connectDecimalColumn(mon09, p -> p.getPropertyByYM(9));
        FXWidgetUtil.connectDecimalColumn(mon10, p -> p.getPropertyByYM(10));
        FXWidgetUtil.connectDecimalColumn(mon11, p -> p.getPropertyByYM(11));
        FXWidgetUtil.connectDecimalColumn(mon12, p -> p.getPropertyByYM(12));

        FXWidgetUtil.cellMoney(sum, price, profit, cost, totalProfit,
                mon01,
                mon02,
                mon03,
                mon04,
                mon05,
                mon06,
                mon07,
                mon08,
                mon09,
                mon10,
                mon11,
                mon12
        );
        FXWidgetUtil.cellRate(profitRate);

        table_detail.setEditable(true);
        table_detail.getColumns().forEach(x -> x.setEditable(false));
        cost.setEditable(true);

        cost.setOnEditCommit(event -> ConfigUtil.alter(
                "fxapp.properties",
                "prefer.analysis.cost." + event.getRowValue().getSerial(),
                FXUtils.decimalStr(event.getNewValue())));
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
        data_year = (long) params.getOrDefault("year", FXUtils.getYearNow());
        btn_year.setText(data_year + "年");
        try {
            List<PriceAnalysisDao> ls = AnalysisServers.staticPriceByYear(data_year);

            Properties ps = ConfigUtil.load("fxapp.properties");
            List<PriceAnalysisDaoWrapper> datas = ls.stream().collect(Collectors.groupingBy(PriceAnalysisDao::getSerial))
                    .values().stream().map(list -> {
                        PriceAnalysisDao x = list.get(0);
                        PriceAnalysisDaoWrapper wrapper = new PriceAnalysisDaoWrapper();
                        wrapper.setSerial(x.getSerial());

                        wrapper.setTotal(list.stream()
                                .map(PriceAnalysisDao::getNum)
                                .reduce(BigDecimal::add)
                                .orElseGet(() -> new BigDecimal(0)));

                        wrapper.setPrice(list.stream()
                                .map(PriceAnalysisDao::getPrice)
                                .reduce(BigDecimal::add)
                                .orElseGet(() -> new BigDecimal(0))
                                .divide(wrapper.getTotal(), 2, BigDecimal.ROUND_UP));

                        wrapper.setMonthMapCount(
                                list.stream()
                                        .collect(Collectors.toMap(
                                                dao -> Integer.valueOf(dao.getYm().substring(5)),
                                                PriceAnalysisDao::getNum))
                        );
                        wrapper.setCost(FXUtils.getDecimal(ps.getProperty("prefer.analysis.cost." + x.getSerial())));
                        return wrapper;
                    })
                    .sorted(Comparator.comparing(PriceAnalysisDaoWrapper::getSerial))
                    .collect(Collectors.toList());
            table_detail.getItems().setAll(datas);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("年度销售利润统计");
    }
}
