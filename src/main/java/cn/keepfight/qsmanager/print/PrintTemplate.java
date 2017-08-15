package cn.keepfight.qsmanager.print;

import javafx.beans.property.*;
import javafx.scene.Node;

/**
 * 打印模板控制器抽象父类
 *
 * @param <T> 打印数据泛化类型参数，指在进行数据填充时传入的参数类型
 *            Created by tom on 2017/6/25.
 */
public abstract class PrintTemplate<T> {

    /**
     * 当前页面索引
     */
    private IntegerProperty pageNow = new SimpleIntegerProperty(0);

    /**
     * 获取内容界面根目录，用于展示面板填充
     */
    abstract public Node getRoot();

    /**
     * 指定外部数据进行数据填充
     *
     * @param datas 需要填充的外部数据
     */
    public void fill(T datas) {
        // do nothing
    }


    /**
     * 可自动计算只读属性，默认不提供
     */
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(false);
    }

    /**
     * 自动计算调用接口，调用该接口触发界面自动计算数据
     */
    public void autoCalculate() {
        // do nothing
    }


    /**
     * 打印前调用
     */
    public void printBefore(){}

    /**
     * 打印后调用
     */
    public void printAfter(){}

    /**
     * 反向将打印表单内的数据打包
     */
    public T pack(){
        return null;
    }

    /**
     * 面板即将取消的通知
     */
    public void cancel(){
    }
}
