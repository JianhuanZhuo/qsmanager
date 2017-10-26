package cn.keepfight.qsmanager.controller.tax;

import cn.keepfight.qsmanager.controller.ContentCtrl;
import javafx.beans.binding.StringBinding;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TaxController implements ContentCtrl, Initializable {
    public VBox root;
    public Button btn_ym;

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void loaded() {

    }

    @Override
    public StringBinding getTitle() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
