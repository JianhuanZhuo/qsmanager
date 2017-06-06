package cn.keepfight.qsmanager.network.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 客户信息管理界面控制器类
 * Created by tom on 2017/6/5.
 */
public class Custom implements ContentController {

    public HBox root;
    public ImageView saveInfo;
    public TextField info_name;
    public TextField info_serial;
    public TextField info_phone;
    public TextField info_fax;
    public TextField info_pracc;
    public TextField info_pbacc;
    public TextField info_addr;
    public TextArea info_note;
    public Label info_attach;
    public TextField info_acc;
    public PasswordField info_psw;
    public Button reset_psw;
    @FXML
    private ListView custList;
    @FXML
    private ImageView addCust;
    @FXML
    private ImageView delCust;

    @Override
    public Node getRoot() {
        return root;
    }
}
