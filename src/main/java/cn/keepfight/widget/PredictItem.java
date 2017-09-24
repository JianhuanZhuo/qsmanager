package cn.keepfight.widget;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 预算表中的小组件
 * Created by 卓建欢 on 2017/9/23.
 */
public class PredictItem implements Initializable, Widget{
    public VBox root;
    public Label label_total;
    public TextField label_edit;
    public Label label_date;

    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> edit = new SimpleObjectProperty<>(new BigDecimal(0));

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label_date.setText("11");
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
