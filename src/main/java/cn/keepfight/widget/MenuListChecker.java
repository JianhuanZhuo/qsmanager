package cn.keepfight.widget;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.utils.FXUtils;
import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by tom on 2017/9/27.
 */
public class MenuListChecker extends VBox implements Widget<String>{

    private ObjectProperty<String> menuListStatus = new SimpleObjectProperty<>();
    private Map<String, CheckBox> toCheckBox = new HashMap<>(10);
    private volatile boolean transformFlag = false;

    public MenuListChecker(){
        setSpacing(5);
        Arrays.stream(MenuList.values())
                .map(m->new Pair<>(m.getName(), m.getTitle()))
                .forEach(s -> {
                    CheckBox c = new CheckBox(s.getValue());
                    toCheckBox.put(s.getKey(), c);
                    getChildren().add(c);
                    c.selectedProperty().addListener(x-> {
//                        if (!transformFlag){
//                            transformFlag = true;、
                        String status = get();
                        System.out.println("status:"+status);
                            menuListStatus.set(status);
//                            transformFlag = false;
//                        }
                    });
                });

        // 监听
//        menuListStatus.addListener((observable, oldValue, newValue) -> {
//            if (!transformFlag){
//                transformFlag = true;
//                set(newValue);
//                transformFlag = false;
//            }
//        });
    }

    @Override
    public Node getRoot() {
        return this;
    }

    @Override
    public String get() {
        return toCheckBox.values().stream()
                .filter(CheckBox::isSelected)
                .map(n -> MenuList.getByTittle(n.getText()))
                .map(MenuList::getName)
                .collect(Collectors.joining("~"));
    }

    @Override
    public void set(String status){
        for (CheckBox c: toCheckBox.values()){
            c.setSelected(false);
        }
        FXUtils.split(status, "~", s -> MenuList.valueOf(s).getTitle())
                .stream()
                .map(toCheckBox::get)
                .filter(Objects::nonNull)
                .forEach(x->x.setSelected(true));
    }

    @Override
    public ObjectProperty<String> getDataProperty() {
        return menuListStatus;
    }
}
