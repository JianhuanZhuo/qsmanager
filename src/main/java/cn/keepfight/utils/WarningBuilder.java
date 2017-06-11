package cn.keepfight.utils;

import javafx.scene.control.Alert;

/**
 * 警告窗口
 * Created by tom on 2017/6/7.
 */
public class WarningBuilder {

    public static void build(String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("警告，程序运行中可能出现某些异常！");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void build(String head, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
