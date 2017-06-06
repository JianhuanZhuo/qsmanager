package cn.keepfight.utils;

import javafx.scene.Node;

import java.util.Arrays;

/**
 * Created by tom on 2017/6/6.
 */
public class FXUtils {

    public static void addStyle(String style, Node... node) {
        Arrays.stream(node).forEach(n -> {
            if (!n.getStyleClass().contains(style)) {
                n.getStyleClass().add(style);
            }
        });
    }

    public static void delStyle(String style, Node... node) {
        Arrays.stream(node).forEach(n -> {
            while (n.getStyleClass().contains(style)) {
                n.getStyleClass().remove(style);
            }
        });
    }
}
