package cn.keepfight.widget;

import cn.keepfight.utils.FXUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * 年 拾取器
 * Created by tom on 2017/9/22.
 */
public class YearPicker extends HBox {

    private IntegerProperty year = new SimpleIntegerProperty(
            FXUtils.stampToLocalDate(System.currentTimeMillis()).getYear());

    private TextField textField = new TextField();

    public YearPicker(int year) {
        String path = YearPicker.class.getResource("/widget/yearpicker.css").toExternalForm();
        System.out.println(path);
        getStylesheets().add(path);

        Button up = new Button("▲");
        up.getStyleClass().addAll("up", "btn");
        up.setOnAction(event -> up());

        Button down = new Button("▼");
        down.getStyleClass().addAll("down", "btn");
        down.setOnAction(event -> down());

        textField.setDisable(true);
        textField.setText("" + year + "年");
        textField.getStyleClass().add("field");

        this.getChildren().addAll(textField, new VBox(up, down));
    }


    /**
     * 设置年份，有效年份必须为 year > 1960 && year<2080
     *
     * @param year 欲设置的年份值
     */
    public void setYear(int year) {
        if (year > 1960 && year < 2080) {
            this.year.set(year);
            textField.setText("" + year + "年");
        }
    }

    /**
     * 获得当前组件表示的年份值
     */
    public int getYear() {
        return year.get();
    }

    /**
     * 年份只读属性值
     */
    public ReadOnlyIntegerProperty yearProperty() {
        return year;
    }

    /**
     * 向上加一年
     */
    public void up() {
        setYear(getYear() + 1);
    }

    /**
     * 向下减一年
     */
    public void down() {
        setYear(getYear() - 1);
    }
}
