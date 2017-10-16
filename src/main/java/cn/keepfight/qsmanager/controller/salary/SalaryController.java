package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.model.MaterialModel;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.TableShowGrid;
import cn.keepfight.widget.YearScrollPicker;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 工资表模块控制器
 * Created by tom on 2017/7/19.
 */
public class SalaryController implements ContentCtrl, Initializable {
    @FXML
    private VBox root;
    @FXML
    private Label label_year;

    public TableView table_static;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
        YearScrollPicker yearScrollPicker = FXWidgetUtil.getYearPicker();
        label_year.setOnMouseClicked(e-> yearScrollPicker.show(label_year));
        yearScrollPicker.setOnClose(year->{
            Properties ps = new Properties();
            ps.put("year", year);
            QSApp.mainPane.reload(ps);
        });
    }

    private void initUI(){

    }

    private void initAction(){
        table_static.setRowFactory(tv -> {
            TableRow<MaterialModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {

                }
            });
            return row;
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
    public void showed(Properties params) {
        int year = (int) params.getOrDefault("year", FXUtils.stampToLocalDate().getYear());
        label_year.setText(""+year);
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资表");
    }
}
