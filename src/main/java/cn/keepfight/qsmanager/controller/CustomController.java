package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.Optional;

import static cn.keepfight.utils.FXUtils.limitLength;

/**
 * 客户信息管理界面控制器类
 * Created by tom on 2017/6/5.
 */
public class CustomController implements ContentController {

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
    private Label info_attach;

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
//    @FXML private  Button reset_psw;

    private CustomModel currentModel;

    // 子界面
    private CustomAddController addController;


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadCustom();
        Platform.runLater(() -> {
            try {
                addController = ViewPathUtil.loadViewForController("custom_add.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showed() {

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
    }

    /**
     * 初始化交互响应
     */
    private void initAction() throws Exception {
        // 新增客户按钮
        addCust.setOnMouseClicked(e -> {
            Optional<CustomModel> op = CustomDialog.gen().build(addController);
            op.ifPresent(model -> {
                try {
                    QSApp.service.getCustomService().insert(model);
                    loadCustom();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("新增客户失败", "新增客户失败，请检查网络是否通畅，客户的账号是否已存在！");
                }
            });
        });

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
        });
        ListProperty<Integer> lp =
                new SimpleListProperty(custList.getSelectionModel().getSelectedIndices());
        infoPane.disableProperty().bind(lp.emptyProperty());
        delCust.disableProperty().bind(lp.emptyProperty());

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

}
