package cn.keepfight.qsmanager.controller;

import javafx.scene.Node;

/**
 * 中间子面板的控制器的共性接口
 * Created by tom on 2017/6/5.
 */
public interface ContentController {

    Node getRoot();

    void refresh();
}
