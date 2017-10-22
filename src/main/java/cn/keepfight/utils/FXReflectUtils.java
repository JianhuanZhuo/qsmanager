package cn.keepfight.utils;

import cn.keepfight.qsmanager.model.ReceiptDetailModel;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * FX 反射相关工具方法类
 * Created by tom on 2017/6/22.
 */
public class FXReflectUtils {

    /**
     * 批量复制 s 的属性到 t 中，属性列表由 attrs 给出
     * 如果没有对应的值，则默认抛出异常
     */
    public static void attrAssign(Object s, Object t, String... attrs) {
        Stream.of(attrs).forEach(a -> {
            try {
                BeanUtils.setProperty(t, a, BeanUtils.getProperty(s, a));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public static void attrsCopy(Object source, Object target) {
        try {
            BeanUtils.copyProperties(target, source);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 拷贝属性到目标对象，并返回
     */
    public static<T> T attrsCopyAndReturn(Object source, T target){
        try {
            BeanUtils.copyProperties(target, source);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return target;
    }

    public static <T> void connectForColumn(TableColumn<T, String> tab_col) {
        tab_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<T, String> param) {
                ObservableValue x = search("tab_col", param.getValue());
                return x;
            }
        });
    }

    public static ObservableValue search(String name, Object obj) {
        Method[] ms = obj.getClass().getDeclaredMethods();
        for (Method m : ms) {
            m.setAccessible(true);
            if (m.getName().equals(name + "Property")) {
                System.out.println("m.getReturnType().getClass():" + m.getReturnType().getClass());
                if (StringProperty.class.equals(m.getReturnType().getClass())) {
                    System.out.println("");
                }
            }
            try {
                return (ObservableValue<String>) m.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将 tab_serial 映射到 serialProperty 方法
     * tab 开头的全部 TableColumn 对象
     * <pre>
     * tab_serial.setCellValueFactory(param->param.getValue().getPrice().serialProperty())
     * </pre>
     *
     * @param clazz
     * @param <T>
     */
    public static <T> void connect(Class<T> clazz, Object obj) {
        System.out.println("f !!!!!!!!!!!!!!!!!!!!!!!! obj.getClass():" + obj.getClass());
        Field[] fs = obj.getClass().getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            System.out.println("f !!!!!!!!!!!!!!!!!!!!!!!! name:" + f.getName());
            System.out.println("f !!!!!!!!!!!!!!!!!!!!!!!! name:" + f.getClass());
            System.out.println("f !!!!!!!!!!!!!!!!!!!!!!!! name:" + f.getType());
        }
        TableColumn<ReceiptDetailModel, String> tab_name;

        Arrays.stream(fs)
                .filter(f -> f.getType().equals(TableColumn.class))
                .filter(f -> f.getName().startsWith("tab_"))
                .forEach(f -> {
                    System.out.println(f);
                });
//        try {
//            Field x = clazz.getDeclaredField("");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }
}
