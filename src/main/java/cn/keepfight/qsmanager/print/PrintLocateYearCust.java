package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateYearCust implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(Flag.CUST, Flag.YEAR));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        System.out.println("source.getCust(), source.getYear():"+source.getCust()+"--"+source.getYear());
        return (T) QSApp.service.getCustAnnualService().selectAnnual(source.getCust(), source.getYear());
    }
}
