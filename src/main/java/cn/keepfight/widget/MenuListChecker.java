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
 * 菜单挂件
 * Created by tom on 2017/9/27.
 */
public class MenuListChecker extends VBox implements Widget<String> {

    private ObjectProperty<String> menuListStatus = new SimpleObjectProperty<>();
    private Map<String, CheckBox> toCheckBox = new HashMap<>(10);
    private AtomicBoolean transformFlag = new AtomicBoolean(false);

    public MenuListChecker() {
        setSpacing(5);
        Arrays.stream(MenuList.values())
                .forEach(m -> {
                    CheckBox c = new CheckBox(m.getTitle());
                    toCheckBox.put(m.getName(), c);
                    getChildren().add(c);
                    c.selectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (!transformFlag.get()) {
                            Set<MenuList> menus = toCheckBox.values().stream()
                                    .filter(CheckBox::isSelected)
                                    .map(n -> MenuList.getByTittle(n.getText()))
                                    .collect(Collectors.toSet());
                            if (newValue){
                                menus.add(m);
                            }else {
                                menus.remove(m);
                            }

                            String status = menus.stream()
                                    .map(MenuList::getName)
                                    .collect(Collectors.joining("~"));

//                            System.out.println("status:" + status);
                            menuListStatus.set(status);
                        }
                    });
                });

        // 监听
        menuListStatus.addListener((observable, oldValue, newValue) -> {
            if (transformFlag.compareAndSet(false, true)) {
//                System.out.println("listener:" + newValue);
                for (CheckBox c : toCheckBox.values()) {
                    c.setSelected(false);
                }
                FXUtils.split(newValue, "~", s -> MenuList.valueOf(s).getName())
                        .stream()
                        .map(toCheckBox::get)
                        .filter(Objects::nonNull)
                        .forEach(x -> x.setSelected(true));

                transformFlag.set(false);
            }
        });
    }

    @Override
    public Node getRoot() {
        return this;
    }

    public String get() {
        return toCheckBox.values().stream()
                .filter(CheckBox::isSelected)
                .map(n -> MenuList.getByTittle(n.getText()))
                .map(MenuList::getName)
                .collect(Collectors.joining("~"));
    }


    private List<MenuList> getMenuList(String status) {
        return FXUtils.split(status, "~", MenuList::valueOf)
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }


    public void set(String status) {
        for (CheckBox c : toCheckBox.values()) {
            c.setSelected(false);
        }
        FXUtils.split(status, "~", s -> MenuList.valueOf(s).getTitle())
                .stream()
                .map(toCheckBox::get)
                .filter(Objects::nonNull)
                .forEach(x -> x.setSelected(true));
    }

    @Override
    public ObjectProperty<String> getDataProperty() {
        return menuListStatus;
    }
}
