package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.service.StaticTotalServers;
import cn.keepfight.utils.FXUtils;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cn.keepfight.utils.FXWidgetUtil;

public class StaticTotalTradeController implements ContentCtrl, Initializable {

    public VBox root;
    public HBox income;


    private Map<String, BigDecimal> rawMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
            rawMap = StaticTotalServers.staticTradeAllByYear(2017L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        income.getChildren().removeAll(income.getChildren().stream()
                .filter(node -> node.getClass().equals(GridPane.class))
                .collect(Collectors.toList()));

        GridPane gridPane = new GridPane();
        FXUtils.addStyle("game-grid", gridPane);
        FXWidgetUtil.addToGridPane(gridPane, "", "", 0, 0);
        FXWidgetUtil.addToGridPane(gridPane, "销售交易额", "grey", 0, 1);
        FXWidgetUtil.addToGridPane(gridPane, "采购交易额", "grey", 0, 2);
        FXWidgetUtil.addToGridPane(gridPane, "交易利润差", "grey", 0, 3);
        List<String> leftTitles = Arrays.asList("年度合计", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月");
        leftTitles.forEach(s -> FXWidgetUtil.addToGridPane(gridPane, s, "grey", 1 + leftTitles.indexOf(s), 0));
        IntStream.range(1, 13).forEach(x -> {
            BigDecimal custTrade = rawMap.getOrDefault("cust_trade_" + x, new BigDecimal(0));
            BigDecimal supTrade = rawMap.getOrDefault("sup_trade_" + x, new BigDecimal(0));
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(custTrade), "", 1 + x, 1);
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(supTrade), "", 1 + x, 2);
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(custTrade.subtract(supTrade)), "pink-yellow", 1 + x, 3);
        });
        BigDecimal cust_trade = rawMap.entrySet().stream()
                .filter(en -> en.getKey().contains("cust_trade"))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        BigDecimal sup_trade = rawMap.entrySet().stream()
                .filter(en -> en.getKey().contains("sup_trade"))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(cust_trade), "green", 1, 1);
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(sup_trade), "green", 1, 2);
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(cust_trade.subtract(sup_trade)), "green", 1, 3);

        FXWidgetUtil.addToGridPane(gridPane, "", "", 0, 4, 14,1);

        FXWidgetUtil.addToGridPane(gridPane, "", "", 0, 5);
        FXWidgetUtil.addToGridPane(gridPane, "客户付款额", "grey", 0, 6);
        FXWidgetUtil.addToGridPane(gridPane, "采购付款额", "grey", 0, 7);
        FXWidgetUtil.addToGridPane(gridPane, "付款差额", "grey", 0, 8);

        leftTitles.forEach(s -> FXWidgetUtil.addToGridPane(gridPane, s, "grey", 1 + leftTitles.indexOf(s), 5));
        IntStream.range(1, 13).forEach(x -> {
            BigDecimal custTrade = rawMap.getOrDefault("cust_remit_" + x, new BigDecimal(0));
            BigDecimal supTrade = rawMap.getOrDefault("sup_remit_" + x, new BigDecimal(0));
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(custTrade), "", 1 + x, 6);
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(supTrade), "", 1 + x, 7);
            FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(custTrade.subtract(supTrade)), "pink-yellow", 1 + x, 8);
        });

        BigDecimal cust_remit = rawMap.entrySet().stream()
                .filter(en -> en.getKey().contains("cust_remit_"))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        BigDecimal sup_remit = rawMap.entrySet().stream()
                .filter(en -> en.getKey().contains("sup_remit_"))
                .map(Map.Entry::getValue)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(cust_remit), "green", 1, 6);
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(sup_remit), "green", 1, 7);
        FXWidgetUtil.addToGridPane(gridPane, FXUtils.deciToMoney(cust_remit.subtract(sup_remit)), "green", 1, 8);

        FXWidgetUtil.setRowConstraints(gridPane);
        FXWidgetUtil.setColumnConstraints(gridPane, 200);
        Platform.runLater(() -> income.getChildren().add(gridPane));
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("交易额总表");
    }
}
