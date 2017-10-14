package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.dao.OperatorDao;
import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.qsmanager.dao.UserDao;
import cn.keepfight.utils.DialogContent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * 新增客户信息控制器
 * Created by tom on 2017/6/7.
 */
public class StuffAddController implements DialogContent<StuffDao> {
    @FXML
    private VBox root;
    @FXML
    private TextField name;
    @FXML
    private TextField serial;
    @FXML
    private TextField acc;
    @FXML
    private TextField psw;

    @Override
    public void init() {
        name.setText("");
        serial.setText("");
        acc.setText("");
        psw.setText("");
    }

    @Override
    @Deprecated
    public void fill(StuffDao model) {
        throw new RuntimeException("不支持的方法");
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty resProperty = new SimpleBooleanProperty();
        resProperty.bind(name.textProperty().isNotEmpty()
                .and(serial.textProperty().isNotEmpty())
                .and(acc.textProperty().isNotEmpty())
                .and(psw.textProperty().isNotEmpty()));
        return resProperty;
    }

    @Override
    public StuffDao pack() {
        StuffDao stuff = new StuffDao();
        OperatorDao operator = new OperatorDao();
        UserDao user = new UserDao();
        user.setHalt(false);
        user.setNickname(name.getText());

        operator.setUserDao(user);
        operator.setAccount(acc.getText());
        operator.setPassword(psw.getText());
        operator.setAuthority("");

        stuff.setOperatorDao(operator);
        stuff.setName(name.getText());
        stuff.setSerial(serial.getText());

        return stuff;
    }
}
