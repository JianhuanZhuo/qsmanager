package cn.keepfight.widget;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * 月份拾取器
 * Created by tom on 2017/10/13.
 */
public class MonthPicker implements Initializable, PopupWidget<Pair<Long, Long>> {
    public HBox root;
    public Button year_uu;
    public Button year_u;
    public Button year_c;
    public Button year_d;
    public Button year_dd;
    public Button month_uu;
    public Button month_u;
    public Button month_c;
    public Button month_d;
    public Button month_dd;
    public VBox year_list;
    public VBox month_list;
    private Popup popup = new Popup();

    private Pair<Long, Long> data = new Pair<>(2017L, 7L);
    private Node locateNode;

    private Consumer<Pair<Long, Long>> onValueChange = null;
    private Consumer<Pair<Long, Long>> onClose = null;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setAutoFix(true);
        popup.getContent().add(root);
        paint();

        year_u.setOnAction(e -> {
            data = new Pair<>(data.getKey() + 1, data.getValue());
            paint();
        });
        year_uu.setOnAction(e -> {
            data = new Pair<>(data.getKey() + 2, data.getValue());
            paint();
        });
        year_dd.setOnAction(e -> {
            data = new Pair<>(data.getKey() - 2, data.getValue());
            paint();
        });
        year_d.setOnAction(e -> {
            data = new Pair<>(data.getKey() - 1, data.getValue());
            paint();
        });

        month_u.setOnAction(e -> {
            data = new Pair<>(data.getKey(), getMonth(1));
            paint();
        });
        month_uu.setOnAction(e -> {
            data = new Pair<>(data.getKey(), getMonth(2));
            paint();
        });
        month_d.setOnAction(e -> {
            data = new Pair<>(data.getKey(), getMonth(-1));
            paint();
        });
        month_dd.setOnAction(e -> {
            data = new Pair<>(data.getKey(), getMonth(-2));
            paint();
        });

        year_list.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                data = new Pair<>(data.getKey() + 1, data.getValue());
                paint();
            } else {
                data = new Pair<>(data.getKey() - 1, data.getValue());
                paint();
            }
        });

        month_list.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                data = new Pair<>(data.getKey(), getMonth(1));
                paint();
            } else {
                data = new Pair<>(data.getKey(), getMonth(-1));
                paint();
            }
        });

        // 关闭事件
        popup.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && onClose!=null){
                onClose.accept(data);
            }
        });
    }

    @Override
    public void show() {
        popup.show(locateNode,
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinX(),
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinY());
    }

    @Override
    public void setOnClose(Consumer<Pair<Long, Long>> onClose) {
        this.onClose = onClose;
    }

    @Override
    public void setOnValueChange(Consumer<Pair<Long, Long>> onValueChange) {
        this.onValueChange = onValueChange;
    }

    @Override
    public void locateNode(Node node) {
        locateNode = node;
    }

    private long getMonth(long offset) {
        long month = data.getValue();
        return (month + 11 + offset) % 12 + 1;
    }

    private void paint() {
        long year = data.getKey();
        year_uu.setText("" + (year + 2));
        year_u.setText("" + (year + 1));
        year_c.setText("" + (year));
        year_d.setText("" + (year - 1));
        year_dd.setText("" + (year - 2));
        long month = data.getValue();
        month_uu.setText("" + (getMonth(+2)));
        month_u.setText("" + (getMonth(1)));
        month_c.setText("" + (month));
        month_d.setText("" + (getMonth(-1)));
        month_dd.setText("" + (getMonth(-2)));

        if (onValueChange!=null){
            onValueChange.accept(data);
        }
    }

    @Override
    public void set(Pair<Long, Long> data) {
        this.data = data;
        paint();
    }
}
