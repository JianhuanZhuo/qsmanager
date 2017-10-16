package cn.keepfight.widget;

import javafx.scene.Node;

import java.util.function.Consumer;

/**
 * 挂件接口
 * Created by tom on 2017/9/24.
 */
public interface PopupWidget<T> extends Widget<T> {

    /**
     * 调用时弹出显示
     */
    void show();

    /**
     * 调用时弹出显示
     */
    default void show(Node node){
        locateNode(node);
        show();
    }

    /**
     * 调用时弹出显示
     */
    default void show(Node node, T data){
        locateNode(node);
        set(data);
        show();
    }

    /**
     * 注册关闭时响应调用
     */
    void setOnClose(Consumer<T> onClose);

    /**
     * 值改变时响应调用
     */
    void setOnValueChange(Consumer<T> onValueChange);

    /**
     * 位置上绑定到指定节点
     */
    void locateNode(Node node);
}
