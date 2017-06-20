package cn.keepfight.utils;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.WritableImage;
import javafx.util.StringConverter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

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


    /**
     * 限制输入控件的输入字符串长度
     */
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

    /**
     * 限制输入控件的输入字符串内容
     */
    public static void limitPattern(TextInputControl textInputControl, String pattern, String defaultStr) {
        textInputControl.setText(defaultStr);
        textInputControl.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if (!textInputControl.getText().trim().matches(pattern)) {
                    //when it not matches the pattern (1.0 - 6.0) set the textField empty
                    textInputControl.setText(defaultStr);
                }else {
                    textInputControl.setText(textInputControl.getText().trim());
                }
            }
        });
    }

    /**
     * 纯数字限制
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
        limitPattern(textInputControl, pattern, "0");
    }


    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 转换时间戳为时期字符串
     */
    public static String stampToDate(Long stamp){
        if (stamp==null){
            return "";
        }
        return formatter.format(new Date(stamp));
    }

    /**
     * 转时间戳为本地时间对象
     */
    public static LocalDate stampToLocalDate(Long stamp){
        if (stamp==null){
            stamp = System.currentTimeMillis();
        }
        return new Timestamp(stamp).toLocalDateTime().toLocalDate();
    }

    /**
     * 带默认值的分数字符串转换，若转换错误则返回默认值
     * @param str 需要转换的分数
     * @param defaultValue 默认值
     */
    public static BigDecimal getDecimal(String str, BigDecimal defaultValue){
        try {
            return new BigDecimal(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public static String decimalStr(BigDecimal d){
        if (d==null){
            return "0";
        }else {
            return d.toString();
        }
    }

    /**
     * 将指定路径的本地图片进行另存为，注意是本地图片，其中图片名并不对应上
     * @param picUrl 图片的路径，如 D:/pic/xx.png
     * @return 包装后的图片URL，也就是 file:D:/xx/xx.png，或者 http:/xxx/xx/xx.png
     */
    public static String copyAndTransform(String picUrl) throws IOException {
        Path source = Paths.get(picUrl);
        String genFileName = generateName(source.getFileName().toString());
        File targetFile = new File("./pic/" + genFileName).getCanonicalFile();
        while (targetFile.exists()){
            genFileName = generateName(genFileName);
            targetFile = new File("./pic/" + genFileName).getCanonicalFile();
        }
        Path target = targetFile.toPath();
        Files.copy(source, target);
        // 添加 file: 本地文件连接
        return "file:" + targetFile.getAbsolutePath();
    }

    /**
     * 随机修改文件名
     */
    public static String generateName(String source){
        return "gen"+(source+System.currentTimeMillis()).hashCode()+ source.substring(source.lastIndexOf('.'));
    }

    /**
     * 将指定图片写入到文件中，并返回这个文件名
     */
    public static String writeImage(WritableImage image) throws IOException {
        String genFileName = "snapshot"+System.currentTimeMillis()+".png";
        File targetFile = new File("./pic/" + genFileName).getCanonicalFile();
        while (targetFile.exists()){
            genFileName = generateName(genFileName);
            targetFile = new File("./pic/" + genFileName).getCanonicalFile();
        }
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", targetFile);
        return targetFile.getAbsolutePath();
    }

    /**
     * 构造转换器，在传入为 null 对象时使用空字符串作为转换返回值
     */
    public static <T> StringConverter<T> converter(Function<T, String> convert){
        return converter(convert, "");
    }

    /**
     * 构造转换器
     * @param nullValue 在传入对象为空时，默认的转换值
     */
    public static <T> StringConverter<T> converter(Function<T, String> convert, String nullValue){
        return new StringConverter<T>() {
            @Override
            public String toString(T object) {
                if (Objects.isNull(object)){
                    return nullValue;
                }else {
                    return convert.apply(object);
                }
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        };
    }
}
