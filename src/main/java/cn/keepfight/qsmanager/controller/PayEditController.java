package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Properties;

/**
 * 收款方式选择
 * Created by tom on 2017/9/6.
 */
public class PayEditController implements ContentCtrl {
    public HBox root;
    public VBox payList;

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
        return FXWidgetUtil.sBinding("收款方式选择");
    }
}
