package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.SupAnnualModelFull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/6/12.
 */
public class LocalServiceImplTest {
    @Test
    public void generateName() throws Exception {
        SupAnnualModelFull f = QSApp.service.getSupAnnualService().selectAnnual(4L, 2017L);

    }

}