package cn.keepfight.qsmanager.network.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.User;
import cn.keepfight.utils.WaitDialog;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

/**
 * 系统登录面板
 * Created by tom on 2017/6/5.
 */
public class LoginPane implements ContentController {

    @FXML
    private VBox root;
    @FXML
    private TextField acc;

    @FXML
    private PasswordField psw;

    @FXML
    private Button login;

    @FXML
    private Label noacc;

    @FXML
    public void initialize() {
        noacc.setTooltip(new Tooltip("如无账号，请联系邓维女士获得登录账号与密码（联系方式：****）"));
        login.setOnMouseClicked((MouseEvent event) -> {
            Optional<Boolean> op = new WaitDialog<>(() -> {
                System.out.println(Thread.currentThread().getName() + " :" + System.currentTimeMillis());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " :" + System.currentTimeMillis());
                return true;
            }).justWait();
            op.ifPresent((v) -> {
                try {
                    QSApp.mainPane.login(new User(acc.getText(), psw.getText()));
                } catch (IOException e) {
                    //@TODO
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }
}
