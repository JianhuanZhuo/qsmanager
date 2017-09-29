package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.StuffWrapper;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.service.StuffServices;
import cn.keepfight.utils.*;
import cn.keepfight.widget.MenuListChecker;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static cn.keepfight.utils.FXUtils.limitLength;

/**
 * 员工菜单模块控制器
 * Created by tom on 2017/7/19.
 */
public class StuffController implements ContentCtrl, Initializable {
    @FXML
    private HBox root;
    @FXML
    private Button addStuff;
    @FXML
    private Button delStuff;
    @FXML
    private ListView<StuffDao> stuffList;

    @FXML
    private VBox infoPane;
    @FXML
    private Button saveInfo;
    @FXML
    private TextField info_serial;
    @FXML
    private TextField info_name;
    @FXML
    private TextField info_acc;
    @FXML
    private PasswordField info_psw;

    public TextField info_salary_basic;
    public TextField info_salary_annual;
    public CheckBox check_halt;

    @FXML
    private VBox menuSelect;
    private MenuListChecker menuListChecker = new MenuListChecker();

    private StuffAddController addController;

    private StuffWrapper currentStuff = new StuffWrapper();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initUI();
        initAction();
    }

    private void initUI() {
        stuffList.setCellFactory(list -> new ListCell<StuffDao>() {
            @Override
            protected void updateItem(StuffDao item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getSerial() + "-" + item.getName());
                } else {
                    setText(null);
                }
                setGraphic(null);
            }
        });

        limitLength(info_name, 60);
        limitLength(info_serial, 30);
        limitLength(info_acc, 60);
        limitLength(info_psw, 60);


        info_name.textProperty().bindBidirectional(currentStuff.nameProperty());
        info_serial.textProperty().bindBidirectional(currentStuff.serialProperty());
        info_acc.textProperty().bindBidirectional(currentStuff.getOperatorWrapper().accountProperty());
        info_psw.textProperty().bindBidirectional(currentStuff.getOperatorWrapper().passwordProperty());

        info_salary_basic.textProperty()
                .bindBidirectional(currentStuff.salary_basicProperty(), FXUtils.deciMoneyConverter("0"));
        info_salary_annual.textProperty()
                .bindBidirectional(currentStuff.salary_annualProperty(), FXUtils.deciMoneyConverter("0"));

        check_halt.selectedProperty().bindBidirectional(
                currentStuff.getOperatorWrapper().getUserWrapper().haltProperty()
        );

        menuSelect.getChildren().add(menuListChecker);
//        menuListChecker.getDataProperty().bindBidirectional(
//                currentStuff.getOperatorWrapper().authorityPr1operty());
    }

    private void initAction() {
        // 新增客户按钮
        addStuff.setOnMouseClicked(e -> QSUtil.add(() -> addController,
                (model -> {
                    StuffServices.insert(model);
                    loadStuff();
                })));

        // 删除客户按钮
        delStuff.setOnMouseClicked(event -> QSUtil.del(() -> stuffList.getSelectionModel(),
                (model) -> {
                    StuffServices.delete(model.getId());
                    loadStuff();
                }));

        // 点击左侧切换信息
        stuffList.getSelectionModel().selectedItemProperty().addListener(item -> {
            StuffDao stuff = stuffList.getSelectionModel().getSelectedItem();
            if (stuff == null) {
                return;
            }
            currentStuff.wrap(stuff);
        });

        infoPane.disableProperty().bind(stuffList.getSelectionModel().selectedItemProperty().isNull());
        delStuff.disableProperty().bind(stuffList.getSelectionModel().selectedItemProperty().isNull());
        saveInfo.disableProperty().bind(stuffList.getSelectionModel().selectedItemProperty().isNull());
        // 保存更新信息
        saveInfo.setOnMouseClicked(event -> {
            try {
                StuffServices.update(currentStuff.get());
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("更新错误，请保证网络通畅再重试！");
            }
            loadStuff();
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        Platform.runLater(() -> {
            try {
                addController = ViewPathUtil.loadViewForController("stuff_add.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showed(Properties params) {
        loadStuff();
        System.out.println("suff show");
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("员工管理");
    }

    /**
     * 加载全部员工信息
     */
    private void loadStuff() {
        Platform.runLater(() -> {
            try {
                stuffList.getItems().setAll(FXCollections.observableList(StuffServices.selectAll()));
                stuffList.getItems().forEach(x -> {
                    System.out.println(x.getOperatorDao().getLast_login_stamp());
                });
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("加载员工信息错误，请保证网络通畅再重试！");
            }
        });
    }
}
