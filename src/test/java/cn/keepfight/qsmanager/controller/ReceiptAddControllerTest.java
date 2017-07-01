package cn.keepfight.qsmanager.controller;

import cn.keepfight.utils.ViewPathUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/6/22.
 */
public class ReceiptAddControllerTest {
    @Test
    public void initialize() throws Exception {
        ViewPathUtil.loadViewForController("receipt_add.fxml");
    }

}