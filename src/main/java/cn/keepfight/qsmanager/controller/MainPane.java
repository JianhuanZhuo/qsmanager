package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ImageLoadUtil;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    @FXML
    private VBox titlePane;
    @FXML
    private HBox titleLine;
    @FXML
    private Button returnBtn;
    @FXML
    private Label title;
    @FXML
    private HBox btnList;

    private CustomModel userModel;

    private LoginController loginController;
    private MenuListController listController;

    private List<MenuList> legalList;
    private Map<ContentCtrl, Boolean> isLoaded = new HashMap<>();

    private ListProperty<Pair<ContentCtrl, Properties>> contentStack = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

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

        returnBtn.disableProperty().bind(contentStack.sizeProperty().lessThanOrEqualTo(1));
        returnBtn.setOnAction(event -> backNav());
    }

    /**
     * 切换菜单，这是一个重置页面栈的行为
     *
     * @param menu 需要切换到的菜单
     */
    public void switchMenu(MenuList menu) {
        while (!contentStack.isEmpty()) {
            if (!peek().getKey().hide()) {
                return;
            } else {
                pop();
            }
        }
        changeTo(menu.getController(), menu.getPs());
    }

    /**
     * 转至指定界面
     *
     * @param mainPane 指定界面
     */
    public void changeTo(MainPaneList mainPane) {
        changeTo(mainPane.getController(), new Properties());
    }

    /**
     * 转至指定界面
     *
     * @param mainPane 指定界面
     * @param params   界面参数
     */
    public void changeTo(MainPaneList mainPane, Properties params) {
        changeTo(mainPane.getController(), params);
    }

    /**
     * 转至指定界面
     *
     * @param controller 指定界面控制器
     */
    public void changeTo(ContentCtrl controller) {
        changeTo(controller, new Properties());
    }

    /**
     * 转至指定界面
     *
     * @param controller 指定界面控制器
     * @param params     界面参数
     */
    public void changeTo(ContentCtrl controller, Properties params) {

        if (controller == null) {
            // 如果为 NULL，这提示错误
            changeTo(MainPaneList.LOADING_ERROR);
            return;
        }

        Platform.runLater(() -> centerScp.setContent(MainPaneList.LOADING.getController().getRoot()));

        Platform.runLater(() -> {
            title.textProperty().bind(controller.getTitle());
            btnList.getChildren().clear();

            // 考虑到一次切换界面的刷新意义不大
            if (!isLoaded.getOrDefault(controller, false)) {
                Platform.runLater(controller::loaded);
                isLoaded.put(controller, true);
            }

            List<Button> btns;
            try {
                btns = controller.getBarBtns(params).stream()
                        .map(this::addTileBtn)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                System.err.println("controller.getBarBtns() error!");
                e.printStackTrace();
                btns = new ArrayList<>(1);
            }
            if (btns.size() != 0) {
                btnList.getChildren().setAll(btns);
                btnList.getChildren().add(new Separator(Orientation.VERTICAL));
            }
            // 添加刷新按钮
            btnList.getChildren().add(addTileBtn(new BarBtn() {
                @Override
                public String getText() {
                    return "刷新";
                }

                @Override
                public String getHit() {
                    return "点击刷新该页面";
                }

                @Override
                public String getImage() {
                    return "reset.png";
                }

                @Override
                public Runnable getAction() {
                    return () -> refresh();
                }
            }));
            titleVisible(!controller.transparentBackground());

            controller.showed(params);
            // 加载至主界面
            centerScp.setContent(controller.getRoot());
            // 添加到队列中
            push(controller, params);
            controller.showedAfter(params);
        });
    }

    /**
     * 替换当前页面，即先将当期页面弹出后再加载一个页面，前一个页面不会被保留 <hr/>
     * 一般用于刷新当前页面，比如修改参数后的下一个页面、上一个页面
     *
     * @param mainPane 用来替换当前页面的页面
     * @param params   页面参数
     */
    public void replace(MainPaneList mainPane, Properties params) {
        pop();
        changeTo(mainPane.getController(), params);
    }

    /**
     * 使用指定参数刷新当前页面
     *
     * @param params 页面参数
     */
    public void reload(Properties params) {
        changeTo(pop().getKey(), params);
    }

    /**
     * 使用相同参数刷新当前页面
     */
    public void refresh() {
        Pair<ContentCtrl, Properties> p = pop();
        changeTo(p.getKey(), p.getValue());
    }

    /**
     * 返回上个界面
     */
    public void backNav() {
        // 无上层的页面
        if (contentStack.size() <= 1) {
            return;
        }
        if (!peek().getKey().hide()) {
            return;
        } else {
            pop();
        }
        Pair<ContentCtrl, Properties> pair = pop();
        changeTo(pair.getKey(), pair.getValue());
    }

    public void login(CustomModel userModel) throws IOException {

        //加载用户信息
        user.setText(userModel.getName());

        //加载菜单
        loadMenu(userModel);
        listController.loadMenuList(legalList);
        Platform.runLater(() -> menuScrollPane.setContent(listController.root));

        //可见
        FXUtils.delStyle("hide", action, menuScrollPane);

        centerScp.setContent(null);
//        changeTo(legalList.get(0));

        this.userModel = userModel;
    }

    /**
     * 检出操作，为初次进入系统的默认操作
     */
    public void logout() {
        // 清空页面栈并提供系统登录界面
        contentStack.clear();
        changeTo(loginController);

        //隐藏
        FXUtils.addStyle("hide", action, menuScrollPane);

        listController.unloadMenuList();
        loginController.loginOut();
    }

    private void loadMenu(CustomModel userModel) {
        switch (userModel.getUtype().intValue()) {
            case 0:
                legalList = Arrays.asList(
                        MenuList.SHOP,
                        MenuList.CUSTOM,
                        MenuList.SUPPLY,
                        MenuList.STUFF,
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
                        MenuList.STUFF,
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
            case 3:
                legalList = FXUtils.split(userModel.getNamefull(), "~", MenuList::valueOf);
                break;
            default:
                legalList = new ArrayList<>();
                break;
        }
    }

    private void titleVisible(boolean v) {
        if (v) {
            FXUtils.delStyle("transparent-title", titlePane);
            titleLine.setManaged(true);
        } else {
            FXUtils.addStyle("transparent-title", titlePane);
            titleLine.setManaged(false);
        }
    }

    private void push(ContentCtrl c, Properties ps) {
        contentStack.add(new Pair<>(c, ps));
    }

    private Pair<ContentCtrl, Properties> pop() {
        return contentStack.remove(contentStack.size() - 1);
    }

    private Pair<ContentCtrl, Properties> peek() {
        return contentStack.get(contentStack.size() - 1);
    }

    public CustomModel getUserModel() {
        return userModel;
    }


    /**
     * 转换一个 bar 对象为一个按钮实例
     */
    private Button addTileBtn(BarBtn bar) {
        Button btn = new Button(bar.getText());
        String img = bar.getImage();
        if (img != null && !"".equals(img)) {
            ImageView m = ImageLoadUtil.bindImage(new ImageView(), bar.getImage());
            m.setFitWidth(18);
            m.setFitHeight(18);
            btn.setGraphic(m);
        }
        String hit = bar.getHit();
        if (hit != null) {
            btn.setTooltip(new Tooltip(hit));
        }
        Runnable r = bar.getAction();
        if (r != null) {
            btn.setOnAction(event -> r.run());
        }
        return btn;
    }
}
