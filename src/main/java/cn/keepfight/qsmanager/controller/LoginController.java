package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.service.LoginService;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * 系统登录面板
 * Created by tom on 2017/6/5.
 */
public class LoginController implements ContentCtrl {

    @FXML
    private ImageView xx;
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
        // 无账号提示
        noacc.setTooltip(new Tooltip("如无账号，请联系邓维女士获得登录账号与密码（联系方式：****）"));

        // 图片加载测试
        noacc.setOnMouseClicked(e->{
        });

        // 登录按钮
        login.setOnMouseClicked((MouseEvent event) -> loginAction());
        psw.setOnAction(event -> loginAction());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
    }

    @Override
    public void showed(Properties params) {
        Platform.runLater(()->psw.requestFocus());
    }

    @Override
    public StringBinding getTitle() {
        return new StringBinding() {
            @Override
            protected String computeValue() {
                return "登录界面";
            }
        };
    }

    @Override
    public boolean transparentBackground() {
        // 使用透明模式的页面
        return true;
    }

    void loginOut() {
        try {
            psw.setText("");
            String str = (String) ConfigUtil.load("fxapp.properties").get("login.acc");
            acc.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginAction() {
        // 有效检查
        if (acc.getText().trim().equals("") || psw.getText().trim().equals("")) {
            return;
        }

        // 登录
        CustomModel user = new CustomModel();
        user.setAcc(acc.getText());
        user.setPsw(psw.getText());

        Optional<CustomModel> op = new WaitDialog<>(() -> {
            try {
                return QSApp.service.getLoginService().login(user);
            } catch (LoginService.PswIllegalException e) {
                return new CustomModel();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).setDetail("系统登录中", "正使用您输入的账号和密码进行登录，请稍后...")
                .justWait();

        if (!op.isPresent()) {
            WarningBuilder.build("登录失败！");
        } else {
            CustomModel resUser = op.get();
            if (resUser.getAcc() == null) {
                WarningBuilder.build("登录失败！", "登录账号或密码错误，如果忘记账号或密码请联系管理员进行重置密码");
            } else {
                ConfigUtil.alter("fxapp.properties", "login.acc", resUser.getAcc());
                try {
                    QSApp.mainPane.login(resUser);
                } catch (IOException e) {
                    //@TODO
                    e.printStackTrace();
                }
            }
        }
    }
}
