package cn.keepfight.widget;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

/**
 * 挂件接口
 * Created by tom on 2017/9/24.
 */
public interface Widget<T> {

    /**
     * 获取根节点，一般用于挂载
     */
    Node getRoot();

    /**
     * 定位在某节点位置下
     * @param node 挂靠节点
     */
    default void locate(Node node){}

    /**
     * 设置挂件表示的数据
     */
    default void set(T data){
        throw new RuntimeException("not support operation in jh!");
    }

    /**
     * 获得对象的数据
     */
    default T get(){
        throw new RuntimeException("not support operation in jh!");
    }

    /**
     * 获得对象属性对象
     */
    default ObjectProperty<T> getDataProperty(){
        throw new RuntimeException("not support operation in jh!");
    }
}
