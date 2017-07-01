package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static cn.keepfight.qsmanager.controller.PrintSourceLocate.Flag.CUST;
import static cn.keepfight.qsmanager.controller.PrintSourceLocate.Flag.SUP;
import static cn.keepfight.qsmanager.controller.PrintSourceLocate.Flag.YEAR;

/**
 * 送货单定位
 * Created by tom on 2017/6/27.
 */
public class PrintLocateYearCust implements PrintSourceLocate {
    @Override
    public Set<Flag> reqFlag() {
        return new HashSet<>(Arrays.asList(CUST, YEAR));
    }

    @Override
    public <T> T query(PrintSource source) throws Exception {
        return (T) QSApp.service.getCustAnnualService().selectAnnual(source.getCust(), source.getYear());
    }
}
