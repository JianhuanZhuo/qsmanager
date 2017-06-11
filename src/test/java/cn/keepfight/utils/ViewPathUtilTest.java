package cn.keepfight.utils;

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

}