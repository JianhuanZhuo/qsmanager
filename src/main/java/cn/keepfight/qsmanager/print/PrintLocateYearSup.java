package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateYearSup implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(Flag.SUP, Flag.YEAR));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        return (T) QSApp.service.getSupAnnualService().selectAnnual(source.getSup(), source.getYear());
    }
}
