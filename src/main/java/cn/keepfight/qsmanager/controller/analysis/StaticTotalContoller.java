package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.StaticTotalSellDao;
import cn.keepfight.qsmanager.dao.StaticTotalSellDao_i;
import cn.keepfight.qsmanager.service.StaticTotalServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cn.keepfight.utils.FXWidgetUtil.addToGridPane;

public class StaticTotalContoller implements ContentCtrl, Initializable {
    public VBox root;
    public HBox income;
    public HBox static_month;

    private List<StaticTotalSellDao_i> listRaw = new ArrayList<>(2);

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
            listRaw = StaticTotalServers.staticSellAllRaw(2017L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        income.getChildren().removeAll(income.getChildren().stream()
                .filter(node -> node.getClass().equals(GridPane.class))
                .collect(Collectors.toList()));
        static_month.getChildren().removeAll(static_month.getChildren().stream()
                .filter(node -> node.getClass().equals(GridPane.class))
                .collect(Collectors.toList()));

        GridPane gridPane = new GridPane();
        Platform.runLater(()->FXUtils.addStyle("game-grid", gridPane));
        addToGridPane(gridPane, "产品销售", "grey", 0, 0);
        addToGridPane(gridPane, "实际应收", "grey", 0, 1);
        List<String> leftTitles = Arrays.asList("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月");
        leftTitles.forEach(s -> addToGridPane(gridPane, s, "pink-yellow", 0, 2 + leftTitles.indexOf(s)));
        leftTitles.forEach(s -> addToGridPane(gridPane, s, "pink-yellow", 0, 17 + leftTitles.indexOf(s)));


        addToGridPane(gridPane, "客户年度销售总结", "orange", 0, 14);


        addToGridPane(gridPane, "现金收入", "grey", 0, 16);
        addToGridPane(gridPane, "客户年度收入总结", "grey", 0, 29);

        BigDecimal actualTotal = new BigDecimal(0);
        try {
            List<StaticTotalSellDao> list = StaticTotalServers.filterRawSell(listRaw);

            for (int i = 0; i < list.size(); i++) {
                StaticTotalSellDao dao = list.get(i);
                final int j = i;
                addToGridPane(gridPane, dao.getUname(), "grey", j + 1, 0);
                addToGridPane(gridPane, dao.getUname(), "grey", j + 1, 16);
                addToGridPane(gridPane, dao.getSumOrDefaultZero("actual"), "grey", j + 1, 1);
                IntStream.range(1, 13)
                        .mapToObj(index -> new Pair<>(index, "trade_" + index))
                        .map(pair -> new Pair<>(pair.getKey(), dao.getSumOrDefaultZero(pair.getValue())))
                        .forEach(pair -> addToGridPane(gridPane, pair.getValue(), "", j + 1, 1 + pair.getKey()));
                IntStream.range(1, 13)
                        .mapToObj(index -> new Pair<>(index, "remit_" + index))
                        .map(pair -> new Pair<>(pair.getKey(), dao.getSumOrDefaultZero(pair.getValue())))
                        .forEach(pair -> addToGridPane(gridPane, pair.getValue(), "", j + 1, 16 + pair.getKey()));

                String nodeStr = FXUtils.deciToMoney(
                        dao.getUnitToSumMap()
                                .entrySet()
                                .stream()
                                .filter(e -> e.getKey().contains("trade_"))
                                .map(Map.Entry::getValue)
                                .reduce(BigDecimal::add)
                                .orElseGet(() -> new BigDecimal(0)));
                addToGridPane(gridPane, nodeStr, "yellow", j + 1, 14);

                String remitStr = FXUtils.deciToMoney(
                        dao.getUnitToSumMap()
                                .entrySet()
                                .stream()
                                .filter(e -> e.getKey().contains("remit_"))
                                .map(Map.Entry::getValue)
                                .reduce(BigDecimal::add)
                                .orElseGet(() -> new BigDecimal(0)));
                addToGridPane(gridPane, remitStr, "yellow", j + 1, 29);

                actualTotal = actualTotal.add(dao.getOrDefaultZero("actual"));
            }

            addToGridPane(gridPane, "实际总应收未收", "orange", 0, 15);
            addToGridPane(gridPane, FXUtils.deciToMoney(actualTotal), "green", 1, 15);
            if (list.size() > 1) {
                addToGridPane(gridPane, "", "", 2, 15, list.size() - 1, 1);
            }

            addToGridPane(gridPane, "", "", 0, 30, list.size()+1, 1);


            GridPane gridPane2 = new GridPane();
            Platform.runLater(()->FXUtils.addStyle("game-grid", gridPane2));

            addToGridPane(gridPane2, "", "grep", 0, 0);
            leftTitles.forEach(s -> addToGridPane(gridPane2, s, "grep", leftTitles.indexOf(s) + 1, 0));
            addToGridPane(gridPane2, "每月销售总结", "orange", 0, 1);
            addToGridPane(gridPane2, "每月收入总结", "orange", 0, 2);
            addToGridPane(gridPane2, "年度总销售", "orange", 0, 3);
            addToGridPane(gridPane2, "年度总收入", "orange", 0, 4);


            Map<String, List<StaticTotalSellDao_i>> rawGroupList = listRaw.stream().collect(Collectors.groupingBy(StaticTotalSellDao_i::getUnit));
            IntStream.range(1, 13)
                    .mapToObj(index -> new Pair<>(index, "trade_" + index))
                    .map(pair -> new Pair<>(pair.getKey(), rawGroupList.getOrDefault(pair.getValue(), new ArrayList<>(1))))
                    .map(pair -> new Pair<>(pair.getKey(), pair.getValue().stream()
                            .map(StaticTotalSellDao_i::getSum)
                            .reduce(BigDecimal::add)
                            .orElse(new BigDecimal(0))))
                    .map(pair -> new Pair<>(pair.getKey(), FXUtils.deciToMoney(pair.getValue())))
                    .forEach(pair -> addToGridPane(gridPane2, pair.getValue(), "yellow", pair.getKey(), 1));
            IntStream.range(1, 13)
                    .mapToObj(index -> new Pair<>(index, "remit_" + index))
                    .map(pair -> new Pair<>(pair.getKey(), rawGroupList.getOrDefault(pair.getValue(), new ArrayList<>(1))))
                    .map(pair -> new Pair<>(pair.getKey(), pair.getValue().stream()
                            .map(StaticTotalSellDao_i::getSum)
                            .reduce(BigDecimal::add)
                            .orElse(new BigDecimal(0))))
                    .map(pair -> new Pair<>(pair.getKey(), FXUtils.deciToMoney(pair.getValue())))
                    .forEach(pair -> addToGridPane(gridPane2, pair.getValue(), "yellow", pair.getKey(), 2));

            BigDecimal res1 = IntStream.range(1, 13)
                    .mapToObj(index -> "trade_" + index)
                    .map(index -> rawGroupList.getOrDefault(index, new ArrayList<>(1)))
                    .map(sumList -> sumList.stream()
                            .map(StaticTotalSellDao_i::getSum)
                            .reduce(BigDecimal::add)
                            .orElse(new BigDecimal(0)))
                    .reduce(BigDecimal::add).orElse(new BigDecimal(0));
            BigDecimal res2 = IntStream.range(1, 13)
                    .mapToObj(index -> "remit_" + index)
                    .map(index -> rawGroupList.getOrDefault(index, new ArrayList<>(1)))
                    .map(sumList -> sumList.stream()
                            .map(StaticTotalSellDao_i::getSum)
                            .reduce(BigDecimal::add)
                            .orElse(new BigDecimal(0)))
                    .reduce(BigDecimal::add).orElse(new BigDecimal(0));

            addToGridPane(gridPane2, FXUtils.deciToMoney(res1), "green", 1, 3);
            addToGridPane(gridPane2, FXUtils.deciToMoney(res2), "green", 1, 4);
            addToGridPane(gridPane2, "", "", 2, 3, 11,1);
            addToGridPane(gridPane2, "", "", 2, 4, 11,1);


            FXWidgetUtil.setRowConstraints(gridPane);
            FXWidgetUtil.setColumnConstraints(gridPane, 200);
            Platform.runLater(() -> income.getChildren().add(gridPane));

            FXWidgetUtil.setRowConstraints(gridPane2);
            FXWidgetUtil.setColumnConstraints(gridPane2, 200);
            Platform.runLater(() -> static_month.getChildren().add(gridPane2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("统计总表");
    }

}
