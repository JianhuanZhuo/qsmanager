package cn.keepfight.qsmanager.controller;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.Properties;

/**
 * 窗口适配
 * Created by tom on 2017/9/5.
 */
public class DialogAdapter implements ContentCtrl{
    @FXML private VBox root;

    private StringProperty titleProperty = new SimpleStringProperty();

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
        // 绑定到到标题属性上
        return new StringBinding() {
            {
                bind(titleProperty);
            }
            @Override
            protected String computeValue() {
                return titleProperty.get();
            }
        };
    }
}
