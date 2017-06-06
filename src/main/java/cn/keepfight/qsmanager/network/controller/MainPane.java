package cn.keepfight.qsmanager.network.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.model.User;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    private List<MenuList> legalList;

    @FXML
    public void initialize() {
        exit.setOnMouseClicked((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("确定退出系统吗？");
            alert.setContentText("如果当前正在编辑且未保存，则会丢失这些数据！");

            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(e -> {
                if (e.equals(ButtonType.OK)) {
                    try {
                        logout();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        });
        user.setOnMouseClicked(event -> {
        });
    }

    /**
     * 加载控制器所指定的视图到主界面中部
     *
     * @param controller 所指定控制器接口
     */
    public void loadCenter(ContentController controller) {
        Node node = controller.getRoot();
        System.out.println(node.getClass());
        centerScp.setContent(node);
    }

    /**
     * 转至指定菜单内容
     *
     * @param menu 指定菜单枚举
     */
    public void changeTo(MenuList menu) {
        if (menu.equals(state)) {
            return;
        }
        loadCenter(menu.getController());
        state = menu;
    }

    public void login(User user) throws IOException {

        //加载用户信息

        //加载菜单
        loadMenu(user);
        MenuListController listController = (MenuListController) ViewPathUtil.loadViewForController("menu.fxml");
        listController.loadMenuList(legalList);
        Platform.runLater(() -> menuScrollPane.setContent(listController.root));

        //可见
        FXUtils.delStyle("hide", action, menuScrollPane);

        changeTo(MenuList.CUSTOM);
    }

    /**
     * 检出操作，为初次进入系统的默认操作
     */
    public void logout() throws IOException {

        //提供系统登录界面
        LoginPane loginPane = (LoginPane) ViewPathUtil.loadViewForController("login.fxml");
        Platform.runLater(() -> centerScp.setContent(loginPane.getRoot()));

        //隐藏
        FXUtils.addStyle("hide", action, menuScrollPane);
    }

    private void loadMenu(User user) {
        switch (user.getUtype()) {
            case 0:
                legalList = Arrays.asList(
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
                legalList = Collections.singletonList(
                        MenuList.SETTINGS);
                break;
            default:
                legalList = new ArrayList<>();
                break;
        }
    }
}
