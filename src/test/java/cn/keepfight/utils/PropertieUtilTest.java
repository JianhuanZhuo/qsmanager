package cn.keepfight.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 属性加载器测试
 * Created by tom on 2017/6/5.
 */
public class PropertieUtilTest {
    @Test
    public void loadProperties() throws Exception {
        assertEquals(PropertieUtil.loadProperties("test.properties").get("test1"), "1");
    }

    @Test
    public void loadProperties1() throws Exception {
        assertEquals(PropertieUtil.loadProperties("noexist.properties", "test.properties")
                .get("test1"), "1");
    }

}