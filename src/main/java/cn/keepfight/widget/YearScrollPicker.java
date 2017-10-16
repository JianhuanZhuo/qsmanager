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
public class YearScrollPicker implements Initializable, PopupWidget<Long> {
    public HBox root;
    public Button year_uu;
    public Button year_u;
    public Button year_c;
    public Button year_d;
    public Button year_dd;
    public VBox year_list;
    private Popup popup = new Popup();

    private Long data = 2017L;
    private Node locateNode;

    private Consumer<Long> onValueChange = null;
    private Consumer<Long> onClose = null;

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


        Button[] btns = {year_dd, year_d, year_c, year_u, year_uu};
        for (int i = 0; i < btns.length; i++) {
            final int j = i;
            btns[i].setOnAction(e -> {
                data = data+j-2;
                paint();
            });
        }

        year_list.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                data = data+1;
                paint();
            } else {
                data = data-1;
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
    public void set(Long data) {
        this.data = data;
        paint();
    }

    @Override
    public void show() {
        popup.show(locateNode,
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinX(),
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinY());
    }

    @Override
    public void setOnClose(Consumer<Long> onClose) {
        this.onClose = onClose;
    }

    @Override
    public void setOnValueChange(Consumer<Long> onValueChange) {
        this.onValueChange = onValueChange;
    }

    @Override
    public void locateNode(Node node) {
        locateNode = node;
    }


    private void paint() {
        long year = data;
        year_uu.setText("" + (year + 2));
        year_u.setText("" + (year + 1));
        year_c.setText("" + (year));
        year_d.setText("" + (year - 1));
        year_dd.setText("" + (year - 2));

        if (onValueChange!=null){
            onValueChange.accept(data);
        }
    }
}
