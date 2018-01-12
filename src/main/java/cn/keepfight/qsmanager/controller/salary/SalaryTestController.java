package cn.keepfight.qsmanager.controller.salary;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 发现自己并没有任何的艺术天赋、歌舞才艺、运动强项、爱好特长。活生生的咸鱼一条。
 * Created by tom on 2017/10/14.
 */
public class SalaryTestController implements ContentCtrl, Initializable {

    public VBox root;
    @FXML
    public WebView webview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webview.getEngine().load("http://localhost:80/mgr");
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
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("新增员工工资");
    }
}
