package cn.keepfight.widget;

/**
 * 以 Grid 格子表现表格的数据源接口
 * <hr/>
 * 这个接口表示的是一行的数据，也就是 ROW
 * Created by tom on 2017/9/22.
 */
public interface TableGridDataSource {

    /**
     * 跨行数，这个用于使用索引列的同步跨行
     */
    int rowSpan();
}
