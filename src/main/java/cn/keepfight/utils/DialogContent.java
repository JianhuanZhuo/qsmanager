package cn.keepfight.utils;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;


/**
 * 用于展现面板的内容接口
 * Created by tom on 2017/6/7.
 */
public interface DialogContent<T> {

    void init();

    void fill(T t);

    Node getContent();

    BooleanProperty allValid();

    default T resultConverter(ButtonType type) {
        if (!type.getButtonData().isCancelButton())
            return pack();
        return null;
    }

    /**
     * 将数据进行打包，并封装为一个 T 对象
     * 需要注意的是，那些默认的值也要上哦
     */
    T pack();
}
