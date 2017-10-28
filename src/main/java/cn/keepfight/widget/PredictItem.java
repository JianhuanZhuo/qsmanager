package cn.keepfight.widget;

import cn.keepfight.utils.FXUtils;
import javafx.beans.property.BooleanProperty;
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

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set(new PredictItemObj(
                false,
                new BigDecimal(0),
                new BigDecimal(0)));
        btn_all.setOnAction(event -> text_edit.setText(label_total.getText()));
        content.disableProperty().bind(check.selectedProperty().not());
    }

    @Override
    public void set(PredictItemObj data) {
        check.selectedProperty().set(data.getSelect());
        text_edit.setText(FXUtils.deciToMoney(data.getEdit()));
        label_total.setText(FXUtils.deciToMoney(data.getTotal()));
    }

    @Override
    public PredictItemObj get() {

        return new PredictItemObj(check.isSelected(),
                FXUtils.getDecimal(text_edit.getText()),
                FXUtils.getDecimal(label_total.getText()));
    }

    public static class PredictItemObj {
        private Boolean select = false;
        private BigDecimal edit = new BigDecimal(0);
        private BigDecimal total = new BigDecimal(0);

        public PredictItemObj(Boolean select, BigDecimal edit, BigDecimal total) {
            this.select = select;
            this.edit = edit;
            this.total = total;
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
    }
}
