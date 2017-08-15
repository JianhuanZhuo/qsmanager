package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.DeliverySelection;
import cn.keepfight.qsmanager.model.OrderSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateMonCust implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(Flag.CUST, Flag.YEAR, Flag.MON));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        OrderSelection selection = new OrderSelection(source.getCust(), source.getYear(), source.getMonth(), null);
        return (T) QSApp.service.getOrderService().selectAll(selection);
    }
}
