package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.TPStatModel;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 购付统计界面控制器
 * Created by tom on 2017/6/10.
 */
public class OutComeStatController implements Initializable, OutComeSub{
    //购付统计部分
    @FXML
    private ChoiceBox<Long> stat_year_sel;
    @FXML
    private TableView<TPStatModel> stat_takePay_table;
    @FXML
    private TableColumn<TPStatModel, String> mon;
    @FXML
    private TableColumn<TPStatModel, String> take;
    @FXML
    private TableColumn<TPStatModel, String> pay;
    @FXML
    private Label tp_total_take;
    @FXML
    private Label tp_total_pay;
//    @FXML
//    private Label tp_total_un_pay;
//    @FXML
//    private Label tp_total_spend;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stat_year_sel.setConverter(FXUtils.converter(x->x+"年", "全部年份"));
        stat_year_sel.setOnAction(event -> loadTPStat());
        mon.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getMon()));
        take.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getRec()));
        pay.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPay()));

        FXWidgetUtil.calculate(stat_takePay_table.getItems(), (m)->new BigDecimal(m.getPay()), tp_total_take::setText);
        FXWidgetUtil.calculate(stat_takePay_table.getItems(), (m)->new BigDecimal(m.getRec()), tp_total_pay::setText);
    }

    @Override
    public void setYearSelection(List<Long> ls) {
        stat_year_sel.getItems().setAll(ls);
    }


    /**
     * 所谓事件处理器，加载供应送货数据
     */
    private void loadTPStat() {
        if (stat_year_sel.getSelectionModel().isEmpty()) {
            return;
        }
        Long year = stat_year_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                stat_takePay_table.getItems().setAll(FXCollections.observableList(QSApp.service.getSupAnnualService().tpStat(year)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
