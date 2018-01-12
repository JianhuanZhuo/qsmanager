package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;

import java.io.IOException;

/**
 * 面板列表登记注册类
 * Created by tom on 2017/9/4.
 */
public enum MainPaneList {

    ORDER_PANE,
    INCOME_ANNUAL,
    RECEIPT_LIST,
    ORDER_EDIT,
    ORDER_MAKE,
    PRINT_MANAGER,
    OUTCOME,
    LOADING,
    LOADING_ERROR,
    PAY_EDIT,
    SALARY,
    SALARY_MONTH,
    SALARY_NEW,
    SALARY_EDIT,
    SALARY_PAY,
    SALARY_TEST,
    SALARY_CLEAR,
    PREDICT,
    PREDICT_LIST,
    PREDICT_ADD,
    OUTCOME_ANNUAL,
    annual$SUP_ADD_INVOICE,
    annual$SUP_ADD_REMIT,
    annual$SUP_ANNUAL_EDIT,
    analysis$SELL,
    analysis$CUSTOM,
    analysis$PRODUCTS,
    analysis$PIE,
    analysis$OUTCOME_STATIC,
    STATIC_TOTAL,
    STATIC_MATERIAL,
    STATIC_TOTAL_PAY,
    STATIC_TOTAL_TRADE,
    tax$TAX;

    String view;
    ContentCtrl controller;

    MainPaneList() {
        if (name().contains("$")) {
            view = name().replace("$", "/").toLowerCase() + ".fxml";
        } else {
            view = name().toLowerCase() + ".fxml";
        }
    }

    private static boolean loaded = false;

    public static synchronized void loadMenuView() {
        if (loaded) return;
        loaded = true;
        for (MainPaneList m : MainPaneList.values()) {
            m.load();
        }
    }

    private void load() {
//        new Thread(() -> {
//            try {
//                controller = ViewPathUtil.loadViewForController(view);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
        Platform.runLater(()->{
            try {
                controller = ViewPathUtil.loadViewForController(view);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 获得控制器，这个方法应该在 runLater 里执行
     *
     * @return 控制器实例
     */
    public ContentCtrl getController() {
        return controller;
    }
}
