package cn.keepfight.qsmanager.controller.predict;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.predict.PredictTradeDao;
import cn.keepfight.qsmanager.dao.predict.PredictTradeGroup;
import cn.keepfight.qsmanager.dao.predict.PredictTradeItemDao;
import cn.keepfight.qsmanager.service.PredictServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.PredictItem;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
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

    private List<Label> outcomeCountLabs = new ArrayList<>(10);
    private List<Label> incomeCountLabs = new ArrayList<>(10);

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
            clearRow(grid_outcome);
            clearRow(grid_income);
            groupAndSetTrade(PredictServers.selectOutcomePredictLeft(), x -> outcomeAddRow(x, outcomeCountLabs::add));
            FXWidgetUtil.calculateListForStr(
                    lab_outcome_sup_total.textProperty(),
                    outcomeCountLabs,
                    Labeled::textProperty,
                    x -> new BigDecimal(x.getText().replace(",", ""))
            );
            groupAndSetTrade(PredictServers.selectIncomePredictLeft(), x -> incomeAddRow(x, incomeCountLabs::add));
            FXWidgetUtil.calculateListForStr(
                    lab_income_total.textProperty(),
                    incomeCountLabs,
                    Labeled::textProperty,
                    x -> new BigDecimal(x.getText().replace(",", ""))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
