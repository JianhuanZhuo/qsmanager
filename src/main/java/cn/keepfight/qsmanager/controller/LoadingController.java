package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.Properties;

/**
 * 加载中。。。
 * Created by tom on 2017/9/6.
 */
public class LoadingController implements ContentCtrl {
    @FXML
    private VBox root;

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
        return FXWidgetUtil.sBinding("加载中");
    }
}
