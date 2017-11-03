package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * 菜单列表
 * Created by tom on 2017/6/6.
 */
public class MenuListController {

    @FXML
    public VBox root;

    public void loadMenuList(List<MenuList> legalList) {
        for (int i = 0; i < legalList.size(); i++) {
            MenuList m = legalList.get(i);
            //加载并添加到菜单列表中
            Platform.runLater(() -> {
                MenuController c = ViewPathUtil.loadViewForController("menuitem.fxml");
                c.setup(m);
                root.getChildren().add(c.getRoot());
                c.getRoot().setOnMouseClicked(event -> QSApp.mainPane.switchMenu(m));
            });
        }
    }

    public void unloadMenuList() {
        root.getChildren().clear();
    }
}
