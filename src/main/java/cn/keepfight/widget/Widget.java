package cn.keepfight.widget;

import javafx.scene.Node;

/**
 * 挂件接口
 * Created by tom on 2017/9/24.
 */
public interface Widget {

    /**
     * 获取根节点，一般用于挂载
     */
    Node getRoot();
}
