package cn.keepfight.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/6/5.
 */
public class ViewPathUtilTest {
    @Test
    public void getFrameView() throws Exception {
        System.out.println(ViewPathUtil.getFrameView("main.fxml"));
    }

    @Test
    public void loadView() throws Exception {
    }

    @Test
    public void getLoader() throws Exception {
    }

    @Test
    public void loadViewForController() throws Exception {
    }

    @Test
    public void testTime() {
        StringProperty x = new SimpleStringProperty("xx");
        StringProperty y = new SimpleStringProperty("yy");

        x.bind(y);

        y.set("yy2");
        System.out.println(x.get());
        y.unbind();
        y.set("yy3");
        System.out.println(x.get());
    }
}