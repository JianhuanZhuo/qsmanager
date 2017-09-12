package cn.keepfight.qsmanager.controller;

/**
 * 上面那一栏的按钮
 * Created by tom on 2017/9/3.
 */
public interface BarBtn {
    /**
     * 按钮文本
     */
    String getText();

    /**
     * 提示文本
     */
    String getHit();

    /**
     * icon 图片路径
     */
    String getImage();

    /**
     * 获得点击事件响应处理
     */
    Runnable getAction();
}
