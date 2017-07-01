package cn.keepfight.qsmanager.controller;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 打印源选择器
 * Created by tom on 2017/6/26.
 */
public interface PrintSourceLocate {

    /**
     * 标志位枚举
     */
    enum Flag{
        CUST, SUP, YEAR, MON, SERIAL
    }

    /**
     * 集合标志位
     */
    Set<Flag> reqFlag();

    /**
     * 查询需要客户信息
     */
    default boolean reqCust() {
        return reqFlag().contains(Flag.CUST);
    }

    /**
     * 查询需要供应商信息
     */
    default boolean reqSup() {
        return reqFlag().contains(Flag.SUP);
    }


    /**
     * 查询需要年份信息
     */
    default boolean reqYear() {
        return reqFlag().contains(Flag.YEAR);
    }

    /**
     * 查询需要月份信息
     */
    default boolean reqMon() {
        return reqFlag().contains(Flag.MON);
    }

    /**
     * 查询需要编号信息
     */
    default boolean reqSerial() {
        return reqFlag().contains(Flag.SERIAL);
    }

    /**
     * 根据对象ID、年月份信息查询获得编号列表
     */
    default List<Pair<Long, String>> getSerialPair(Long obj, Long year, Long mon) throws Exception{
        return new ArrayList<>(1);
    }

    /**
     * 执行查询动作
     */
    <T> T query(PrintSource source) throws Exception ;
}
