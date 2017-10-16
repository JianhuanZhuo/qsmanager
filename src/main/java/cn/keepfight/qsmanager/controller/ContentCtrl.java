package cn.keepfight.qsmanager.controller;

import javafx.beans.binding.StringBinding;
import javafx.scene.Node;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 中间子面板的控制器的共性接口
 * Created by tom on 2017/6/5.
 */
public interface ContentCtrl {

    Node getRoot();

    /**
     * 该接口在界面加载完成后只调用一次，该调用一般会延迟到首次显示时调用
     */
    void loaded();

    /**
     * 带参数的界面调用显示
     * @param params 调用页面前时使用
     */
    void showed(Properties params);;

    /**
     * 带参数的界面调用显示
     * @param params 调用页面后使用
     */
    default void showedAfter(Properties params){

    }

    default<T> void updateState(T s){}

    /**
     * 获得页面的 title
     * @return 页面 title，将被显示页面上方
     */
    StringBinding getTitle();

    /**
     * 是否使用透明背景，是则不显示 title
     */
    default boolean transparentBackground(){
        return false;
    }

    /**
     * 获取按钮列表，在这个基础上会再加一个刷新按钮
     * @param params 页面打开参数
     * @return 默认返回一个空的列表
     */
    default List<BarBtn> getBarBtns(Properties params){
        return new ArrayList<>(1);
    }

    /**
     * 当页面隐藏时调用
     * @return 返回表示
     */
    default boolean hide(){
        return true;
    }
}
