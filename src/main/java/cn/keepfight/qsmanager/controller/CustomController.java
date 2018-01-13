package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.QSUtil;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import cn.keepfight.widget.ImageManager;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static cn.keepfight.utils.FXUtils.limitLength;

/**
 * 客户信息管理界面控制器类
 * Created by tom on 2017/6/5.
 */
public class CustomController implements ContentCtrl {

    @FXML
    private VBox infoPane;
    @FXML
    private HBox root;
    @FXML
    private Button saveInfo;
    @FXML
    private TextField info_name;
    @FXML
    private TextField info_serial;
    @FXML
    private TextField info_phone;
    @FXML
    private TextField info_fax;
    @FXML
    private TextField info_accpv;
    @FXML
    private TextField info_accpb;
    @FXML
    private TextField info_bccpv;
    @FXML
    private TextField info_bccpb;
    @FXML
    private TextField info_addr;
    @FXML
    private TextField info_namefull;
    @FXML
    private TextArea info_note;
    @FXML
    private Button info_attach;

    // 用户列表
    @FXML
    private ListView<CustomModel> custList;
    @FXML
    private Button addCust;
    @FXML
    private Button delCust;

    // 账户密码信息
    @FXML
    private TextField info_acc;
    @FXML
    private PasswordField info_psw;

    @FXML
    private Button addMat;
    @FXML
    private Button delMat;
    @FXML
    private TableView<ProductModel> matTable;
    @FXML
    private TableColumn<ProductModel, String> mat_spec;
    @FXML
    private TableColumn<ProductModel, String> mat_name;

    private CustomModel currentModel;

    // 子界面
    private CustomAddController addController;
    private OrderFavorAddController addOrderFavor;


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadCustom();
        Platform.runLater(() -> {
            addController = ViewPathUtil.loadViewForController("custom_add.fxml");
            addOrderFavor = ViewPathUtil.loadViewForController("order_favor_add.fxml");
        });
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("客户管理");
    }

    @FXML
    public void initialize() throws Exception {
        initUI();
        initAction();
    }

    private void initUI() throws Exception {

        custList.setCellFactory(list -> new ListCell<CustomModel>() {
            @Override
            protected void updateItem(CustomModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getSerial() + "-" + item.getName());
                } else {
                    setText("");
                }
                setGraphic(null);
            }
        });
        //长度限制
        limitLength(info_name, 60);
        limitLength(info_serial, 30);
        limitLength(info_phone, 60);
        limitLength(info_fax, 60);
        limitLength(info_accpv, 60);
        limitLength(info_accpb, 60);
        limitLength(info_bccpv, 120);
        limitLength(info_bccpb, 120);
        limitLength(info_addr, 120);
        limitLength(info_namefull, 120);
        limitLength(info_note, 250);

        mat_name.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getSerial() + "-" + cell.getValue().getName()));
        mat_spec.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDetail()));
    }

    /**
     * 初始化交互响应
     */
    private void initAction() throws Exception {
        // 新增客户按钮
        addCust.setOnMouseClicked(e -> QSUtil.add(() -> addController,
                (model -> {
                    QSApp.service.getCustomService().insert(model);
                    loadCustom();
                })));

        // 删除客户按钮
        delCust.setOnMouseClicked(event -> {
            CustomModel custom = custList.getSelectionModel().getSelectedItem();
            if (custom == null) {
                WarningBuilder.build("删除客户失败", "请先选中指定客户后再进行删除！");
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("是否要删除这个客户？");
                alert.setContentText("系统为了账目安全，你必须先删除和该客户相关的交易记录！");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        QSApp.service.getCustomService().delete(custom);
                        loadCustom();
                    } catch (Exception e) {
                        WarningBuilder.build("删除客户失败", "请先清空该客户相关的交易记录，再进行删除！");
                    }
                }
            }
        });

        // 点击左侧切换信息
        custList.getSelectionModel().selectedItemProperty().addListener(item -> {
            CustomModel custom = custList.getSelectionModel().getSelectedItem();
            if (custom == null) {
                return;
            }

            // 保存
            currentModel = custom;

            info_name.setText(custom.getName());
            info_serial.setText(custom.getSerial());
            info_phone.setText(custom.getPhone());
            info_fax.setText(custom.getFax());
            info_accpv.setText(custom.getAccpv());
            info_accpb.setText(custom.getAccpb());
            info_addr.setText(custom.getAddr());
            info_bccpb.setText(custom.getBccpb());
            info_bccpv.setText(custom.getBccpv());
            info_namefull.setText(custom.getNamefull());
            info_note.setText(custom.getNote());

            info_acc.setText(custom.getAcc());
            info_psw.setText(custom.getPsw());

            loadOrderFavor();
        });

        BooleanBinding lp = custList.getSelectionModel().selectedItemProperty().isNull();
        infoPane.disableProperty().bind(lp);
        delCust.disableProperty().bind(lp);

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
            custom.setPhone(info_phone.getText());
            custom.setFax(info_fax.getText());
            custom.setAccpv(info_accpv.getText());
            custom.setAccpb(info_accpb.getText());
            custom.setBccpb(info_bccpb.getText());
            custom.setBccpv(info_bccpv.getText());
            custom.setAddr(info_addr.getText());
            custom.setNamefull(info_namefull.getText());
            custom.setNote(info_note.getText());

            custom.setAcc(info_acc.getText());
            custom.setPsw(info_psw.getText());

            try {
                QSApp.service.getCustomService().update(custom);
                loadCustom();
            } catch (Exception e) {
                WarningBuilder.build("更新错误，请保证网络通畅再重试！");
            }
        });

        addMat.setOnAction(event -> QSUtil.add(
                () -> addOrderFavor,
                (model) -> {
                    QSApp.service.getOrderFavorService().insert(currentModel.getId(), model.getId());
                    loadOrderFavor();
                },
                () -> currentModel != null
        ));

        delMat.setOnAction(event ->
                QSUtil.del(() -> matTable.getSelectionModel(), (model) -> {
                    QSApp.service.getOrderFavorService().delete(currentModel.getId(), model.getId());
                    loadOrderFavor();
                }));


        info_attach.disableProperty().bind(custList.getSelectionModel().selectedItemProperty().isNull());
        info_attach.setOnAction(event -> {
            CustomModel selected = custList.getSelectionModel().getSelectedItem();
            String category = "Custom-" + selected.getId() + "-" + selected.getSerial();
            String title = "图片管理器：" + selected.getSerial() + "-" + selected.getName() + "-附件管理";
            try {
                ImageManager.newManager(category, title);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    // 加载客户列表
    private void loadCustom() {
        Platform.runLater(() -> {
            try {
                custList.getItems().setAll(FXCollections.observableList(QSApp.service.getCustomService().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    // 加载原料列表
    private void loadOrderFavor() {
        if (currentModel == null) {
            return;
        }
        Long id = currentModel.getId();
        Platform.runLater(() -> {
            try {
                matTable.getItems().setAll(FXCollections.observableList(QSApp.service.getOrderFavorService().selectAll(id)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
