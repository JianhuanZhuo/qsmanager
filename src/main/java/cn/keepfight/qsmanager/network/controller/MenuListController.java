package cn.keepfight.qsmanager.network.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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
                try {
                    MenuController c = (MenuController) ViewPathUtil.loadViewForController("menuitem.fxml");
                    c.setup(m);
                    root.getChildren().add(c.getRoot());

                    c.getRoot().setOnMouseClicked(event -> {
                        QSApp.mainPane.changeTo(m);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
