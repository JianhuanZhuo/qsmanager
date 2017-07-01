package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.DeliverySelection;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.keepfight.qsmanager.controller.PrintSourceLocate.Flag.*;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateMonCust implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(CUST, YEAR, MON));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        DeliverySelection selection = new DeliverySelection(source.getCust(), source.getYear(), source.getMonth(), null);
        return (T) QSApp.service.getDeliveryService().selectAll(selection);
    }
}
