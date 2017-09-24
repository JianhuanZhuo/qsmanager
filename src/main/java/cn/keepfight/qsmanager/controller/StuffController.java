package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
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
public class StuffController implements ContentCtrl, Initializable{
    @FXML
    private HBox root;
    @FXML
    private Button addStuff;
    @FXML
    private Button delStuff;
    @FXML
    private ListView<CustomModel> stuffList;

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

    @FXML
//    private ListView<Pair<MenuList, Boolean>> menuSelect;
    private VBox menuSelect;

    @FXML
    private  TextField info_serial1;
    @FXML
    private  TextField info_serial11;
    @FXML
    private  CheckBox check_fired;

    private StuffAddController addController;

    private CustomModel currentModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initUI();
        initAction();
    }

    private void initUI() {
        stuffList.setCellFactory(list -> new ListCell<CustomModel>() {
            @Override
            protected void updateItem(CustomModel item, boolean empty) {
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

        menuSelect.setSpacing(5);
        Arrays.stream(MenuList.values())
                .map(MenuList::getTitle)
                .forEach(s->menuSelect.getChildren().add(new CheckBox(s)));
    }

    private void initAction() {
        // 新增客户按钮
        addStuff.setOnMouseClicked(e -> QSUtil.add(() -> addController,
                (model -> {
                    QSApp.service.getStuffService().insert(model);
                    loadStuff();
                })));

        // 删除客户按钮
        delStuff.setOnMouseClicked(event -> QSUtil.del(()->stuffList.getSelectionModel(),
                (model)->{
                    QSApp.service.getStuffService().delete(model);
                    loadStuff();
                }));

        // 点击左侧切换信息
        stuffList.getSelectionModel().selectedItemProperty().addListener(item -> {
            CustomModel custom = stuffList.getSelectionModel().getSelectedItem();
            if (custom == null) {
                return;
            }

            // 保存
            currentModel = custom;

            info_name.setText(custom.getName());
            info_serial.setText(custom.getSerial());
            info_acc.setText(custom.getAcc());
            info_psw.setText(custom.getPsw());

            selectMenu(custom);
        });

        infoPane.disableProperty().bind(stuffList.getSelectionModel().selectedItemProperty().isNull());
        delStuff.disableProperty().bind(stuffList.getSelectionModel().selectedItemProperty().isNull());

        // 保存更新信息
        saveInfo.setOnMouseClicked(event -> {
            if (currentModel == null) {
                return;
            }
            CustomModel custom = new CustomModel();

            custom.setId(currentModel.getId());
            custom.setUtype(currentModel.getUtype());

            custom.setName(info_name.getText());
            custom.setSerial(info_serial.getText());

            custom.setAcc(info_acc.getText());
            custom.setPsw(info_psw.getText());

            custom.setNamefull(getMenuSelectList());

            try {
                QSApp.service.getStuffService().update(custom);
                loadStuff();
            } catch (Exception e) {
                WarningBuilder.build("更新错误，请保证网络通畅再重试！");
            }
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadStuff();
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

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("员工管理");
    }

    /**
     * 加载全部员工信息
     */
    private void loadStuff(){
        Platform.runLater(() -> {
            try {
                stuffList.getItems().setAll(FXCollections.observableList(QSApp.service.getStuffService().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void selectMenu(CustomModel c){
        List<String> titles = FXUtils.split(c.getNamefull(), "~", s -> MenuList.valueOf(s).getTitle());
        for (Node node : menuSelect.getChildren()){
            CheckBox checkBox = (CheckBox) node;
            if (titles.contains(checkBox.getText())){
                checkBox.setSelected(true);
            }else {
                checkBox.setSelected(false);
            }
        }
    }


    private String getMenuSelectList(){
        return menuSelect.getChildren().stream()
                .filter(n->((CheckBox)n).isSelected())
                .map(n->MenuList.getByTittle(((CheckBox)n).getText()))
                .map(MenuList::getName).collect(Collectors.joining("~"));
    }
}
