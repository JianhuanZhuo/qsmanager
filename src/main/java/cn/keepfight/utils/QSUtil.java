package cn.keepfight.utils;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * QS 管理软件相关工具
 * Created by tom on 2017/6/20.
 */
public class QSUtil {

    /**
     * 订单号格式
     */
    private static SimpleDateFormat orderSerialFormatter = new SimpleDateFormat("yyMMdd");

    public static String orderSerial(Long stamp, Long ct) {
        return orderSerialFormatter.format(new Date(stamp)) + String.format("%03d", ct);
    }

    public static <T> void refreshItems(List<T> items, T item) {
        int i = items.indexOf(item);
        items.remove(item);
        items.add(i, item);
    }

    /**
     * 设置安利输入格式：70盒/箱×28箱/板×12板+40盒
     *
     * @param p 需要设置格式的输入框
     * @return 输入框格式化后的值属性
     */
    public static ObjectProperty<AnliPack> setAnliPack(TextField p) {
        TextFormatter<AnliPack> formatter = new TextFormatter<>(
                new StringConverter<AnliPack>() {
                    @Override
                    public String toString(AnliPack o) {
                        return Objects.isNull(o) ? "" : o.toString();
                    }

                    @Override
                    public AnliPack fromString(String s) {
                        return AnliPack.toAnliPack(s);
                    }
                },
                AnliPack.defaultPack(),//默认包装
                x -> {
                    if (!x.isContentChange()) {
                        return x;
                    }
                    if (x.getControlNewText().matches("\\d+盒/箱×\\d+箱/板×\\d+板(\\+)")) {
                        TextFormatter.Change c = x.clone();
                        c.setText("+40盒");
                        return c;
                    }
                    if (x.getControlNewText().matches("[1-9]\\d*盒/箱×[1-9]\\d*箱/板×[1-9]\\d*板\\+\\d+")) {
                        TextFormatter.Change c = x.clone();
                        c.setRange(0, c.getRangeEnd());
                        c.setText(x.getControlText().replaceAll("\\+\\d+盒", ""));
                        return c;
                    }

                    // 内容清空，允许通过
                    if (x.getControlNewText().equals("")) {
                        return x;
                    }

                    if (x.getControlNewText().matches("[1-9]\\d*盒/箱×[1-9]\\d*箱/板×[1-9]\\d*板(\\+[1-9]\\d*盒)?")) {
                        return x;
                    } else {
                        return null;
                    }
                });
        p.setTextFormatter(formatter);
        return formatter.valueProperty();
    }

    public static class AnliPack {
        int per1; // 每盒
        int per2; // 每箱
        int per3; // 每板
        int add; // 另外加的

        private static Pattern p = Pattern.compile("(\\d+)盒/箱×(\\d+)箱/板×(\\d+)板(\\+(\\d+)盒)?");

        public static AnliPack defaultPack() {
            return toAnliPack("70盒/箱×28箱/板×25板");
        }

        public static AnliPack toAnliPack(String str) {
            Matcher m = p.matcher(str);
            if (m.matches()) {
                AnliPack a = new AnliPack();
                try {
                    a.per1 = Integer.valueOf(m.group(1));
                    a.per2 = Integer.valueOf(m.group(2));
                    a.per3 = Integer.valueOf(m.group(3));
                    try {
                        System.out.println(m.groupCount());
                        a.add = Integer.valueOf(m.group(5));
                    }catch (Exception e){}
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return a;
            }
            return null;
        }

        @Override
        public String toString() {
            return per1 + "盒/箱×" + per2 + "箱/板×" + per3 + "板" + (add == 0 ? "" : ("+" + add + "盒"));
        }

        public int count(){
            return per1*per2*per3+add;
        }
    }
}
