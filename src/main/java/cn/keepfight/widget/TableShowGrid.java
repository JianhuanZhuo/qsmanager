package cn.keepfight.widget;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.function.Function;

/**
 * 用 Grid 显示的表格，该类表格应该是不可编辑的
 * 应用于需要固定长度-边界不可出现重线的打印
 * <hr/>
 * 数据抽取=>数据表现。在这里的表现仅表现为字符串与具体数据的转换<br/>
 * 比如说，
 * 由于使用 row 行 为单元数据，那么内容中的全部数据皆为一致表现。
 * <hr/>
 * 数据关联
 *
 * Created by tom on 2017/9/22.
 */
public class TableShowGrid extends GridPane{
    // 转化问题


    public void setHeadStr(){

    }

    public void setHeadRow(){

    }


    public void setContent(){
        this.getColumnConstraints().add(new ColumnConstraints());
    }

    /**
     * 从 1 到 n 的序号，怎么表示出来
     * @param title 行头标题
     * @param intToStr 数字到字符串的转换器
     */
    public void setIndexColumn(String title, Function<String, Integer> intToStr){
    }
}
