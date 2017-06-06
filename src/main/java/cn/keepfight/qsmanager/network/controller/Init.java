package cn.keepfight.qsmanager.network.controller;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * 这仅仅作为一个初始状态的控制器
 * Created by tom on 2017/6/6.
 */
public class Init implements ContentController {
    public AnchorPane root;

    @Override
    public Node getRoot() {
        return root;
    }
}
