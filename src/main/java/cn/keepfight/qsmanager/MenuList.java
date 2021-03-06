package cn.keepfight.qsmanager;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.OrderPaneController;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Properties;

/**
 * 菜单列表
 * Created by tom on 2017/6/5.
 */
public enum MenuList {
    SHOP("浏览下单"),
    SUPPLY("供应商管理"),
    CUSTOM("客户管理"),
    STUFF("员工管理"),
    PRODUCTS("产品管理"),
    INCOME("收入管理"),
    OUTCOME("支出管理"),
    ORDERS("订单管理"),
    SETTINGS("系统设置");

    ContentCtrl controller;
    String view;
    String image;
    String english;
    String title;
    Properties ps;

    MenuList(String title) {
        view = name().toLowerCase() + ".fxml";
        image = "menu-" + name().toLowerCase() + ".png";
        english = name().toLowerCase() + " manage";
        this.title = title;
        ps = new Properties();

        // 这是一场意外
        if (name().toLowerCase().equals("income")) {
            view = "income_manager.fxml";
        } else if (name().toLowerCase().equals("outcome")) {
            view = "outcome_manager.fxml";
        } else if (name().toLowerCase().equals("orders")) {
            view = "order_pane.fxml";
            ps.put("mode", OrderPaneController.USING_IN_ORDERS);
        }
    }

    private static boolean loaded = false;

    public static synchronized void loadMenuView() {
        if (loaded) return;
        loaded = true;
        for (MenuList m : MenuList.values()) {
            m.load();
        }
    }

    /**
     * 获得匹配项
     */
    public static MenuList getByTittle(String title) {
        for (MenuList m : values()) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }

    private void load() {
        new Thread(() -> controller = ViewPathUtil.loadViewForController(view)).start();
    }

    public ContentCtrl getController() {
        return controller;
    }

    public String getImage() {
        return image;
    }

    public String getEnglish() {
        return english;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name();
    }

    public Properties getPs() {
        return ps;
    }
}
