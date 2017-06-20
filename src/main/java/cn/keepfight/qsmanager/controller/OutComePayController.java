package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.MonStatModel;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 支出界面的付款统计部分
 * Created by tom on 2017/6/10.
 */
public class OutComePayController implements Initializable, OutComeSub {

    //年度付款部分
    @FXML private ChoiceBox<Long> ap_year_sel;
    @FXML private TableView<MonStatModel> ap_table;
    @FXML private TableColumn<MonStatModel, String> sup;
    @FXML private TableColumn<MonStatModel, String> total;
    @FXML private TableColumn<MonStatModel, String> mon1;
    @FXML private TableColumn<MonStatModel, String> mon2;
    @FXML private TableColumn<MonStatModel, String> mon3;
    @FXML private TableColumn<MonStatModel, String> mon4;
    @FXML private TableColumn<MonStatModel, String> mon5;
    @FXML private TableColumn<MonStatModel, String> mon6;
    @FXML private TableColumn<MonStatModel, String> mon7;
    @FXML private TableColumn<MonStatModel, String> mon8;
    @FXML private TableColumn<MonStatModel, String> mon9;
    @FXML private TableColumn<MonStatModel, String> mon10;
    @FXML private TableColumn<MonStatModel, String> mon11;
    @FXML private TableColumn<MonStatModel, String> mon12;
    @FXML
    private Label ap_total;

    @Override
    public void setYearSelection(List<Long> ls) {
        ap_year_sel.getItems().setAll(ls);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ap_year_sel.setOnAction(event -> loadTakeStat());
        sup.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getSup()));
        total.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getTotal())));
        mon1.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon1())));
        mon2.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon2())));
        mon3.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon3())));
        mon4.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon4())));
        mon5.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon5())));
        mon6.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon6())));
        mon7.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon7())));
        mon8.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon8())));
        mon9.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon9())));
        mon10.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon10())));
        mon11.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon11())));
        mon12.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(FXUtils.decimalStr(cellFeature.getValue().getMon12())));

        FXWidgetUtil.calculate(ap_table.getItems(), MonStatModel::getTotal, ap_total::setText);
    }


    /**
     * 所谓事件处理器，加载供应送货数据
     */
    private void loadTakeStat() {
        if (ap_year_sel.getSelectionModel().isEmpty()) {
            return;
        }
        Long year = ap_year_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                ap_table.getItems().setAll(FXCollections.observableList(QSApp.service.getSupAnnualService().payStat(year)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
