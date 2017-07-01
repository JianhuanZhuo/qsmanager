package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.DeliverySelection;
import cn.keepfight.qsmanager.model.ReceiptSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static cn.keepfight.qsmanager.controller.PrintSourceLocate.Flag.*;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateMonSup implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(SUP, YEAR, MON));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        return (T) QSApp.service.getReceiptService().selectAll(source.getSup(), source.getYear(), source.getMonth());
    }
}
