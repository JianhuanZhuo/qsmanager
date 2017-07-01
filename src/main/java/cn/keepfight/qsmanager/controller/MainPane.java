package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.utils.FXReflectUtils;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.*;

/**
 * 主界面面板控制器
 * Created by tom on 2017/6/5.
 */
public class MainPane {

    Long x;
    @FXML
    private ScrollPane menuScrollPane;
    @FXML
    private ScrollPane centerScp;
    @FXML
    private Label user;
    @FXML
    private Label exit;
    @FXML
    private HBox action;

    private MenuList state = MenuList.SETTINGS;

    private LoginController loginController;
    private MenuListController listController;

    private List<MenuList> legalList;
    private Map<ContentController, Boolean> isLoaded = new HashMap<>();

    @FXML
    public void initialize() throws IOException {

        // 加载子界面
        listController = ViewPathUtil.loadViewForController("menu.fxml");
        loginController = ViewPathUtil.loadViewForController("login.fxml");

        // 退出系统警告
        exit.setOnMouseClicked((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("确定退出系统吗？");
            alert.setContentText("如果当前正在编辑且未保存，则会丢失这些数据！");

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(e -> {
                if (e.equals(ButtonType.OK)) {
                    logout();
                }
            });
        });
        user.setOnMouseClicked(event -> {
        });


    }

    /**
     * 转至指定菜单内容
     *
     * @param menu 指定菜单枚举
     */
    public void changeTo(MenuList menu) {
        if (state.equals(menu)) {
            return;
        }
        // 加载至主界面
        ContentController controller = menu.getController();
        centerScp.setContent(controller.getRoot());

        // 考虑到一次切换界面的刷新意义不大
        if (!isLoaded.getOrDefault(controller, false)){
            Platform.runLater(controller::loaded);
            isLoaded.put(controller, true);
        }
        controller.showed();
        state = menu;
    }

    public void login(CustomModel userModel) throws IOException {

        //加载用户信息

        //加载菜单
        loadMenu(userModel);
        listController.loadMenuList(legalList);
        Platform.runLater(() -> menuScrollPane.setContent(listController.root));

        //可见
        FXUtils.delStyle("hide", action, menuScrollPane);

        centerScp.setContent(null);
//        changeTo(legalList.get(0));
    }

    /**
     * 检出操作，为初次进入系统的默认操作
     */
    public void logout() {
        //提供系统登录界面

        Platform.runLater(() -> centerScp.setContent(loginController.getRoot()));

        //隐藏
        FXUtils.addStyle("hide", action, menuScrollPane);

        listController.unloadMenuList();
        loginController.loginOut();

        state = MenuList.SETTINGS;
    }

    private void loadMenu(CustomModel userModel) {
        switch (userModel.getUtype().intValue()) {
            case 0:
                legalList = Arrays.asList(
                        MenuList.SHOP,
                        MenuList.CUSTOM,
                        MenuList.SUPPLY,
                        MenuList.PRODUCTS,
                        MenuList.INCOME,
                        MenuList.OUTCOME,
                        MenuList.ORDERS,
                        MenuList.SETTINGS);
                break;
            case 1:
                legalList = Arrays.asList(
                        MenuList.CUSTOM,
                        MenuList.SUPPLY,
                        MenuList.PRODUCTS,
                        MenuList.INCOME,
                        MenuList.OUTCOME,
                        MenuList.ORDERS,
                        MenuList.SETTINGS);
                break;
            case 2:
                legalList = Arrays.asList(
                        MenuList.SHOP,
                        MenuList.SETTINGS);
                break;
            default:
                legalList = new ArrayList<>();
                break;
        }
    }
}
