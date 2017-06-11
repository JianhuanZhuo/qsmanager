package cn.keepfight.qsmanager;

import cn.keepfight.qsmanager.controller.ContentController;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;

import java.io.IOException;

/**
 * 菜单列表
 * Created by tom on 2017/6/5.
 */
public enum MenuList {
    SUPPLY("供应商管理"),
    CUSTOM("客户管理"),
    PRODUCTS("产品管理"),
    INCOME("收入管理"),
    OUTCOME("支出管理"),
    ORDERS("订单管理"),
    SETTINGS("系统设置");

    ContentController controller;
    String view;
    String image;
    String english;
    String title;

    MenuList(String title) {
        view = name().toLowerCase() + ".fxml";
        image = "menu-" + name().toLowerCase() + ".png";
        english = name().toLowerCase() + " manage";
        this.title = title;
        Platform.runLater(() -> {
            try {
                controller = ViewPathUtil.loadViewForController(view);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public ContentController getController() {
        return controller;
    }

    public String getImage() {
        return image;
    }

    public String getEnglish() {
        return english;
    }

    public String getView() {
        return view;
    }

    public String getTitle() {
        return title;
    }
}
