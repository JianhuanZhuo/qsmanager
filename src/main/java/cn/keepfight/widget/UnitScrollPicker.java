package cn.keepfight.widget;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 月份拾取器
 * Created by tom on 2017/10/13.
 */
public class UnitScrollPicker<T> implements Initializable, PopupWidget<T> {
    public HBox root;
    public Button year_uu;
    public Button year_u;
    public Button year_c;
    public Button year_d;
    public Button year_dd;
    public VBox year_list;
    private Popup popup = new Popup();

    private int index = 0;
    private Node locateNode;

    private Consumer<T> onValueChange = null;
    private Consumer<T> onClose = null;
    private List<T> unitList = new ArrayList<>(20);
    private Function<T, String> stringConverter;

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

        Button[] btns = {year_uu, year_u, year_c, year_d, year_dd};
        for (int i = 0; i < btns.length; i++) {
            final int j = i;
            btns[i].setOnAction(e -> {
                index = index + j - 2;
                index = (index + unitList.size()) % unitList.size();
                paint();
                if (onValueChange != null) {
                    onValueChange.accept(unitList.get(index));
                }
            });
        }

        year_list.setOnScroll(event -> {
            index = index + (event.getDeltaY() < 0 ? 1 : -1);
            index = (index + unitList.size()) % unitList.size();
            paint();
            if (onValueChange != null) {
                onValueChange.accept(unitList.get(index));
            }
        });

        // 关闭事件
        popup.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && onClose != null) {
                onClose.accept(unitList.get(index));
            }
        });
    }

    @Override
    public void set(T data) {
        if (unitList.indexOf(data) != -1) {
            index = unitList.indexOf(data);
            paint();
            if (onValueChange != null) {
                onValueChange.accept(unitList.get(index));
            }
        }
    }

    @Override
    public T get() {
        return unitList.get(index);
    }

    public void setDataList(List<T> dataList, Function<T, String> stringConverter) {
        unitList.clear();
        unitList.addAll(dataList);
        this.stringConverter = stringConverter;
        paint();
    }

    @Override
    public void show() {
        popup.show(locateNode,
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinX(),
                locateNode.localToScreen(locateNode.getBoundsInLocal()).getMinY());
    }

    @Override
    public void setOnClose(Consumer<T> onClose) {
        this.onClose = onClose;
    }

    @Override
    public void setOnValueChange(Consumer<T> onValueChange) {
        this.onValueChange = onValueChange;
    }


    @Override
    public void locateNode(Node node) {
        locateNode = node;
    }

    private void paint() {
        if (unitList.size()==0){
            return;
        }
        year_uu.setText(stringConverter.apply(unitList.get((index + unitList.size() - 2) % unitList.size())));
        year_u.setText(stringConverter.apply(unitList.get((index + unitList.size() - 1) % unitList.size())));
        year_c.setText(stringConverter.apply(unitList.get((index + unitList.size()) % unitList.size())));
        year_d.setText(stringConverter.apply(unitList.get((index + unitList.size() + 1) % unitList.size())));
        year_dd.setText(stringConverter.apply(unitList.get((index + unitList.size() + 2) % unitList.size())));

        if (onValueChange != null) {
            onValueChange.accept(unitList.get(index));
        }
    }
}
