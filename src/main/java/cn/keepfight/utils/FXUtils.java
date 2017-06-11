package cn.keepfight.utils;

import javafx.scene.Node;
import javafx.scene.control.TextInputControl;
import javafx.util.Callback;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Arrays;

/**
 * FX 工具集合类
 * Created by tom on 2017/6/6.
 */
public class FXUtils {

    /**
     * 添加节点样式，由于 FXML 中样式的存储以 List 存储，所以可能导致多次存入相同样式的可能
     *
     * @param style 指定需要添加的样式
     * @param node  需要进行样式添加操作操作的节点列表
     */
    public static void addStyle(String style, Node... node) {
        Arrays.stream(node).forEach(n -> {
            if (!n.getStyleClass().contains(style)) {
                n.getStyleClass().add(style);
            }
        });
    }

    /**
     * 删除节点样式，由于 FXML 中样式的存储以 List 存储，所以可能导致多次存入相同样式的可能
     * @param style 指定需要删除的样式
     * @param node 需要进行样式删除操作操作的节点列表
     */
    public static void delStyle(String style, Node... node) {
        Arrays.stream(node).forEach(n -> {
            while (n.getStyleClass().contains(style)) {
                n.getStyleClass().remove(style);
            }
        });
    }

    public static <T, R> R getMapper(SqlSessionFactory sqlSessionFactory, Class<T> clazz, ConsumerTR<T, R> consumer) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return consumer.accept(session.getMapper(clazz));
        }
    }


    public static <T, P> void getMapper(SqlSessionFactory sqlSessionFactory, Class<T> clazz, ConsumerT<T, P> consumer, P p) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            consumer.accept(session.getMapper(clazz), p);
        }
    }

    public static <T, R, P> R getMapper(SqlSessionFactory sqlSessionFactory, Class<T> clazz, ConsumerTRP<T, R, P> consumer, P p) throws Exception {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            return consumer.accept(session.getMapper(clazz), p);
        }
    }

    @FunctionalInterface
    public interface ConsumerTR<T, R> {
        R accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface ConsumerT<T, P> {
        void accept(T t, P p) throws Exception;
    }

    @FunctionalInterface
    public interface ConsumerTRP<T, R, P> {
        R accept(T t, P p) throws Exception;
    }


    public static void limitLength(TextInputControl textInputControl, int limit) {
        textInputControl.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (textInputControl.getText().length() >= limit) {
                    // if it's 11th character then just setText to previous one
                    textInputControl.setText(textInputControl.getText().substring(0, limit));
                }
            }
        });
    }

    public static void limitPattern(TextInputControl textInputControl, String pattern) {
        textInputControl.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!textInputControl.getText().matches(pattern)) {
                    //when it not matches the pattern (1.0 - 6.0) set the textField empty
                    textInputControl.setText("");
                }
            }
        });
    }

    /**
     * 纯数字限制
     *
     * @param textInputControl
     * @param inte             最大整数限制
     * @param deci             最大小数限制
     * @param allowNega        是否允许负数输入
     */
    public static void limitNum(TextInputControl textInputControl, int inte, int deci, boolean allowNega) {
        if (inte < 1) {
            return;
        }
        String pattern = "\\d{0," + inte + "}";
        if (allowNega) {
            pattern = "-?" + pattern;
        }
        if (deci > 0) {
            pattern += "(\\.\\d{0," + deci + "})?";
        }
        limitPattern(textInputControl, pattern);
    }
}
