package cn.keepfight.utils;


import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 组件强化功能
 * Created by tom on 2017/6/16.
 */
public class FXWidgetUtil {

    /**
     * 属性存储的默认属性文件
     */
    private static final String PROPERTIES_FILE = "fxapp.properties";

    /**
     * 属性数组存储的分割符
     */
    private static final String SPLITER = "~";

    /**
     * 最大属性数组存储尺寸
     */
    private static final int STORE_LIMIT = 5;

    /**
     * 为指定文本框添加默认下拉填充字符串，该功能是基于上下文菜单实现的
     *
     * @param text 需要填充默认字符串的文本框
     * @param list 默认字符串列表
     */
    public static void defaultList(TextField text, List<String> list) {
        if (list == null || text == null || list.isEmpty()) {
            return;
        }

        // 创建上下文菜单
        ContextMenu popup = new ContextMenu();
        for (String s : list) {
            MenuItem item = new MenuItem(s);
            popup.getItems().add(item);
            item.setOnAction(event -> text.setText(s));
        }

        // 监听
        text.setOnMouseClicked(t -> {
            TextField txt = (TextField) t.getSource();
            popup.show(txt, t.getScreenX(), t.getScreenY());
        });
    }

    /**
     * 为指定的文本框匹配相应的默认下拉输入
     *
     * @param textFieldStringPair 指定文本框和与之匹配的下拉属性的数组
     */
    @SafeVarargs
    public static void defaultList(Pair<TextField, String>... textFieldStringPair) {
        Properties ps = ConfigUtil.load(PROPERTIES_FILE);
        for (Pair<TextField, String> pair : textFieldStringPair) {
            defaultList(pair.getKey(), Arrays.asList(((String) ps.get(pair.getValue())).split(SPLITER)));
        }
    }

    /**
     * 为指定属性名的数组添加一个属性值，该新添加值将被插在头，且因数组的长度限制而舍弃最旧的值
     *
     * @param pair 数组属性名和新增属性值的 Pair 对数组
     */
    @SafeVarargs
    public static void addDefaultList(Pair<String, String>... pair) {
        Properties ps = ConfigUtil.load(PROPERTIES_FILE);
        for (Pair<String, String> p : pair) {
            if (p.getValue() == null || p.getValue().trim().equals("")) {
                continue;
            }
            List<String> ls = new LinkedList<>(Arrays.asList(((String) ps.get(p.getKey())).split(SPLITER)));
            ls.add(0, p.getValue());
            ps.setProperty(p.getKey(), ls.stream()
                    .distinct()
                    .limit(STORE_LIMIT)
                    .collect(Collectors.joining(SPLITER)));
        }
        ConfigUtil.store(PROPERTIES_FILE, ps);
    }

    /**
     * 对可变数组进行统计监听
     * @param obserList 需要监听的可变数组
     * @param toDecimal 对可变数组的某一元素转化为数字特征
     * @param consumer 统计结果的消费器
     * @param accumulator 对于数组的所有数字特征进行合计的方法
     * @param <T> 可变数组的泛化类型
     */
    public static <T> void calculate(ObservableList<T> obserList, Function<T, BigDecimal> toDecimal,
                                     Consumer<String> consumer, BinaryOperator<BigDecimal> accumulator) {
        // 默认值
        consumer.accept("0");

        // 添加监听
        obserList.addListener((ListChangeListener<T>) c -> {
                    Optional t = obserList.stream()
                            .map((item)->{
                                try {
                                    return toDecimal.apply(item);
                                }catch (Exception e){
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .reduce(accumulator);
                    String text = "0";
                    if (t.isPresent()) {
                        text = t.get().toString();
                    }
                    consumer.accept(text);
                }
        );
    }

    /**
     * 对可变数组进行统计监听，该重载方法使用了加法作为默认合计方法
     * @param obserList 需要监听的可变数组
     * @param toDecimal 对可变数组的某一元素转化为数字特征
     * @param consumer 统计结果的消费器
     * @param <T> 可变数组的泛化类型
     */
    public static <T>  void calculate(ObservableList<T> obserList, Function<T, BigDecimal> toDecimal,
                                      Consumer<String> consumer){
        calculate(obserList, toDecimal, consumer, BigDecimal::add);
    }

    /**
     * 破解提示工具 Tooltip，修改其触发时间为 200 毫秒
     * 参考：https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay
     * @param tooltip 需要被破解的提示工具
     */
    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(200)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
