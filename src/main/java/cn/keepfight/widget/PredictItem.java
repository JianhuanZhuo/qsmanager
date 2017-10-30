package cn.keepfight.widget;

import cn.keepfight.utils.FXUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 预算表中的小组件
 * Created by 卓建欢 on 2017/9/23.
 */
public class PredictItem implements Initializable, Widget<PredictItem.PredictItemObj> {
    public HBox root;
    public Label label_total;
    public CheckBox check;
    public TextField text_edit;
    public Button btn_all;
    public HBox content;
    private Long year = 2017L;
    private Long month = 6L;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set(new PredictItemObj(
                false,
                new BigDecimal(0),
                new BigDecimal(0),
                2017L,
                6L));
        btn_all.setOnAction(event -> text_edit.setText(label_total.getText()));
        content.disableProperty().bind(check.selectedProperty().not());
        check.setText(year+"年"+month+"月");
    }

    @Override
    public void set(PredictItemObj data) {
        check.selectedProperty().set(data.getSelect());
        text_edit.setText(FXUtils.deciToMoney(data.getEdit()));
        label_total.setText(FXUtils.deciToMoney(data.getTotal()));
        year = data.getYear();
        month = data.getMonth();
        check.setText(year+"年"+month+"月");
    }

    @Override
    public PredictItemObj get() {
        return new PredictItemObj(check.isSelected(),
                FXUtils.getDecimal(text_edit.getText()),
                FXUtils.getDecimal(label_total.getText()),
                year, month);
    }

    public static class PredictItemObj {
        private Boolean select = false;
        private BigDecimal edit = new BigDecimal(0);
        private BigDecimal total = new BigDecimal(0);
        private Long year = 2017L;
        private Long month = 6L;

        public PredictItemObj(Boolean select, BigDecimal edit, BigDecimal total, Long year, Long month) {
            this.select = select;
            this.edit = edit;
            this.total = total;
            this.year = year;
            this.month = month;
        }

        public Boolean getSelect() {
            return select;
        }

        public PredictItemObj setSelect(Boolean select) {
            this.select = select;
            return this;
        }

        public BigDecimal getEdit() {
            return edit;
        }

        public PredictItemObj setEdit(BigDecimal edit) {
            this.edit = edit;
            return this;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public PredictItemObj setTotal(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Long getYear() {
            return year;
        }

        public void setYear(Long year) {
            this.year = year;
        }

        public Long getMonth() {
            return month;
        }

        public void setMonth(Long month) {
            this.month = month;
        }
    }
}
