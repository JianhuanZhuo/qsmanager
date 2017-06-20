package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.ImageLoadUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 图片查看器界面控制器
 * Created by tom on 2017/6/18.
 */
public class PicViewer  implements DialogContent<String>, Initializable {

    @FXML private VBox root;
    @FXML private ImageView imageLoader;

    @Override
    public void init() {
        setPicurl("");
    }

    @Override
    public void fill(String s) {
        setPicurl(s);
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        return new SimpleBooleanProperty(false);
    }

    @Override
    public String pack() {
        return "";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    private void setPicurl(String absolutePath) {
        if (absolutePath != null && !absolutePath.equals("")) {
            ImageLoadUtil.bindImageDirectly(imageLoader, absolutePath);
        }
    }
}
