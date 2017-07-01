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
public class PrintLocateDelivery implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(CUST, YEAR, MON, SERIAL));
    }

    @Override
    public List<Pair<Long, String>> getSerialPair(Long obj, Long year, Long mon) throws Exception {
        DeliverySelection selection = new DeliverySelection(obj, year, mon, null);
        return QSApp.service.getDeliveryService().selectAll(selection)
                .stream()
                .map(d -> new Pair<>(d.getId(), d.getSerial())).collect(Collectors.toList());
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        return (T) QSApp.service.getDeliveryService().selectByID(source.getItem());
    }
}
