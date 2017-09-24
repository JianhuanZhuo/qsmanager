package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.widget.TableShowGrid;
import cn.keepfight.widget.YearPicker;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 工资表模块控制器
 * Created by tom on 2017/7/19.
 */
public class SalaryMonthController implements ContentCtrl, Initializable{
    @FXML private HBox title;
    @FXML private VBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().add(new TableShowGrid());
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

    }

    @Override
    public List<BarBtn> getBarBtns(Properties params) {
        BarBtn nextMonth = new BarBtn() {
            @Override public String getText() {
                return "后一个月";
            }
            @Override public String getHit() {
                return "点击该按钮查看后一个月的工资表";
            }
            @Override public String getImage() {
                return "next.png";
            }
            @Override public Runnable getAction() {
                return ()-> System.out.println("xx");
            }
        };
        BarBtn prevMonth = new BarBtn() {
            @Override public String getText() {
                return "前一个月";
            }
            @Override public String getHit() {
                return "点击该按钮查看前一个月的工资表";
            }
            @Override public String getImage() {
                return "prev.png";
            }
            @Override public Runnable getAction() {
                return ()-> System.out.println("xx");
            }
        };
        return Arrays.asList(prevMonth, nextMonth);
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("工资表");
    }
}
