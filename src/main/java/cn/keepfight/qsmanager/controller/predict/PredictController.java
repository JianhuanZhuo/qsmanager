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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
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
            List<PredictTradeDao> list = PredictServers.selectOutcomePredictLeft();
            clearRow(grid_income);
            list.stream()
                    .collect(Collectors.groupingBy(
                            PredictTradeDao::getSid)
                    ).values()
                    .stream()
                    .map(ss -> {
                        PredictTradeGroup res = new PredictTradeGroup();
                        res.setName(ss.get(0).getName());
                        res.setSid(ss.get(0).getSid());
                        res.setLefts(ss.stream()
                                .map(s -> new PredictTradeItemDao(s.getYear(), s.getMonth(), s.getLeftsum()))
                                .collect(Collectors.toList()));
                        return res;
                    }).forEach(this::incomeAddRow);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        FXWidgetUtil.simpleTriOper(lab_total, BigDecimal::add, BigDecimal::subtract, lab_cash_total, lab_income_total, lab_outcome_total);
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

    private void incomeAddRow(PredictTradeGroup dao) {
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
        });
        RowConstraints rowConstraints = new RowConstraints();
        grid_income.getRowConstraints().add(rowConstraints);
        System.out.println("grid_income.getRowConstraints().size():" + grid_income.getRowConstraints().size());
        grid_income.addRow(grid_income.getRowConstraints().size() - 1,
                new Label(dao.getName()),
                vBox,
                new Label("+0"));
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
