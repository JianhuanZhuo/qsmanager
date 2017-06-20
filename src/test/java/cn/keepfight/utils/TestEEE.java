package cn.keepfight.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;

/**
 *
 * Created by tom on 2017/6/11.
 */
class TestEEE {

    @Test
    public void testTime() {
        StringProperty x = new SimpleStringProperty("xx");
        StringProperty y = new SimpleStringProperty("yy");

        x.bind(y);

        y.set("yy2");
        System.out.println(x.get());
        x.unbind();
        y.set("yy3");
        System.out.println(x.get());
    }
}
