package cn.keepfight.qsmanager.controller.analysis;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.dao.analysis.SellSumDao;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.service.AnalysisServers;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.UnitScrollPicker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AnalysisCustomController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_cust;

    private UnitScrollPicker<CustomModel> unitScrollPicker = FXWidgetUtil.getUnitPicker();

    @FXML
    private BarChart<String, BigDecimal> bc;

    private XYChart.Series<String, BigDecimal> series1;
    private List<SellSumDao> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_cust.setOnAction(event -> unitScrollPicker.show(btn_cust));
        unitScrollPicker.setOnClose(model -> {
            btn_cust.setText(model.getSerial() + "-" + model.getName());
            QSApp.mainPane.reload(FXUtils.ps(
                    new Pair<>("cid", model.getId()),
                    new Pair<>("cname", model.getSerial() + "-" + model.getName())));
        });


        series1 = new XYChart.Series<>();
        bc.getData().add(series1);
        bc.getXAxis().setLabel("销售箱数");
        bc.getYAxis().setLabel("年月");
    }

    @Override
    public void showed(Properties params) {
        Long data_cid = (Long) params.get("cid");
        btn_cust.setText(params.getProperty("cname", "无客户"));
        try {
            unitScrollPicker.setDataList(QSApp.service.getCustomService().selectAll(), x -> x.getSerial() + "-" + x.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (data_cid ==null){
                list=null;
            }else {
                list = AnalysisServers.staticSellSumByCust(data_cid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showedAfter(Properties params) {
        Platform.runLater(() -> {
            series1.getData().clear();
            if (list != null) {
                list.forEach(x -> {
                    XYChart.Data<String, BigDecimal> d = new XYChart.Data<>(x.getYm(), x.getSum());
                    series1.getData().add(d);
                });
            }
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("销售情况分析");
    }
}
