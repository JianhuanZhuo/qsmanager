package cn.keepfight.qsmanager.controller;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * 这仅仅作为一个初始状态的控制器
 * Created by tom on 2017/6/6.
 */
public class Init implements ContentCtrl {
    public AnchorPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public void showed() {

    }
}
